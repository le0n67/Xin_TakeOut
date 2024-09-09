package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Date：2024/9/9  20:53
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */

@RestController("userShopController")
@RequestMapping("user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    public static final String KEY = "SHOP_STATUS";

    @Autowired
    RedisTemplate redisTemplate;
    @ApiOperation("获取店铺状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态为：{}", status == 1 ? "开启" : "关闭");
        return Result.success(status);
    }
}
