package ru.alinacozy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void createFloss(String brand, String number, String colorName, String colorGroup, int red, int green, int blue) {
        // Проверяем, нет ли уже такой нитки
        if (flossRepository.findByBrandAndColorNumber(brand, number).isPresent()) {
            throw new IllegalArgumentException("Floss with brand " + brand + " and number " + number + " already exists");
        }

        Floss newFloss = new Floss(brand, number, colorName, colorGroup, red, green, blue);
        flossRepository.save(newFloss);
    }

    @Override
    public Floss findByBrandAndNumber(String brand, String number) {
        return flossRepository.findByBrandAndColorNumber(brand, number)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Floss not found with brand: " + brand + " and number: " + number
        ));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!flossRepository.existsById(id)) {
            throw new IllegalArgumentException("Floss with id " + id + " not found");
        }
        flossRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateRGB(Long id, int red, int green, int blue) {
        Floss floss = flossRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floss with id " + id + " not found"));

        floss.setRed(red);
        floss.setGreen(green);
        floss.setBlue(blue);
        flossRepository.save(floss);
    }

    @Override
    public Floss findByColor(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be from 0 to 255");
        }

        // Сначала ищем точное совпадение
        List<Floss> exactMatches = flossRepository.findByRedAndGreenAndBlue(red, green, blue);
        if (!exactMatches.isEmpty()) {
            return exactMatches.getFirst();
        }

        // Если точного нет, ищем ближайший
        List<Floss> candidates = flossRepository.findAll();

        // проверяем, что БД не пуста
        if (candidates.isEmpty()) {
            throw new IllegalStateException("Floss database is empty. Cannot find colors.");
        }

        return findClosestByColor(red, green, blue, candidates);
    }

    @Override
    public Floss findSimilar(String brand, String number) {
        Floss original = flossRepository.findByBrandAndColorNumber(brand, number)
                .orElseThrow(() -> new IllegalArgumentException("Floss with brand " + brand + " and number " + number + " not found"));

        List<Floss> candidates = flossRepository.findAll().stream()
                .filter(f -> !f.getId().equals(original.getId()))  // исключаем исходную
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            throw new IllegalStateException("Floss database contain only one floss. Cannot find similar.");
        }

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
