package com.zisheng.Dto;

import com.zisheng.Pojo.Dish;
import com.zisheng.Pojo.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于扩展Dish类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
    private String categoryName;
    private Integer copies;
}
