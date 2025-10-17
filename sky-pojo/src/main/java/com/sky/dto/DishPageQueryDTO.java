package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DishPageQueryDTO implements Serializable {

    // 当前页码，用于分页查询，从1开始计数
    private int page;

    // 每页显示的记录数量，控制分页大小
    private int pageSize;

    // 菜品名称搜索关键字，支持模糊查询
    private String name;

    // 菜品分类ID，用于按分类筛选菜品
    private Integer categoryId;

    // 菜品状态筛选条件：0-禁用状态，1-启用状态
    private Integer status;


}
