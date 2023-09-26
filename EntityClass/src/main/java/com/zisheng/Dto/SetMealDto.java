package com.zisheng.Dto;

import com.zisheng.Pojo.Setmeal;
import com.zisheng.Pojo.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetMealDto extends Setmeal implements Serializable {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
