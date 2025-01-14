package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.CUInfoAutoFill;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("SELECT * FROM employee WHERE username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     * @param employee
     */
    @Insert("INSERT INTO employee(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES" +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @CUInfoAutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询员工
     * @param name
     * @return
     */
    Page<Employee> pageQuery(@Param(value = "name") String name);

    /**
     * 根据主键动态修改employee数据
     * @param employee
     */
    @CUInfoAutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee selectById(Long id);
}
