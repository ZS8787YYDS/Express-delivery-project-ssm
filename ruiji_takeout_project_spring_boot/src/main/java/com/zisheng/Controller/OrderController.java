package com.zisheng.Controller;

import com.zisheng.Pojo.OrderPagingResult;
import com.zisheng.Pojo.Orders;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单功能
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result submitCart(@RequestBody Orders orders)
    {
        log.info("接收到的数据为：{}",orders);
        orderService.submitCart(orders);
        return Result.success();
    }

    /**
     * 订单分页擦查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result selectPaging(Integer page,Integer pageSize)
    {
        log.info("接收到的信息为：page:{},pageSize:{}",page,pageSize);
        OrderPagingResult orderPagingResult = orderService.selectPaging(page,pageSize);
        return Result.success(orderPagingResult);
    }
}
