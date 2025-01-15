package com.sky.mapper;

import com.sky.annotation.CUInfoAutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 */
@Mapper
public interface DishFlavorMapper {


    /**
     * 批量写入菜品口味
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品ID删除对应口味信息
     * @param dishIds
     */
    void deleteFlavorsByDishIds(List<Long> dishIds);

    // todo
    @CUInfoAutoFill(OperationType.UPDATE)
    @Update("UPDATE dish_flavor SET flavor = #{flavor} WHERE id = #{id}")
    void update();

    /**
     * 根据菜品ID查询口味
     * @param dishId
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);
}
