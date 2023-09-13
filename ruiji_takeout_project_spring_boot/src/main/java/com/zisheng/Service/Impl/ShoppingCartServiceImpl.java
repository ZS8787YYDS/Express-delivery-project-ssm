package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zisheng.Mapper.ShoppingCartMapper;
import com.zisheng.MyUtils.ThreadUtils;
import com.zisheng.Pojo.ShoppingCart;
import com.zisheng.Service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Override
    public ShoppingCart insertDataToCart(ShoppingCart shoppingCart) {
        // 设置当前购物车所属用户
        shoppingCart.setUserId(ThreadUtils.getThreadLocal());
        // 获取菜品id，查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        // 获取套餐id
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,ThreadUtils.getThreadLocal());
        if(dishId != null)
        {
            // 为菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else
        {
            // 为套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart shoppingCart1 = shoppingCartMapper.selectOne(lambdaQueryWrapper);
        if(shoppingCart1 != null)
        {
            // 当前菜品或者套餐在购物车中存在,在原有菜品或者套餐的基础上直接修改字段让其加1
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartMapper.updateById(shoppingCart1);
        }
        else {
            shoppingCart.setCreateTime(LocalDateTime.now());

            shoppingCartMapper.insert(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return shoppingCart1;
    }

    /**
     * 删除购物车中的数据
     * @param shoppingCart
     */
    @Override
    public void subCartData(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dishId != null)
        {
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        else
        {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart shoppingCart1 = shoppingCartMapper.selectOne(lambdaQueryWrapper);
        if(shoppingCart1.getNumber() == 1)
        {
            shoppingCartMapper.delete(lambdaQueryWrapper);
        }
        else
        {
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartMapper.updateById(shoppingCart1);
        }
    }

    /**
     * 清空购物车
     */
    @Override
    public void clear() {
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,ThreadUtils.getThreadLocal());
        shoppingCartMapper.delete(lambdaQueryWrapper);
    }
}
