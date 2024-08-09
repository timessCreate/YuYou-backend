package com.model.enums;



/**
 * @author timess
 * @className: TeamStatusEnum
 * @Version: 1.0
 * @description:
 */

public enum TeamStatusEnum {

    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");

    private   int value;
    private  String text;

    public static TeamStatusEnum getEnumValue(Integer value){
        if(value == null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if(teamStatusEnum.value == value){
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setText(String text) {
        this.text = text;
    }
}