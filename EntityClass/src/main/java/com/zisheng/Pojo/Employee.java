package com.zisheng.Pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "员工实体")
public class Employee implements Serializable{
    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "用户账号")
    private String username;
    @ApiModelProperty(value = "用户姓名")
    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;
    // 在插入数据时会自动填充
    @TableField(fill = FieldFill.INSERT)

    private LocalDateTime createTime;
    // 在插入或者更新的时候会自动进行填充
    @TableField(fill = FieldFill.INSERT_UPDATE)

    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
