package com.zisheng.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zisheng.Pojo.OrderPagingResult;
import com.zisheng.Pojo.Orders;
import com.zisheng.Pojo.Result;
import com.zisheng.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    /**
     * 账单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public Result selectPaging_02(Integer page, Integer pageSize, Integer number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime beginTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime)
    {
        log.info("接收到的信息为：page:{},pageSize:{},number:{},beginTime:{},endTIme:{}",page,pageSize,number,beginTime,endTime);
        OrderPagingResult orderPagingResult = orderService.selectPaging(page,pageSize,number,beginTime,endTime);
        return Result.success(orderPagingResult);
    }

    /**
     * 修改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public Result finish(@RequestBody Orders orders)
    {
        log.info("接收到的信息为：{}",orders);
        orderService.updateById(orders);
        return Result.success();
    }
}
