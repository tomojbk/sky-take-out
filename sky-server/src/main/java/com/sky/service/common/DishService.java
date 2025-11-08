package com.sky.service.common;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch( List<Integer> ids);

    /**
     * 根据id查询
     * @param dishId
     * @return
     */
    DishVO findById(Integer dishId);

    /**
     * 修改菜品
     * @param dishVO
     */
    void update(DishVO dishVO);

}
