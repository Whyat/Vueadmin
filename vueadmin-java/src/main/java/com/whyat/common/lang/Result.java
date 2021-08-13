package com.whyat.common.lang;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 16:21
 */
@Data
@AllArgsConstructor
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        return new Result(2000,"操作成功！", data);
    }

    public static Result success(int code, String msg, Object data) {
        return new Result(code, msg, 200);
    }

    public static Result fail(Object data) {
        return new Result(4000, "操作失败", data);
    }

    public static Result fail(int code, String msg, Object data) {
        return new Result(code, msg, 400);
    }

}
