package ru.alinacozy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.repository.FlossRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlossServiceImpl implements FlossService{
    private final FlossRepository flossRepository;

    @Autowired
    public FlossServiceImpl(FlossRepository flossRepository)
    {
        this.flossRepository = flossRepository;
    }

    @Override
    public void createFloss(String brand, String number, String colorName, String colorGroup, int red, int green, int blue) {
        if (brand == null || number == null || colorName == null || colorGroup == null) {
            throw new IllegalArgumentException("Brand, number, colorName and colorGroup cannot be null");
        }
        // Проверка RGB (0-255)
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be from 0 to 255");
        }
        Floss newFloss=new Floss(brand, number, colorName, colorGroup, red, green, blue);
        flossRepository.create(newFloss);
    }

    @Override
    public Floss findByBrandAndNumber(String brand, String number) {
        return flossRepository.findByBrandAndNumber(brand, number);
    }

    @Override
    public void deleteById(Long id) {
        flossRepository.delete(id);
    }

    @Override
    public void updateRGB(Long id, int red, int green, int blue) {
        Floss floss=flossRepository.read(id);
        if (floss == null) {
            throw new IllegalArgumentException("Floss with id " + id + " not found");
        }
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be from 0 to 255");
        }
        floss.setRed(red);
        floss.setGreen(green);
        floss.setBlue(blue);
        flossRepository.update(floss);
    }

    @Override
    public Floss findByColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be from 0 to 255");
        }
        List<Floss> candidates = flossRepository.findAll();

        return findClosestByColor(red, green, blue, candidates);
    }

    @Override
    public Floss findSimilar(String brand, String number) {
        Floss original =flossRepository.findByBrandAndNumber(brand, number);
        if (original == null){
            throw new IllegalArgumentException("Floss with selected brand and number not found");
        }
        List<Floss> candidates = flossRepository.findAll().stream()
                .filter(f -> !f.getId().equals(original.getId()))  // исключаем исходную
                .collect(Collectors.toList());

        return findClosestByColor(original.getRed(), original.getGreen(), original.getBlue(), candidates);
    }

    private Floss findClosestByColor(int red, int green, int blue, List<Floss> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(f ->
                        distanceWeighted(red, green, blue,
                                f.getRed(), f.getGreen(), f.getBlue())))
                .orElse(null);
    }

    /**
     * Вычисляем взвешенное расстояние между rgb-цветами
     * с коэффициентами, учитывающими восприятие человеком
     */
    private double distanceWeighted(int first_r, int first_g, int first_b, int second_r, int second_g, int second_b) {
        int dr = first_r - second_r;
        int dg = first_g - second_g;
        int db = first_b - second_b;

        // Коэффициенты учитывают чувствительность глаза
        double r = 0.3 * dr * dr;
        double g = 0.59 * dg * dg;
        double b = 0.11 * db * db;

        return Math.sqrt(r + g + b);
    }
}
