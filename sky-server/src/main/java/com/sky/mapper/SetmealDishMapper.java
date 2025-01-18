package com.sky.mapper;

import com.sky.bo.DishSetmealBO;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据套餐id删除套餐菜品关系
     * @param setmealIds
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐id查询套餐菜品关系
     * @param setmealId
     * @return
     */
    @Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    List<SetmealDish> selectBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐菜品
     * @param id
     * @return
     */
    @Select("SELECT d.name, d.description, d.image, sd.copies FROM setmeal_dish sd LEFT OUTER JOIN dish d ON sd.dish_id = d.id WHERE sd.setmeal_id = #{id}")
    List<DishItemVO> getDishItemBySetmealId(Long id);
}
