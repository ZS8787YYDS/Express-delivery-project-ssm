package com.zisheng.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zisheng.Pojo.OrderPagingResult;
import com.zisheng.Pojo.Orders;

import java.time.LocalDateTime;

public interface OrderService extends IService<Orders> {
    void submitCart(Orders orders);
    OrderPagingResult selectPaging(Integer page, Integer pageSize);

    OrderPagingResult selectPaging(Integer page, Integer pageSize, Integer number, LocalDateTime beginTime,LocalDateTime endTime);
}
