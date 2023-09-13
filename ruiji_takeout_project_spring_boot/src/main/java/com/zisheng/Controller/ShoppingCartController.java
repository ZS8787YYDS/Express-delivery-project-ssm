package com.zisheng.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zisheng.MyUtils.ThreadUtils;
import com.zisheng.Pojo.Result;
import com.zisheng.Pojo.ShoppingCart;
import com.zisheng.Service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result insertDataToCart(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("接收到的数据为：{}",shoppingCart.toString());
        ShoppingCart shoppingCart1 = shoppingCartService.insertDataToCart(shoppingCart);
        return Result.success(shoppingCart1);
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping("/list")
    public  Result findCartData()
    {
        log.info("查询购物车里用户选中的所有数据");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, ThreadUtils.getThreadLocal());
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return Result.success(list);
    }

    /**
     * 修改购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result subCartData(@RequestBody ShoppingCart shoppingCart)
    {
        log.info("接收到的数据为：{}",shoppingCart);
        shoppingCartService.subCartData(shoppingCart);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result clear()
    {
        log.info("清空购物车所有数据");
        shoppingCartService.clear();
        return Result.success();
    }
}
