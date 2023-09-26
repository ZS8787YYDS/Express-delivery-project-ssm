package com.zisheng.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPagingResult implements Serializable {
    private List<Orders> records;
    private Long total;
}
