package com.vicent.xoyo.constant;

/**
 * @author vincent
 * @description
 * @Date 2020/9/16 3:08 下午
 */
public enum SexEnum {
    ONE(1,"成男"),
    TWO(2,"成女"),
    THREE(3,"正太"),
    FOUR(4,"萝莉");

    int code;
    String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
