package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.CUInfoAutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 添加套餐
     * @param setmeal
     */
    @CUInfoAutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 查出所给套餐中已启售的套餐id
     * @param ids
     * @return
     */
    List<Long> onSaleIds(List<Long> ids);

    /**
     * 根据ID批量删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("SELECT s.*, c.name as category_name FROM setmeal s LEFT OUTER JOIN category c ON s.category_id = c.id WHERE s.id = #{id}")
    SetmealVO selectById(Long id);
}
