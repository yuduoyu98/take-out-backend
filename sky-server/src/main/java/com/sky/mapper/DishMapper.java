package com.sky.mapper;

import com.sky.annotation.CUInfoAutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("SELECT count(id) FROM dish WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 添加菜品
     * @param dish
     * @return dish ID
     */
    @CUInfoAutoFill(OperationType.INSERT)
    void insert(Dish dish);

}
