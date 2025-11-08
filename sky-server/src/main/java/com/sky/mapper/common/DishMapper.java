package com.sky.mapper.common;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.pojo.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询
     * @param dishPageQueryDTO
     */
    Page<Dish> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    Integer selectCountByIdsAndStatus(List<Integer> ids, Integer status);

    void deleteByIds( List<Integer> ids);

    DishVO findById(Integer dishId);

    @AutoFill(OperationType.UPDATE)
    void update(DishVO dishVO);


    void updateById(Integer status, Long id);
}
