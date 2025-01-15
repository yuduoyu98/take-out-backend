package com.sky.bo;

import lombok.Data;

/**
 * 菜品删除时的业务对象
 */
@Data
public class DishSetmealBO {

    private Long dishId;

    private Long setmealId;

    private Integer status;
}
