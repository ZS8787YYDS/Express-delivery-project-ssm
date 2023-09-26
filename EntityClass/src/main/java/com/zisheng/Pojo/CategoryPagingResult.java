package com.zisheng.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPagingResult implements Serializable {
    private List<Category> records;
    private Long total;
}
