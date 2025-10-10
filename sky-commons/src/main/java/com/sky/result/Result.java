package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg=""; //错误信息
    private T data; //数据


// 创建一个成功结果对象，不包含具体数据
public static <T> Result<T> success() {
    // 创建Result对象实例
    Result<T> result = new Result<T>();
    // 设置返回码为1，表示成功
    result.code = 1;
    // 返回成功结果对象
    return result;
}

// 创建一个成功结果对象，包含具体数据
public static <T> Result<T> success(T object) {
    // 创建Result对象实例
    Result<T> result = new Result<T>();
    // 设置返回的数据对象
    result.data = object;
    // 设置返回码为1，表示成功
    result.code = 1;
    // 返回成功结果对象
    return result;
}


    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
