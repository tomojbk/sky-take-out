package com.sky.mapper.common;

import com.sky.pojo.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description DishFlavorMapper
 * @Author tomLe
 * @Date 2025-10-17
 */
@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishIds( List<Integer>  ids);

    List<DishFlavor> findTasteByDishId(Integer dishId);
}
