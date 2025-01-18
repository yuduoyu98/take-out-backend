package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * 套餐管理
 */
public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void add(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param id
     * @param status
     */
    void changeStatus(Long id, Integer status);

    /**
     * 根据套餐id查询套餐包含的菜品
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    /**
     *根据分类id和status查询套餐
     * @param categoryId
     * @param status
     * @return
     */
    List<Setmeal> list(Long categoryId, Integer status);
}
