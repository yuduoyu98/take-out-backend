package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端店铺操作
 */
@RestController("AdminShopController") // 指定bean名称 否则会和 com.sky.controller.user.ShopController 冲突
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺操作")
public class ShopController {

    private static final String REDIS_KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result getShopStatus() {
        log.info("获取营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(REDIS_KEY);
        return Result.success(status);
    }

    /**
     * 设置营业状态
     * @param status 1-营业中 0-打烊中
     * @return
     */
    @PutMapping("{status}")
    @ApiOperation("设置营业状态")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置营业状态: {}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(REDIS_KEY, status);
        return Result.success();
    }


}
