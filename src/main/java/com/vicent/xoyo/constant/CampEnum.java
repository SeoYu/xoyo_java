package com.vicent.xoyo.constant;

/**
 * @author vincent
 * @description
 * @Date 2020/9/16 3:08 下午
 */
public enum CampEnum {
    ONE(1,"浩气盟"),
    TWO(2,"恶人谷"),
    THREE(3,"中立");

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

    private CampEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
