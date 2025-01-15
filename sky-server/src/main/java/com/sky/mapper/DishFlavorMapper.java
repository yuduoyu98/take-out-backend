package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

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
}
