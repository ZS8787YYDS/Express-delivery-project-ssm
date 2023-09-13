package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart insertDataToCart(ShoppingCart shoppingCart);

    void subCartData(ShoppingCart shoppingCart);

    void clear();
}
