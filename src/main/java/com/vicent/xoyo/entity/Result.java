package com.vicent.xoyo.entity;

import java.io.Serializable;

/**
 * @author vincent
 * @description
 * @Date 2020/8/3 6:05 下午
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -5876715952317380919L;

    private int code;
    private Object data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static Result success(Object o){
        return new Result(0,o,"成功");
    }

    public static Result success(){
        return new Result(0,null,"成功");
    }

    public static Result success(String msg,Object o){
        return new Result(0,o,msg);
    }

    public static Result success(String msg){
        return new Result(0,null,msg);
    }

    public static Result error(String msg,Object o){
        return new Result(-1,o,msg);
    }

    public static Result error(String msg){
        return new Result(-1,null,msg);
    }

    public static Result error(){
        return new Result(-1,null,"失败");
    }

    public static Result error(int code,String msg){
        return new Result(code,null,msg);
    }

    public static Result error(int code,String msg,Object o){
        return new Result(code,null,msg);
    }

}
