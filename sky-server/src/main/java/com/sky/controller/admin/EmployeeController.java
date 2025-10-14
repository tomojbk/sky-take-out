package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
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
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：" + employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        ;
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     *
     * @param status 状态
     * @param id     员工id
     * @return
     */
    /*
      @PostMapping("/status/{status}")
      主要功能
        HTTP方法映射：将 startOrStop 方法映射到 HTTP POST 请求
        URL路径定义：指定请求路径为 /status/{status}，其中 {status} 是路径变量
        路径变量说明
        {status} 是一个路径参数占位符
        实际请求时会被具体数值替换，例如：
        /status/1 （启用员工）
        /status/0 （禁用员工）
     */
    @PostMapping("/status/{status}")
    /*
    @ApiOperation("启用禁用员工账号")
    类型：Swagger 注解
        作用：
        为 API 接口提供文档说明
        在 Swagger UI 界面中显示接口的用途描述
        帮助开发者理解该接口的功能
        功能：在 API 文档中标识这个接口是用于「员工登录」操作，便于前后端开发人员理解和使用
        这两个注解共同作用，一个负责路由映射，一个负责接口文档说明，都是 RESTful API 开发中的重要组成部分。
     */
    @ApiOperation("启用禁用员工账号")
    /*
    @PathVariable
    基本概念
    类型：Spring MVC 注解
    用途：用于将 URL 路径中的变量值绑定到方法参数上
    工作原理
    提取 URL 路径中用 {} 包裹的变量部分
    将提取的值自动转换并赋给对应的方法参数
    支持类型自动转换（如 String 转 Integer）
     */
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工账号:{}{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

}


