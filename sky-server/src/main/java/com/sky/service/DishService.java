package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * 菜品管理
 */
public interface DishService {

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    void addDishWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteDishes(List<Long> idsToDel);
}
