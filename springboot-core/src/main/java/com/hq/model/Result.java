package com.hq.model;


import lombok.Getter;
import lombok.Setter;

/**
 * @description: 返回前端对象
 * @create: 2019-05-13 17:26
 **/
@Getter
@Setter
public class Result<T> {

    //相应数据
    T payload;

    // 是否成功
    Boolean success;

    //返回消息
    String msg;

    //返回记录条数
    Integer total;

    //状态码
    int code = -1;

    //服务器相应时间
    long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public Result(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public Result(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public Result(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public Result(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public Result(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }
    public static Result success() {
        return new Result(true);
    }

    public static <T> Result success(T payload) {
        return new Result(true, payload);
    }

    public static <T> Result success(int code) {
        return new Result(true, null, code);
    }

    public static <T> Result success(T payload, int code) {
        return new Result(true, payload, code);
    }

    public static Result fail() {
        return new Result(false);
    }

    public static Result fail(String msg) {
        return new Result(false, msg);
    }

    public static Result fail(int code) {
        return new Result(false, null, code);
    }

    public static Result fail(int code, String msg) {
        return new Result(false, msg, code);
    }
}
