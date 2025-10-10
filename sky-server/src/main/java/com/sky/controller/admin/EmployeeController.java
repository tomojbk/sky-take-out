package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.pojo.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.admin.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
// 员工管理控制器类，提供员工相关的RESTful API接口
@RestController
// 设置该控制器的基础URL路径为/admin/employee
@RequestMapping("/admin/employee")
// Lombok注解，自动生成日志记录器Logger对象
@Slf4j
// Swagger注解，定义API文档的标签名称
@Api(tags = "员工管理")
public class EmployeeController {

    // 自动注入EmployeeService实例，用于处理员工相关的业务逻辑
    @Autowired
    private EmployeeService employeeService;

    // 自动注入JwtProperties实例，用于获取JWT相关的配置属性
    @Autowired
    private JwtProperties jwtProperties;

    // 员工登录接口，处理POST请求到/login路径
    @PostMapping("/login")
    // Swagger注解，描述该API操作的用途
    @ApiOperation("员工登录")
    // @RequestBody表示从请求体中解析参数
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        // 记录员工登录日志信息，输出登录参数详情
        log.info("员工登录：{}", employeeLoginDTO);

        // 调用员工服务进行登录验证，返回验证通过的员工信息
        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成jwt令牌，创建载荷映射表
        Map<String, Object> claims = new HashMap<>();

        // 将员工ID放入JWT载荷中，用于后续身份识别
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());

        // 创建JWT令牌，使用管理员密钥、有效期和载荷信息
        String token = JwtUtil.createJWT(
                // 获取JWT签名密钥
                jwtProperties.getAdminSecretKey(),
                // 获取JWT过期时间
                jwtProperties.getAdminTtl(),
                // 设置JWT载荷数据
                claims);

        // 构建员工登录返回对象，包含员工基本信息和访问令牌
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                // 设置员工唯一标识
                .id(employee.getId())
                // 设置员工用户名
                .userName(employee.getUsername())
                // 设置员工真实姓名
                .name(employee.getName())
                // 设置JWT访问令牌
                .token(token)
                // 使用Builder模式构建对象
                .build();

        // 返回登录成功的响应结果，封装员工登录信息
        return Result.success(employeeLoginVO);
    }

    // 员工退出登录接口，处理POST请求到/logout路径
    @PostMapping("/logout")
    // Swagger注解，描述该API操作的用途
    @ApiOperation("员工退出")
    // 退出登录接口，无需参数
    public Result<String> logout() {
        // 返回退出成功的响应结果，消息体为空字符串
        return Result.success();
    }
    @PostMapping
    @ApiOperation("新增员工")
    //前端返回的数据为 json 数据 必须添加 @RequestBody使得数据能被解析
    //后端 @RequestBody 缺失
    //不能让 Spring 用 JSON 反序列化成 EmployeeDTO
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

}


