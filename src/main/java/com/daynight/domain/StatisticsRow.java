package com.daynight.domain;

public class StatisticsRow {

    private String from;

    private String brand;

    /**
     * 款号
     */
    private String style;

    private String color;

    private int sizeLNum;

    private int sizeXLNum;

    private int size2XLNum;

    private int size3XLNum;

    private double price;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSizeLNum() {
        return sizeLNum;
    }

    public void setSizeLNum(int sizeLNum) {
        this.sizeLNum = sizeLNum;
    }

    public int getSizeXLNum() {
        return sizeXLNum;
    }

    public void setSizeXLNum(int sizeXLNum) {
        this.sizeXLNum = sizeXLNum;
    }

    public int getSize2XLNum() {
        return size2XLNum;
    }

    public void setSize2XLNum(int size2XLNum) {
        this.size2XLNum = size2XLNum;
    }

    public int getSize3XLNum() {
        return size3XLNum;
    }

    public void setSize3XLNum(int size3XLNum) {
        this.size3XLNum = size3XLNum;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String group() {
        return brand + "_" + style + "_" + color;
    }
}
