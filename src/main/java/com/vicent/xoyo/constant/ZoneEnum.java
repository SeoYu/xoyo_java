package com.vicent.xoyo.constant;

public enum ZoneEnum {
    ONE("z01","gate0126","蝶恋花"),
    TWO("z01","gate0115","龙争虎斗"),
    THREE("z01","gate0101","长安城"),
    FOUR("z05","gate0519","幽月轮"),
    FIVE("z05","gate0515","斗转星移"),
    SIX("z05","gate0524","剑胆琴心"),
    SEVEN("z05","gate0514","乾坤一掷"),
    EIGHT("z05","gate0505","唯我独尊"),
    NINE("z05","gate0502","梦江南"),
    TEN("z08","gate0807","绝代天骄"),
    ELEVEN("z21","gate2107","天鹅坪"),
    TWELVE("z21","gate2106","破阵子"),
    THIRTEEN("z22","gate2204","飞龙在天"),
    FOURTEEN("z24","gate2402","青梅煮酒"),
    FIFTEEN("z24","gate2407","凌雪藏锋"),
    SIXTEEN("z24","gate2409","风月同天"),
    SEVENTEEN("z24","gate2410","江湖故人");

    String zone;
    String server;
    String desc;

    ZoneEnum(String zone, String server, String desc) {
        this.zone = zone;
        this.server = server;
        this.desc = desc;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
