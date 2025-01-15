package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品");
        dishService.addDishWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids 菜品id，之间用逗号分隔
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDishes(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteDishes(ids);
        return Result.success();
    }

    /**
     * 菜品起售、停售
     * @param id 菜品ID
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    public Result updateDishStatus(@RequestParam Long id, @PathVariable Integer status) {
        log.info("菜品起售、停售：{}", id);
        dishService.updateDishStatus(id, status);
        return Result.success();
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 根据id查询菜品 (回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        return Result.success(dishService.getDishById(id));
    }
}
