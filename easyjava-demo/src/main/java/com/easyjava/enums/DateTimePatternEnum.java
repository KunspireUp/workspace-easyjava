package com.easyjava.enums;

/**
 * @Description: 时间枚举类
 * @Author: KunSpireUp
 */

public enum DateTimePatternEnum {
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"), YYYY_MM_DD("yyyy-MM-dd");

    private String pattern;

    DateTimePatternEnum(String pattern){
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }


}
