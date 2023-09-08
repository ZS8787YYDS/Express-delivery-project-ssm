package com.zisheng.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;
    private String meg;
    private Object data;
    public static final Result success()
    {
        return new Result(1,"success",null);
    }
    public static final Result success(Object data)
    {
        return new Result(1,"success",data);
    }
    public static final Result error()
    {
        return new Result(0,"failure",null);
    }
    public static final Result error(Object data)
    {
        return new Result(0,"failure",data);
    }
}
