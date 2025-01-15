package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端店铺操作
 */
@RestController("UserShopController") // 指定bean名称 否则会和 com.sky.controller.admin.ShopController 冲突
@RequestMapping("/user/shop")
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

}
