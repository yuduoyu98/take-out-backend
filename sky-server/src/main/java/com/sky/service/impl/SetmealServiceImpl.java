package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 套餐管理
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        // 保存套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        // 保存套餐菜品关系
        Long setmealId = setmeal.getId(); // 回填ID
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.<SetmealVO>startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * - 套餐启售中无法删除
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        // 套餐启售中无法删除
        List<Long> odSaleSetmealIds = setmealMapper.onSaleIds(ids);
        if (!odSaleSetmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        // 删除套餐
        setmealMapper.deleteByIds(ids);
        // 删除套餐关联菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = setmealMapper.selectById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        // 更新套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 更新套餐菜品关系
        Long setmealId = setmeal.getId();
        // 1. 删除原先绑定的套餐菜品关系
        setmealDishMapper.deleteBySetmealIds(Collections.singletonList(setmealId));
        // 2. 保存新的套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐起售、停售
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(Long id, Integer status) {
        if (status == StatusConstant.ENABLE) {
            // 起售套餐时，如果套餐内包含停售的菜品，则不能起售
            List<Dish> dishes = dishMapper.getBySetmealId(id);
            if (dishes != null && dishes.size() > 0) {
                dishes.forEach(dish -> {
                    if (dish.getStatus() == StatusConstant.DISABLE) {
                        log.debug("套餐中存在启售菜品：{}", dish.getId());
                        throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
