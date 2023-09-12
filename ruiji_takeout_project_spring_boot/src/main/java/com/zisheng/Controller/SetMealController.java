package com.zisheng.Controller;

import com.zisheng.Dto.SetMealDto;
import com.zisheng.Pojo.Result;
import com.zisheng.Pojo.SetMealPagingResult;
import com.zisheng.Pojo.Setmeal;
import com.zisheng.Service.SetMealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    private static final Logger log = LoggerFactory.getLogger(SetMealController.class);

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result setMealPaging(Integer page,Integer pageSize,String name)
    {
        log.info("接收到的数据为：page:{}, pageSize:{}",page,pageSize);
        SetMealPagingResult setMealPagingResult = setMealService.setMealPaging(page,pageSize,name);
        return Result.success(setMealPagingResult);
    }

    /**
     * 插入套餐
     * @param setMealDto
     * @return
     */
    @PostMapping
    public Result insertSetmeal(@RequestBody SetMealDto setMealDto)
    {
        log.info("接收到的数据为：{}",setMealDto);
        setMealService.insertSetmeal(setMealDto);
        return Result.success();
    }

    /**
     * 查询套餐功能
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result searchSetmeal(@PathVariable Long id)
    {
        log.info("接收到的数据为:{}",id);
        SetMealDto setMealDto = setMealService.searchSetmeal(id);
        return  Result.success(setMealDto);
    }

    /**
     * 修改套餐功能
     * @param setMealDto
     * @return
     */
    @PutMapping
    public Result updateSetmeal(@RequestBody SetMealDto setMealDto)
    {
        log.info("接收到的数据为：{}",setMealDto);
        setMealService.updateSetmeal(setMealDto);
        return Result.success();
    }

    /**
     * 批量起售与停售功能
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result startSell(@PathVariable Integer status, @RequestParam List<Long> ids)
    {
        log.info("接收到的数据为：{}",ids);
        setMealService.startSell(status,ids);
        return Result.success();
    }

    @DeleteMapping
    public Result delteSetmeals(@RequestParam List<Long> ids)
    {
        log.info("接收到的信息为：{}",ids);
        setMealService.deleteSetmeals(ids);
        return Result.success();
    }
}
