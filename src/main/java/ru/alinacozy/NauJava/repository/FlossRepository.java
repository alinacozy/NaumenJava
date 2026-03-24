package ru.alinacozy.NauJava.repository;

import ru.alinacozy.NauJava.entity.Floss;

import java.util.List;

/**
 * Интерфейс, расширяющий базовый CRUD и содержащий методы, специфичные для Floss
 */
public interface FlossRepository extends CrudRepository<Floss, Long>{
    Floss findByBrandAndNumber(String brand, String number);
    List<Floss> findByBrand(String brand);
    List<Floss> findByColorGroup(String colorGroup);
}
