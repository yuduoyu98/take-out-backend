package com.sky.mapper;

import com.sky.bo.DishSetmealBO;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 查询所给菜品中在套餐里或在售的菜品
     * @param dishIds
     * @return
     */
    List<DishSetmealBO> selectOnSaleInSetmealDishes(List<Long> dishIds);

    /***
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
