package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alinacozy.NauJava.entity.Floss;
import java.util.List;
import java.util.Optional;

public interface FlossRepository extends CrudRepository<Floss, Long> {

    /**
     * Находит нитку по бренду и номеру цвета
     */
    Optional<Floss> findByBrandAndColorNumber(String brand, String colorNumber);

    /**
     * Находит все нитки (замена findAll())
     */
    List<Floss> findAll();

    /**
     * Поиск по точным RGB значениям
     */
    List<Floss> findByRedAndGreenAndBlue(int red, int green, int blue);

    /**
     * Поиск ниток с RGB значениями в диапазоне (для оптимизации поиска ближайшего цвета)
     */
    List<Floss> findByRedBetweenAndGreenBetweenAndBlueBetween(
            int minRed, int maxRed,
            int minGreen, int maxGreen,
            int minBlue, int maxBlue);
}