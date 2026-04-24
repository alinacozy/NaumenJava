package ru.alinacozy.NauJava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import ru.alinacozy.NauJava.entity.Floss;
import ru.alinacozy.NauJava.repository.FlossRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Тестовый класс для FlossServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class FlossServiceTest {

    @Mock
    private FlossRepository flossRepository; //заглушка репозитория

    @InjectMocks
    private FlossServiceImpl flossService; //реальный экземпляр с внедренным Mock-объектом

    // тестовые данные, которые будем использовать во всех тестах
    private Floss testFloss;
    private final Long TEST_ID = 1L;
    private final String TEST_BRAND = "DMC";
    private final String TEST_NUMBER = "310";

    /**
     * Инициализация тестовых данных перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        testFloss = new Floss(TEST_BRAND, TEST_NUMBER, "Black", "Neutral", 0, 0, 0);
        testFloss.setId(TEST_ID);
    }

    /**
     * Тест, покрывающий позитивный сценарий создания нитки
     */
    @Test
    void createFloss_Success() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber(TEST_BRAND, TEST_NUMBER))
                .thenReturn(Optional.empty()); // вернуть как будто в БД нет такой нитки

        // Act
        // assertDoesNotThrow проверяет, что код внутри не выбрасывает исключений
        assertDoesNotThrow(() -> flossService.createFloss(
                TEST_BRAND, TEST_NUMBER, "Black", "Neutral", 0, 0, 0
        ));

        // Assert
        ArgumentCaptor<Floss> flossCaptor = ArgumentCaptor.forClass(Floss.class);

        // метод save был вызван 1 раз, а также делаем capture аргумента
        verify(flossRepository, times(1)).save(flossCaptor.capture());

        // проверяем аргумент метода save
        Floss savedFloss = flossCaptor.getValue();
        assertEquals(TEST_BRAND, savedFloss.getBrand());
        assertEquals(TEST_NUMBER, savedFloss.getColorNumber());
        assertEquals("Black", savedFloss.getColorName());
        assertEquals("Neutral", savedFloss.getColorGroup());
        assertEquals(0, savedFloss.getRed());
        assertEquals(0, savedFloss.getGreen());
        assertEquals(0, savedFloss.getBlue());
    }

    /**
     * Тест, покрывающий негативный сценарий создания нитки
     * (выбрасывающий исключение, что нитка уже существует)
     */
    @Test
    void createFloss_AlreadyExists_ThrowsException() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber(TEST_BRAND, TEST_NUMBER))
                .thenReturn(Optional.of(testFloss)); // мок как будто у нас уже есть эта нитка в БД

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flossService.createFloss(TEST_BRAND, TEST_NUMBER, "Black", "Neutral", 0, 0, 0)
        );

        assertEquals("Floss with brand " + TEST_BRAND + " and number " + TEST_NUMBER + " already exists",
                exception.getMessage());

        // не вызывается метод save
        verify(flossRepository, never()).save(any());
    }


    /**
     * Тест, покрывающий позитивный сценарий поиска нитки по бренду и номеру
     */
    @Test
    void findByBrandAndNumber_Found() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber(TEST_BRAND, TEST_NUMBER))
                .thenReturn(Optional.of(testFloss)); // мок как будто эта нитка есть

        // Act
        Floss result = flossService.findByBrandAndNumber(TEST_BRAND, TEST_NUMBER);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_BRAND, result.getBrand());
        assertEquals(TEST_NUMBER, result.getColorNumber());
    }

    /**
     * Тест, покрывающий негативный сценарий поиска нитки по бренду и номеру
     * сервис должен вернуть выбросить исключение
     */
    @Test
    void findByBrandAndNumber_NotFound_Exception() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber(TEST_BRAND, "999"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ResourceNotFoundException.class,
                () -> flossService.findByBrandAndNumber("DMC", "999"));
    }

    /**
     * Тест, покрывающий позитивный сценарий удаления нитки по Id
     */
    @Test
    void deleteById_Success() {
        // Arrange
        when(flossRepository.existsById(TEST_ID)).thenReturn(true);
        doNothing().when(flossRepository).deleteById(TEST_ID); // так как это void метод

        // Act
        assertDoesNotThrow(() -> flossService.deleteById(TEST_ID));

        // Assert
        verify(flossRepository, times(1)).existsById(TEST_ID);
        verify(flossRepository, times(1)).deleteById(TEST_ID);
    }

    /**
     * Тест, покрывающий негативный сценарий удаления нитки по Id
     * Метод должен выбросить исключение
     */
    @Test
    void deleteById_NotFound_ThrowsException() {
        // Arrange
        when(flossRepository.existsById(TEST_ID)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flossService.deleteById(TEST_ID)
        );

        assertEquals("Floss with id " + TEST_ID + " not found", exception.getMessage());

        // не вызывался метод удаления у репозитория
        verify(flossRepository, never()).deleteById(any());
    }


    /**
     * Тест, покрывающий позитивный сценарий обновления RGB-аналога нитки
     */
    @Test
    void updateRGB_Success() {
        // Arrange
        when(flossRepository.findById(TEST_ID)).thenReturn(Optional.of(testFloss));
        when(flossRepository.save(any(Floss.class))).thenReturn(testFloss);

        int newRed = 255;
        int newGreen = 128;
        int newBlue = 64;

        // Act
        assertDoesNotThrow(() -> flossService.updateRGB(TEST_ID, newRed, newGreen, newBlue));

        // Assert
        assertEquals(newRed, testFloss.getRed());
        assertEquals(newGreen, testFloss.getGreen());
        assertEquals(newBlue, testFloss.getBlue());
        verify(flossRepository, times(1)).save(testFloss);
    }

    /**
     * Тест, покрывающий позитивный сценарий обновления RGB-аналога нитки
     * Обновление несуществующей нитки должно выбрасывать исключение
     */
    @Test
    void updateRGB_FlossNotFound_ThrowsException() {
        // Arrange
        when(flossRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flossService.updateRGB(TEST_ID, 255, 255, 255)
        );

        assertEquals("Floss with id " + TEST_ID + " not found", exception.getMessage());

        // ни разу не вызывался save
        verify(flossRepository, never()).save(any());
    }

    /**
     * Тест, покрывающий позитивный сценарий поиска ближайшего по RGB
     * Сценарий: найдено точное совпадение
     */
    @Test
    void findByColor_ExactMatch_Found() {
        // Arrange
        when(flossRepository.findByRedAndGreenAndBlue(0, 0, 0))
                .thenReturn(Collections.singletonList(testFloss));

        // Act
        Floss result = flossService.findByColor(0, 0, 0);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getRed());
        assertEquals(0, result.getGreen());
        assertEquals(0, result.getBlue());
        // не вызывается поиск всех ниток и не проводится подсчет расстояний
        verify(flossRepository, never()).findAll();
    }

    /**
     * Тест, покрывающий позитивный сценарий поиска ближайшего по RGB
     * Сценарий: не найдено точное совпадение, ищем ближайший по RGB среди всех ниток
     */
    @Test
    void findByColor_NoExactMatch_FindsClosest() {
        // Arrange
        Floss floss2 = new Floss("DMC", "666", "Bright Red", "Red", 255, 0, 0);
        floss2.setId(2L);
        Floss floss3 = new Floss("DMC", "995", "Dark Blue", "Blue", 0, 0, 255);
        floss3.setId(3L);

        List<Floss> allFlosses = Arrays.asList(testFloss, floss2, floss3);

        when(flossRepository.findByRedAndGreenAndBlue(10, 10, 10))
                .thenReturn(List.of()); //точного совпадения нет
        when(flossRepository.findAll()).thenReturn(allFlosses);

        // Act
        Floss result = flossService.findByColor(10, 10, 10);

        // Assert
        assertNotNull(result);
        assertEquals(testFloss.getColorName(), result.getColorName()); // ожидаем что ближе всего черный (testFloss)
        verify(flossRepository, times(1)).findAll(); // 1 раз вызываем поиск всех
    }

    /**
     * Тест, покрывающий негативный сценарий поиска ближайшего по RGB
     * Сценарий: передано неправильное значение RGB, выбрасываем исключение
     */
    @Test
    void findByColor_InvalidRGB_ThrowsException() {
        // Arrange
        int invalidValue = 300;

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flossService.findByColor(invalidValue, 0, 0)
        );

        assertEquals("RGB values must be from 0 to 255", exception.getMessage());
        // не обращаемся ни разу к репозиторию
        verify(flossRepository, never()).findByRedAndGreenAndBlue(anyInt(), anyInt(), anyInt());
    }

    /**
     * Тест, покрывающий негативный сценарий поиска ближайшего по RGB
     * Сценарий: база данных пуста
     * Должно выбрасываться исключение
     */
    @Test
    void findByColor_EmptyDatabase_Exception() {
        // Arrange
        when(flossRepository.findByRedAndGreenAndBlue(100, 100, 100))
                .thenReturn(List.of());
        when(flossRepository.findAll()).thenReturn(List.of());

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> flossService.findByColor(100, 100, 100)
        );

        assertEquals("Floss database is empty. Cannot find colors.",
                exception.getMessage());
    }

    /**
     * Тест, покрывающий позитивный сценарий поиска ближайшего к конкретной нитке
     */
    @Test
    void findSimilar_Success() {
        // Arrange
        Floss similarFloss1 = new Floss("Anchor", "403", "Dark Grey", "Grey", 50, 50, 50);
        similarFloss1.setId(2L);
        Floss similarFloss2 = new Floss("DMC", "666", "Red", "Red", 255, 0, 0);
        similarFloss2.setId(3L);

        List<Floss> allFlosses = Arrays.asList(testFloss, similarFloss1, similarFloss2);

        when(flossRepository.findByBrandAndColorNumber("DMC", "310"))
                .thenReturn(Optional.of(testFloss));
        when(flossRepository.findAll()).thenReturn(allFlosses);

        // Act
        Floss result = flossService.findSimilar("DMC", "310");

        // Assert
        assertNotNull(result);
        assertNotEquals(testFloss.getId(), result.getId());
        assertEquals("Dark Grey", result.getColorName()); // Closest to black
    }

    /**
     * Тест, покрывающий негативный сценарий поиска ближайшего к конкретной нитке
     * Сценарий: не найдена запрашиваемая оригинальная нитка, должно выброситься исключение
     */
    @Test
    void findSimilar_OriginalFlossNotFound_ThrowsException() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber("DMC", "999"))
                .thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> flossService.findSimilar("DMC", "999")
        );

        assertEquals("Floss with brand DMC and number 999 not found", exception.getMessage());
        verify(flossRepository, never()).findAll();
    }

    /**
     * Тест, покрывающий негативный сценарий поиска ближайшего к конкретной нитке
     * Сценарий: в базе данных есть только одна нитка, и это оригинальная (поиск похожего не даст результата)
     * Должно выбрасываться исключение
     */
    @Test
    void findSimilar_OnlyOriginalFlossInDatabase_Exception() {
        // Arrange
        when(flossRepository.findByBrandAndColorNumber("DMC", "310"))
                .thenReturn(Optional.of(testFloss));
        when(flossRepository.findAll()).thenReturn(Collections.singletonList(testFloss));

        // Act + Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> flossService.findSimilar("DMC", "310")
        );

        assertEquals("Floss database contain only one floss. Cannot find similar.",
                exception.getMessage());
    }

    /**
     * Тест метода подсчета расстояния между цветами
     * Сценарий: расстояние между одинаковым цветом
     */
    @Test
    void colorDistance_ExactSameColor() {
        // Arrange
        Floss original = new Floss("DMC", "310", "Black", "Neutral", 100, 150, 200);
        original.setId(1L);
        Floss duplicate = new Floss("Anchor", "403", "Grey", "Grey", 100, 150, 200);
        duplicate.setId(2L);

        when(flossRepository.findByBrandAndColorNumber("DMC", "310"))
                .thenReturn(Optional.of(original));
        when(flossRepository.findAll()).thenReturn(Arrays.asList(original, duplicate));

        // Act
        Floss result = flossService.findSimilar("DMC", "310");

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
    }

    /**
     * Тест метода подсчета расстояния между цветами
     * Сценарий: расстояние между различными цветами, проверка корректности весов
     */
    @Test
    void colorDistance_WeightedCorrectly() {
        // Arrange
        Floss black = testFloss;
        Floss darkRed = new Floss("DMC", "498", "Dark Red", "Red", 100, 0, 0);
        darkRed.setId(2L);
        Floss darkGreen = new Floss("DMC", "500", "Dark Green", "Green", 0, 100, 0);
        darkGreen.setId(3L);

        when(flossRepository.findByBrandAndColorNumber("DMC", "310"))
                .thenReturn(Optional.of(black));
        when(flossRepository.findAll()).thenReturn(Arrays.asList(black, darkRed, darkGreen));

        // Act
        Floss result = flossService.findSimilar("DMC", "310");

        // Assert
        assertNotNull(result);
        // Вес красного меньше, следовательно, ближайшим цветом должен стать красный
        assertEquals("Dark Red", result.getColorName());
    }
}
