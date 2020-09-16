package com.vicent.xoyo.constant;

/**
 * @author vincent
 * @description
 * @Date 2020/9/16 3:08 下午
 */
public enum  PriceEnum {
    ALL(0,"ALL"),
    ONE(1,"0~1k"),
    TWO(2,"1k~3k"),
    THREE(3,"3k~1w"),
    FOUR(4,"1w~3w"),
    FIVE(5,"3w~");

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

    private PriceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
