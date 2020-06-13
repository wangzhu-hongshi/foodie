package com.imooc.enums;

/**
 * @Desc: 是否 枚举
 */
public enum CategoryEnum {
    Root(1, "一级"),
    Tow(2, "二级"),
    Three(3, "三级");

    public final Integer type;
    public final String value;

    CategoryEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
