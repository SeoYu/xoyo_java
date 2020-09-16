package com.vicent.xoyo.constant;

/**
 * @author vincent
 * @description
 * @Date 2020/9/16 3:08 下午
 */
public enum MenPaiEnum {
    ALL(0,"ALL"),
    LINGXUEGE(1,"凌雪阁"),
    PENGLAI(2,"蓬莱"),
    BADAO(3,"霸刀"),
    CHANGGE(4,"长歌"),
    CANGYUN(5,"苍云"),
    GAIBANG(6,"丐帮"),
    MINGJIAO(7,"明教"),
    TANGMEN(8,"唐门"),
    WUDU(9,"五毒"),
    CANGJIAN(10,"藏剑"),
    TIANCE(11,"天策"),
    CHUNYANG(12,"纯阳"),
    SHAOLIN(13,"少林"),
    QIXIU(14,"七秀"),
    WANHUA(15,"万花"),
    DAXIA(16,"大侠");

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

    private MenPaiEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
