package com.sky.aspect;

import com.sky.annotation.CUInfoAutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.CreateUpdateInfo;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 在对数据库进行INSERT和UPDATE时，自动填充创建时间、创建人、更新时间、更新人等信息
 */
@Aspect
@Component
@Slf4j
public class CUInfoAutomationAspect {

    // execution(返回类型 包名.类名.方法名(参数类型) 异常类型)
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.CUInfoAutoFill)")
    // 可重用的切点名称
    public void CUInfoAutoFill() {
    }

    @Before("CUInfoAutoFill()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("自动填充创建和更新信息");

        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        // 1.获取注解的值 (操作类型 INSERT / UPDATE)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CUInfoAutoFill annotation = method.getAnnotation(CUInfoAutoFill.class);
        if (annotation == null || annotation.value() == null) {
            return;
        }
        OperationType operationType = annotation.value();

        // 2.从方法入参中获取对应需要自动填充的实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof CreateUpdateInfo) {
                // 3.反射填充实体对象的创建和更新信息
                try {
                    Class<?> clazz = arg.getClass();
                    Method setUpdateTime = clazz.getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                    Method setUpdateUser = clazz.getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                    setUpdateTime.invoke(arg, now);
                    setUpdateUser.invoke(arg, id);

                    if (operationType == OperationType.INSERT) {
                        Method setCreateTime = clazz.getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                        Method setCreateUser = clazz.getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                        setCreateTime.invoke(arg, now);
                        setCreateUser.invoke(arg, id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
