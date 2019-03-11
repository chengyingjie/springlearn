package com.spring.core.enums;

public enum ScopeTypeEnum {

    SINGLETON(1, "singleton", "单例"),
    PROTOTYPE(2, "prototype", "原型");

    private Integer key;
    private String value;
    private String desc;

    ScopeTypeEnum(Integer key, String value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
