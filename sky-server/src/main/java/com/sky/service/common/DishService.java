package com.sky.service.common;

import com.sky.dto.DishDTO;

/**
 * @Description 菜品管理
 * @Author tomLe
 * @Date 2025-10-17
 */
public interface DishService {

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
