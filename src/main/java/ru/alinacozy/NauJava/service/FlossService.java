package ru.alinacozy.NauJava.service;

import ru.alinacozy.NauJava.entity.Floss;

public interface FlossService
{
    void createFloss(String brand, String number, String colorName, String colorGroup, int red, int green, int blue);
    Floss findByBrandAndNumber(String brand, String number);
    void deleteById(Long id);
    void updateRGB(Long id, int red, int green, int blue);
    Floss findByColor(int red, int green, int blue); // найти наиболее похожий цвет
    Floss findSimilar(String brand, String number); // найти похожий по номеру и бренду
}

