package com.zisheng.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
    private String avatar;
    private Integer status;
}
