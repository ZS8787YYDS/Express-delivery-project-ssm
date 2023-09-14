package com.zisheng.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zisheng.Mapper.Address_bookMapper;
import com.zisheng.Mapper.OrderDetailMapper;
import com.zisheng.Mapper.OrderMapper;
import com.zisheng.Mapper.ShoppingCartMapper;
import com.zisheng.MyUtils.ThreadUtils;
import com.zisheng.Pojo.*;
import com.zisheng.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders>  implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private Address_bookMapper addressBookMapper;
    /**
     * 用户下单功能
     * @param orders
     */
    @Override
    public void submitCart(Orders orders) {
        // 插入Orders表
        Long userId = ThreadUtils.getThreadLocal();
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(userId != null,AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());

        BigDecimal total = new BigDecimal("0.0");
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(lambdaQueryWrapper);
        for(ShoppingCart shoppingCart : shoppingCarts)
        {
            if(shoppingCart.getNumber() == 1)
            {
                total = total.add(shoppingCart.getAmount());
            }
            else
            {
                total = total.add(shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber())));
            }
        }
        orders.setAmount(total);
        orderMapper.insert(orders);
        // 插入完成之后将清空该用户购物车信息
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(userId != null,ShoppingCart::getUserId,userId);
        shoppingCartMapper.delete(lambdaQueryWrapper);
        // 插入Order_detail表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orders.getId());
        for(ShoppingCart shoppingCart : shoppingCarts)
        {
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            if(shoppingCart.getDishId() != null)
            {
                orderDetail.setDishId(shoppingCart.getDishId());
            }
            else orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetailMapper.insert(orderDetail);
        }
    }

    /**
     * 订单分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public OrderPagingResult selectPaging(Integer page, Integer pageSize) {
        IPage<Orders> page1 = new Page<>(page,pageSize);
        orderMapper.selectPage(page1,null);

        return new OrderPagingResult(page1.getRecords(),page1.getTotal());
    }
}
