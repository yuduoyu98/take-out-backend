package com.sky.mapper;

import com.sky.bo.DishSetmealBO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 */
@Mapper
public interface DishSetmealMapper {

    /**
     * 查询所给菜品中在套餐里或在售的菜品
     * @param dishIds
     * @return
     */
    List<DishSetmealBO> selectOnSaleInSetmealDishes(List<Long> dishIds);

}
