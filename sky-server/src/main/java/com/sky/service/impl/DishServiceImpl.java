package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.bo.DishSetmealBO;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜品管理
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 保存新增菜品以及口味消息
     *
     * @param dishDTO
     */
    @Override
    @Transactional // 保存到两张表 需要保证一致性
    public void addDishWithFlavor(DishDTO dishDTO) {
        // 保存菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE); // 默认停售 需手动启售
        dishMapper.insert(dish);
        Long dishID = dish.getId(); // 通过MyBatis的主键回填功能获取新增的菜品ID

        // 保存口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors.isEmpty()) {
            return;
        }
        flavors.forEach(flavor -> flavor.setDishId(dishID));
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.<DishVO>startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * 业务规则：
     * - 在售状态下，不可删除菜品 (都不允许删除)
     * - 有套餐关联的不能删除 (都不允许删除)
     * - 删除时 口味数据一并删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteDishes(List<Long> ids) {
        // 查询出在售的或者有套餐绑定的菜品信息
        List<DishSetmealBO> dishNotDel = setmealDishMapper.selectOnSaleInSetmealDishes(ids);
        if (!dishNotDel.isEmpty()) {
            for (DishSetmealBO dishSetmealBO : dishNotDel) {
                if (dishSetmealBO.getStatus() == 1) {
                    throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                }
                if (dishSetmealBO.getSetmealId() != null) {
                    throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                }
            }
        }

        // 删除菜品
        dishMapper.deleteDishesByIds(ids);
        dishFlavorMapper.deleteFlavorsByDishIds(ids);
    }

    /**
     * 菜品起售、停售
     *
     * @param id
     * @param status
     */
    @Override
    public void updateDishStatus(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getDishById(Long id) {
        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.selectById(id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 更新菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        // 更新菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 更新口味
        // 1.删除原关联口味
        dishFlavorMapper.deleteFlavorsByDishIds(Collections.singletonList(dish.getId()));
        // 2.重新写入关联口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (!flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dish.getId())); // dishId允许不传
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 根据分类ID查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getDishesByCategory(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).build();
        return dishMapper.list(dish);
    }

    /**
     * 根据分类ID查询菜品以及菜品口味
     * @param categoryId
     * @param status
     * @return
     */
    @Override
    public List<DishVO> listWithFlavor(Long categoryId, Integer status) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(status)
                .build();
        List<Dish> dishes = dishMapper.list(dish);
        List<DishVO> dishVOs = new ArrayList<>();
        dishes.forEach(d -> {
            List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(d.getId());
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            dishVO.setFlavors(dishFlavors);
            dishVOs.add(dishVO);
        });
        return dishVOs;
    }

}
