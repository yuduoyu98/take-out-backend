package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     * 批量删除菜品
     * @param ids
     */
    void deleteDishes(List<Long> ids);

    /**
     * 菜品起售、停售
     * @param id
     * @param status
     */
    void updateDishStatus(Long id, Integer status);

    /**
     * 根据ID查询菜品
     * @param id
     * @return
     */
    DishVO getDishById(Long id);

    /**
     * 更新菜品
     * @param dishDTO
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getDishesByCategory(Long categoryId);

    /**
     * 根据分类ID查询菜品以及菜品口味
     * @param categoryId
     * @param status
     * @return
     */
    List<DishVO> listWithFlavor(Long categoryId, Integer status);
}
