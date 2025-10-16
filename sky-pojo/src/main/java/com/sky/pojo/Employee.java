package com.sky.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户唯一标识ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 用户姓名 */
    private String name;

    /** 用户密码 */
    private String password;

    /** 用户手机号 */
    private String phone;

    /** 用户性别 */
    private String sex;

    /** 用户身份证号 */
    private String idNumber;

    /** 用户状态：0-禁用 1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 创建人ID */
    private Long createUser;

    /** 更新人ID */
    private Long updateUser;



}
