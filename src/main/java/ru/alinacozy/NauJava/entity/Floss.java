package ru.alinacozy.NauJava.entity;

import jakarta.persistence.*;

/**
 * Класс-сущность конкретной нитки из каталога
 */
@Entity
@Table(name = "flosses")
public class Floss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(name = "color_number", nullable = false)
    private String colorNumber;

    @Column(name = "color_name")
    private String colorName;

    @Column(name = "color_group")
    private String colorGroup;

    @Column(nullable = false)
    private Integer red;

    @Column(nullable = false)
    private Integer green;

    @Column(nullable = false)
    private Integer blue;

    // Конструктор по умолчанию (обязателен для JPA)
    public Floss() {
    }

    // Конструктор с ID
    public Floss(Long id, String brand, String colorNumber, String colorName, String colorGroup, Integer red, Integer green, Integer blue) {
        this.id = id;
        this.brand = brand;
        this.colorNumber = colorNumber;
        this.colorName = colorName;
        this.colorGroup = colorGroup;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    // Конструктор без ID
    public Floss(String brand, String colorNumber, String colorName, String colorGroup, Integer red, Integer green, Integer blue) {
        this.brand = brand;
        this.colorNumber = colorNumber;
        this.colorName = colorName;
        this.colorGroup = colorGroup;
        this.red = red;
        this.green = green;
        this.blue = blue;
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

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getBlue() {
        return blue;
    }

    public void setBlue(Integer blue) {
        this.blue = blue;
    }

    // метод для получения HEX цвета
    public String getHexCode() {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    // Дополнительный метод для установки цвета из HEX
    public void setHexCode(String hex) {
        if (hex != null && hex.length() == 7 && hex.charAt(0) == '#') {
            this.red = Integer.parseInt(hex.substring(1, 3), 16);
            this.green = Integer.parseInt(hex.substring(3, 5), 16);
            this.blue = Integer.parseInt(hex.substring(5, 7), 16);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s - %s (%s) [%s]",
                brand, colorNumber, colorName, colorGroup, getHexCode());
    }
}