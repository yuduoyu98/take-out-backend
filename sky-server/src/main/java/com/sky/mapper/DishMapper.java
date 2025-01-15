package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.CUInfoAutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID批量删除菜品
     * @param ids
     * @return
     */
    void deleteDishesByIds(List<Long> ids);

    /**
     * 根据ID查询菜品
     * @param id
     * @return
     */
    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish selectById(Long id);

    @CUInfoAutoFill(OperationType.UPDATE)
    void update(Dish dish);
}
