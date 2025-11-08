package com.sky.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description SetmealDishMapper
 * @Author tomLe
 * @Date 2025-10-26
 */
@Mapper
public interface SetmealDishMapper {
    Integer selectCountByDishIds( List<Integer> ids);
}
