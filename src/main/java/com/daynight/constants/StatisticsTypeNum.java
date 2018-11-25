package com.daynight.constants;

public enum  StatisticsTypeNum {
    SOURCE("终端"),
    RATE("倍率"),
    STOCK_UP("备货");

    public final String type;

    StatisticsTypeNum(String type) {
        this.type = type;
    }
}
