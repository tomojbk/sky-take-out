package com.sky.controller.common;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.common.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 菜品管理
 * @Author tomLe
 * @Date 2025-10-17
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    //自动装配 Bean
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    //@RequestBody
    // 数据绑定：将客户端发送的 JSON、XML 等格式的请求体数据自动转换为 Java 对象
    //反序列化：配合 Jackson 等序列化框架，自动完成数据格式转换
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        //Flavor 情味，风味；香料；滋味
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询，参数为{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    //@RequestParam绑定请求参数
    public Result delete(@RequestParam List<Integer> ids){
        log.info("菜品批量删除{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据dishId查询菜品
     * @param dishId
     * @return
     */
    @GetMapping("/{dishId}")
    @ApiOperation("根据id查询菜品")
    public Result findById(@PathVariable Integer dishId){
        log.info("根据id查询菜品 dishId={}",dishId);
        DishVO dish = dishService.findById(dishId);
        return Result.success(dish);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishVO dishVO){
        log.info("修改菜品 dishVO={}",dishVO);
        dishService.update(dishVO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改菜品状态")
    public Result updateById(@PathVariable Integer status, Long id){
        log.info("修改菜品状态 status={}",status);
        dishService.updateById(status,id);
        return Result.success();
    }
}
