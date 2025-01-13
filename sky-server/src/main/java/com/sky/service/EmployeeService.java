package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     *
     * @param employeeDTO
     * @returne
     */
    void addEmployee(EmployeeDTO employeeDTO);


    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据id修改员工状态
     * @param id
     * @param status
     */
    void updateEmployeeStatus(Long id, Integer status);

    Employee getEmployeeById(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);
}
