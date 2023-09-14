package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 插入购物车数据
     * @param shoppingCart
     * @return
     */
    ShoppingCart insertDataToCart(ShoppingCart shoppingCart);

    /**
     * 削减购物车数据
     * @param shoppingCart
     */
    void subCartData(ShoppingCart shoppingCart);

    /**
     * 清空购物车数据
     */
    void clear();
}
