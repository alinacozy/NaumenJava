package ru.alinacozy.NauJava.entity;

/**
 * Класс-сущность конкретной нитки из каталога
 */
public class Floss {
    private Long id;
    private String brand;
    private String colorNumber;
    private String colorName;
    private String colorGroup;
    private int red;
    private int green;
    private int blue;

    public Floss(Long id, String brand, String colorNumber, String colorName, String colorGroup, int red, int green, int blue){
        this.id=id;
        this.brand=brand;
        this.colorNumber=colorNumber;
        this.colorName=colorName;
        this.colorGroup=colorGroup;
        this.red=red;
        this.green=green;
        this.blue=blue;
    }

    public Floss(String brand, String colorNumber, String colorName, String colorGroup, int red, int green, int blue){
        this.brand=brand;
        this.colorNumber=colorNumber;
        this.colorName=colorName;
        this.colorGroup=colorGroup;
        this.red=red;
        this.green=green;
        this.blue=blue;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(String colorNumber) {
        this.colorNumber = colorNumber;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(String colorGroup) {
        this.colorGroup = colorGroup;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    // метод для получения HEX цвета
    public String getHexCode() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
