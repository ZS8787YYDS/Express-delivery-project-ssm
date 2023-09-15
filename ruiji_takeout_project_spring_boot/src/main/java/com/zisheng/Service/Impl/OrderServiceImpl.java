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
        // 获取当前用户的id，设置当前订单所属用户
        Long userId = ThreadUtils.getThreadLocal();
        orders.setUserId(userId);
        // 设置订单创建时间
        orders.setOrderTime(LocalDateTime.now());
        // 设置订单提交时间
        orders.setCheckoutTime(LocalDateTime.now());
        // 创建QueryWrapper对象，设置泛型为AddressBook，操作这个实体类所对应的表
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        // 设置查询条件，user_id = userId and is_default = 1
        queryWrapper.lambda().eq(userId != null,AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,1);
        // 调用addressBookMapper对象的selectOne方法，执行查询操作，将查询的结果封装在AddressBook对象中返回
        AddressBook addressBook = addressBookMapper.selectOne(queryWrapper);
        // 设置订单所属用户的手机号
        orders.setPhone(addressBook.getPhone());
        // 设置订单的收件人
        orders.setConsignee(addressBook.getConsignee());
        // 设置接收订单的用户地址
        orders.setAddress(addressBook.getDetail());
        // 创建BigDecimal对象，初始化为0.0，表示订单的总金额
        BigDecimal total = new BigDecimal("0.0");
        // 创建LambdaQueryWrapper对象，设置泛型为ShoppingCart类型，表示操作这个实体类对应的表
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        // 查询当前用户的购物车详情，将购物车的数据封装成对象，放在集合当中返回
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(lambdaQueryWrapper);
        for(ShoppingCart shoppingCart : shoppingCarts)
        {
            if(shoppingCart.getNumber() == 1)
            {
                // 当前掏槽或者菜品的个数为1
                total = total.add(shoppingCart.getAmount());
            }
            else
            {
                total = total.add(shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber())));
            }
        }
        // 设置订单的总金额
        orders.setAmount(total);
        // 设置状态为待派送
        orders.setStatus(2);
        // 将当前订单数据插入到Orders表中
        orderMapper.insert(orders);
        // 插入完成之后将清空该用户购物车信息
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(userId != null,ShoppingCart::getUserId,userId);
        shoppingCartMapper.delete(lambdaQueryWrapper);
        // 插入Order_detail表
        // 遍历当前用户的购物车数据，
        for(ShoppingCart shoppingCart : shoppingCarts)
        {
            // 创建一个OrderDetail对象
            OrderDetail orderDetail = new OrderDetail();
            // 设置当前订单数据所属的订单id
            orderDetail.setOrderId(orders.getId());
            // 设置订单数据的名称，可能是套餐，也可能是菜品
            orderDetail.setName(shoppingCart.getName());
            // 设置订单数据的图片
            orderDetail.setImage(shoppingCart.getImage());
            // 判断当前订单数据是否是菜品
            if(shoppingCart.getDishId() != null)
            {
                // 是菜品
                orderDetail.setDishId(shoppingCart.getDishId());
            }
            else orderDetail.setSetmealId(shoppingCart.getSetmealId());
            // 设置当前菜品或者套餐的份数
            orderDetail.setNumber(shoppingCart.getNumber());
            // 设置当前菜品或者套餐的价格
            orderDetail.setAmount(shoppingCart.getAmount());
            // 将这一订单数据插入到订单详情表中
            orderDetailMapper.insert(orderDetail);
        }
    }

    /**
     * 用户订单分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public OrderPagingResult selectPaging(Integer page, Integer pageSize) {
        IPage<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId,ThreadUtils.getThreadLocal());
        orderMapper.selectPage(page1,lambdaQueryWrapper);
        return new OrderPagingResult(page1.getRecords(),page1.getTotal());
    }

    /**
     * 订单分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public OrderPagingResult selectPaging(Integer page, Integer pageSize,Integer number,LocalDateTime beginTime,LocalDateTime endTime) {
        IPage<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(number != null,Orders::getNumber,number).between(beginTime != null && endTime != null,Orders::getOrderTime,beginTime,endTime);
        orderMapper.selectPage(page1,lambdaQueryWrapper);

        return new OrderPagingResult(page1.getRecords(),page1.getTotal());
    }
}
