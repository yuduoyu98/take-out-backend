package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐管理
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void add(SetmealDTO setmealDTO) {
        // 保存套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        // 保存套餐菜品关系
        Long setmealId = setmeal.getId(); // 回填ID
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.<SetmealVO>startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * - 套餐启售中无法删除
     * @param ids
     */
    @Override
    @Transactional
    public void batchDelete(List<Long> ids) {
        // 套餐启售中无法删除
        List<Long> odSaleSetmealIds = setmealMapper.onSaleIds(ids);
        if (!odSaleSetmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        // 删除套餐
        setmealMapper.deleteByIds(ids);
        // 删除套餐关联菜品
        setmealDishMapper.deleteBySetmealIds(ids);
    }
}
