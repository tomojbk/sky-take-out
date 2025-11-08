package com.sky.service.common.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.common.DishFlavorMapper;
import com.sky.mapper.common.DishMapper;
import com.sky.mapper.common.SetmealDishMapper;
import com.sky.pojo.Dish;
import com.sky.pojo.DishFlavor;
import com.sky.result.PageResult;
import com.sky.service.common.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @Description DishServiceImpl
 * @Author tomLe
 * @Date 2025-10-17
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    @Override
    /*
    @Transactional
    自动开启事务：在方法执行前自动开启数据库事务
    自动提交/回滚：方法正常执行完毕后自动提交事务，出现异常时自动回滚
    2. ACID 特性保障
    原子性（Atomicity）：整个方法要么全部执行成功，要么全部回滚
    一致性（Consistency）：保证数据库从一个一致状态转换到另一个一致状态
    隔离性（Isolation）：控制并发访问时的数据隔离级别
    持久性（Durability）：事务提交后数据永久保存
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        /*
        BeanUtils.copyProperties（）
        1. 功能说明
        属性复制：将 dishDTO 对象中的属性值复制到 dish 对象中
        自动映射：自动匹配两个对象中同名且类型兼容的属性
        2. 工作原理
        通过反射机制获取源对象和目标对象的属性
        将源对象中非空属性值赋值给目标对象的对应属性
        只复制名称相同且类型兼容的属性
         */
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);//后绪步骤实现
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<Dish> results = page.getResult();
        return new PageResult(total,results);
    }

    /**
     * 菜品批量删除
     * @param ids
     */
    @Override
    public void deleteBatch( List<Integer>  ids) {
        Integer count = dishMapper.selectCountByIdsAndStatus(ids, StatusConstant.ENABLE);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //查询删除的菜品是否有被套餐关联的
        Integer count2= setmealDishMapper.selectCountByDishIds(ids);
        if(count2>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        dishFlavorMapper.deleteByDishIds(ids);
        //删除菜品信息
        dishMapper.deleteByIds(ids);
    }

    /**
     * 根据id查询
     * @param dishId
     * @return
     */
    @Override
    public DishVO findById(Integer dishId) {

        DishVO dish = dishMapper.findById(dishId);

        List<DishFlavor> dishFlavorList = dishFlavorMapper.findTasteByDishId(dishId);
        dish.setFlavors(dishFlavorList);
        return dish;
    }

    @Override
    public void update(DishVO dishVO) {
        dishMapper.update(dishVO);
        dishFlavorMapper.deleteByDishIds((Arrays.asList(Integer.valueOf(dishVO.getId().toString()))));
        List<DishFlavor> flavors = dishVO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishVO.getId());
        }
        dishFlavorMapper.insertBatch(dishVO.getFlavors());

    }


}
