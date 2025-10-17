package com.sky.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.enumeration.OperationType;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.EmployeeNameExistException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.admin.EmployeeMapper;
import com.sky.pojo.Employee;
import com.sky.result.PageResult;
import com.sky.service.admin.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl  implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();//

        //1、根据用户名查询数据库中的数据  select * from employee where username = #{username}
        Employee employee = employeeMapper.getByUsername(username); //

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
                // 对密码进行MD5加密处理，将明文密码转换为32位十六进制的MD5摘要值
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */


    @Override
    public void save(EmployeeDTO employeeDTO) {

        //判断用户名是否存在
        Employee dbEmployee = employeeMapper.getByUsername(employeeDTO.getUsername());
        if(dbEmployee!=null){
            throw new EmployeeNameExistException(dbEmployee.getName()+"已存在");
        }

        Employee employee = new Employee();
        //对象属性拷贝

        BeanUtils.copyProperties(employeeDTO,employee);
        //设置账号的状态
        employee.setStatus(StatusConstant.ENABLE);


        //设置默认密码123456
        //对密码进行MD5加密处理，将明文密码转换为32位十六进制的MD5摘要值
        //md5DigestAsHex（） 方法 可以将字符串进行MD5加密处理，并返回一个十六进制的字符串。
        //getBytes（） 方法 可以将字符串转换为字节数组。
        employee.setPassword(DigestUtils
                .md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

//        //设置当前记录的创建时间和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //设置当前记录创建人id和修改人id
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        //插入数据
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        /*
        PageHelper
        在执行具体查询操作之前，
        通过调用 PageHelper.startPage(int pageNum, int pageSize) 来设置当前页数和页面大小
         */
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        /*
        Page
        Page<T> 是 ArrayList<T> 的子类，因此它既是一个列表，也包含了分页相关信息
        它继承自 com.github.pagehelper.PageInfo 的部分特性，用来保存分页查询后的结果集
        封装分页查询的结果数据
        提供分页相关的信息，如总记录数、总页数、当前页码等
        与 PageHelper.startPage() 配合使用实现物理分页
        pageNum: 当前页码
        pageSize: 每页显示数量
        total: 总记录数
        pages: 总页数
        list: 当前页数据列表
         */
        Page<Employee> page =employeeMapper.pageQuery(employeePageQueryDTO);
        /*
        getTotal() 方法
        作用：获取分页查询的总记录数
        返回类型：long 或 int
        使用场景：通常在 PageInfo 对象中调用，用于获取数据库中符合条件的总记录数
        getResult() 方法
        作用：获取分页查询的结果列表
        返回类型：List<T>，其中 T 是具体的实体类型
        使用场景：获取当前页的数据列表
         */
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        employeeMapper.startOrStop(status,id);
    }

    /**
     *根据id查询员工信息
     * @param id
     * @return
     */

    @Override
    public Employee getById(Long id) {
       Employee employee = employeeMapper.getById(id);
       employee.setPassword("*****");
       return employee;
    }

    /**
     * 修改员工
     * @param employee
     */
    @Override
    public void update(Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
//        //设置修改人id
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

}
