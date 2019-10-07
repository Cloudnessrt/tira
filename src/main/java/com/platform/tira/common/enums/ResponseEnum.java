package com.platform.tira.common.enums;

public enum ResponseEnum {
    ok(200),error(500);

    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    private ResponseEnum(int key){
        this.key=key;
    }

}
