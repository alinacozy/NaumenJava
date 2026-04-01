package ru.alinacozy.NauJava.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alinacozy.NauJava.entity.Floss;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class FlossTest {

    private final FlossRepository flossRepository;

    @Autowired
    FlossTest(FlossRepository flossRepository)
    {
        this.flossRepository = flossRepository;
    }


    /**
     * Тест для метода findByBrandAndColorNumber
     */
    @Test
    void testFindByBrandAndColorNumber() {
        // Подготовка
        String brandName = UUID.randomUUID().toString();
        String colorNumber = UUID.randomUUID().toString();

        Floss floss = new Floss();
        floss.setBrand(brandName);
        floss.setColorNumber(colorNumber);
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        flossRepository.save(floss);  // Сохраняется в рамках транзакции

        // Действие
        Optional<Floss> found = flossRepository.findByBrandAndColorNumber(brandName, colorNumber);

        // Проверка
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(brandName, found.get().getBrand());
        Assertions.assertEquals(colorNumber, found.get().getColorNumber());
    }


    /**
     * Тест для метода findAll()
     */
    @Test
    void testFindAll() {
        // Подготовка - создаем несколько ниток
        Floss floss1 = new Floss();
        floss1.setBrand("DMC-1");
        floss1.setColorNumber("310");
        floss1.setColorName("Black");
        floss1.setRed(0);
        floss1.setGreen(0);
        floss1.setBlue(0);
        flossRepository.save(floss1);

        Floss floss2 = new Floss();
        floss2.setBrand("DMC-2");
        floss2.setColorNumber("666");
        floss2.setColorName("Red");
        floss2.setRed(255);
        floss2.setGreen(0);
        floss2.setBlue(0);
        flossRepository.save(floss2);

        // Действие
        List<Floss> allFlosses = flossRepository.findAll();

        // Проверка
        Assertions.assertNotNull(allFlosses);
        Assertions.assertTrue(allFlosses.size() >= 2);
        Assertions.assertTrue(allFlosses.stream().anyMatch(f -> "DMC-1".equals(f.getBrand())));
        Assertions.assertTrue(allFlosses.stream().anyMatch(f -> "DMC-2".equals(f.getBrand())));
    }


    /**
     * Тест для метода findByRedAndGreenAndBlue - точное совпадение
     */
    @Test
    void testFindByRedAndGreenAndBlue() {
        // Подготовка
        Floss floss1 = new Floss();
        floss1.setBrand("DMC-1");
        floss1.setColorNumber("310");
        floss1.setColorName("Black");
        floss1.setRed(0);
        floss1.setGreen(0);
        floss1.setBlue(0);
        flossRepository.save(floss1);

        Floss floss2 = new Floss();
        floss2.setBrand("DMC-2");
        floss2.setColorNumber("666");
        floss2.setColorName("Red");
        floss2.setRed(255);
        floss2.setGreen(0);
        floss2.setBlue(0);
        flossRepository.save(floss2);

        // Действие - ищем черный цвет
        List<Floss> found = flossRepository.findByRedAndGreenAndBlue(0, 0, 0);

        // Проверка
        Assertions.assertNotNull(found);
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertTrue(found.stream().anyMatch(f -> "DMC-1".equals(f.getBrand())));
        Assertions.assertTrue(found.stream().noneMatch(f -> "DMC-2".equals(f.getBrand())));
    }

    /**
     * Тест для метода findByRedBetweenAndGreenBetweenAndBlueBetween - поиск в диапазоне
     */
    @Test
    void testFindByRedBetweenAndGreenBetweenAndBlueBetween() {
        // Подготовка - создаем несколько ниток с разными цветами
        Floss floss1 = new Floss(); // черный
        floss1.setBrand("DMC-1");
        floss1.setColorNumber("310");
        floss1.setColorName("Black");
        floss1.setRed(0);
        floss1.setGreen(0);
        floss1.setBlue(0);
        flossRepository.save(floss1);

        Floss floss2 = new Floss(); // красный
        floss2.setBrand("DMC-2");
        floss2.setColorNumber("666");
        floss2.setColorName("Red");
        floss2.setRed(255);
        floss2.setGreen(0);
        floss2.setBlue(0);
        flossRepository.save(floss2);

        Floss floss3 = new Floss(); // темно-серый (близкий к черному)
        floss3.setBrand("DMC-3");
        floss3.setColorNumber("317");
        floss3.setColorName("Dark Gray");
        floss3.setRed(50);
        floss3.setGreen(50);
        floss3.setBlue(50);
        flossRepository.save(floss3);

        // Действие - ищем цвета с красным от 0 до 100, зеленым от 0 до 100, синим от 0 до 100
        List<Floss> found = flossRepository.findByRedBetweenAndGreenBetweenAndBlueBetween(
                0, 100,  // minRed, maxRed
                0, 100,  // minGreen, maxGreen
                0, 100   // minBlue, maxBlue
        );

        // Проверка
        Assertions.assertNotNull(found);
        Assertions.assertEquals(2, found.size()); // должны найтись черный и серый
        Assertions.assertTrue(found.stream().anyMatch(f -> "DMC-1".equals(f.getBrand())));
        Assertions.assertTrue(found.stream().anyMatch(f -> "DMC-3".equals(f.getBrand())));
        Assertions.assertTrue(found.stream().noneMatch(f -> "DMC-2".equals(f.getBrand())));
    }

    /**
     * Тест для метода findByRedBetweenAndGreenBetweenAndBlueBetween - пустой результат
     */
    @Test
    void testFindByRedBetweenAndGreenBetweenAndBlueBetween_Empty() {
        // Подготовка - создаем одну нитку
        Floss floss = new Floss();
        floss.setBrand("DMC");
        floss.setColorNumber("310");
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        flossRepository.save(floss);

        // Действие - ищем в диапазоне, где ничего нет
        List<Floss> found = flossRepository.findByRedBetweenAndGreenBetweenAndBlueBetween(
                100, 200,  // minRed, maxRed
                100, 200,  // minGreen, maxGreen
                100, 200   // minBlue, maxBlue
        );

        // Проверка
        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.isEmpty());
    }
}
