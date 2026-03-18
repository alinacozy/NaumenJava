package ru.alinacozy.NauJava.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
//import ru.alinacozy.NauJava.console.CommandProcessor;
import ru.alinacozy.NauJava.entity.Floss;

@Configuration
public class Config
{

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void init() {
        System.out.println("======================================");
        System.out.println("        Приложение: " + appName);
        System.out.println("        Версия: " + appVersion);
        System.out.println("======================================");
    }

    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public List<Floss> flossContainer()
    {
        List<Floss> database = new ArrayList<>();

        // DMC - Черные и серые (Нейтральные)
        database.add(new Floss(1L, "DMC", "310", "Черный", "Нейтральные", 0, 0, 0));
        database.add(new Floss(2L, "DMC", "317", "Серый графит", "Нейтральные", 158, 150, 148));
        database.add(new Floss(3L, "DMC", "318", "Светло-серый", "Нейтральные", 200, 200, 200));
        database.add(new Floss(4L, "DMC", "414", "Стальной серый", "Нейтральные", 140, 140, 140));
        database.add(new Floss(5L, "DMC", "762", "Жемчужно-серый", "Нейтральные", 230, 230, 230));

        // Anchor - Черные и серые (Нейтральные)
        database.add(new Floss(6L, "Anchor", "400", "Черный", "Нейтральные", 5, 5, 5));
        database.add(new Floss(7L, "Anchor", "401", "Темно-серый", "Нейтральные", 100, 100, 100));
        database.add(new Floss(8L, "Anchor", "230", "Светло-серый", "Нейтральные", 210, 210, 210));

        // Gamma - Черные и серые (Нейтральные)
        database.add(new Floss(9L, "Gamma", "001", "Белый", "Нейтральные", 255, 255, 255));
        database.add(new Floss(10L, "Gamma", "002", "Черный", "Нейтральные", 10, 10, 10));
        database.add(new Floss(11L, "Gamma", "003", "Серый", "Нейтральные", 150, 150, 150));

        // DMC - Красные и розовые (Красные)
        database.add(new Floss(12L, "DMC", "321", "Рождественский красный", "Красные", 200, 50, 50));
        database.add(new Floss(13L, "DMC", "666", "Ярко-красный", "Красные", 255, 0, 0));
        database.add(new Floss(14L, "DMC", "304", "Средне-красный", "Красные", 180, 40, 40));
        database.add(new Floss(15L, "DMC", "3326", "Нежно-розовый", "Красные", 255, 200, 220));
        database.add(new Floss(16L, "DMC", "3689", "Розово-лиловый", "Красные", 180, 120, 140));

        // Anchor - Красные и розовые (Красные)
        database.add(new Floss(17L, "Anchor", "1000", "Ярко-красный", "Красные", 240, 20, 20));
        database.add(new Floss(18L, "Anchor", "1001", "Темно-розовый", "Красные", 200, 100, 120));
        database.add(new Floss(19L, "Anchor", "48", "Светло-розовый", "Красные", 255, 200, 200));

        // Gamma - Красные и розовые (Красные)
        database.add(new Floss(20L, "Gamma", "011", "Красный", "Красные", 220, 30, 30));
        database.add(new Floss(21L, "Gamma", "012", "Розовый", "Красные", 255, 150, 180));
        database.add(new Floss(22L, "Gamma", "013", "Малиновый", "Красные", 200, 0, 80));

        // DMC - Синие (Синие)
        database.add(new Floss(23L, "DMC", "311", "Темно-синий", "Синие", 0, 50, 100));
        database.add(new Floss(24L, "DMC", "312", "Светло-темно-синий", "Синие", 50, 100, 150));
        database.add(new Floss(25L, "DMC", "334", "Небесно-голубой", "Синие", 150, 200, 255));
        database.add(new Floss(26L, "DMC", "336", "Синий", "Синие", 0, 30, 70));
        database.add(new Floss(27L, "DMC", "3755", "Васильковый", "Синие", 180, 210, 255));

        // Anchor - Синие (Синие)
        database.add(new Floss(28L, "Anchor", "130", "Темно-синий", "Синие", 10, 40, 90));
        database.add(new Floss(29L, "Anchor", "131", "Синий", "Синие", 40, 80, 160));
        database.add(new Floss(30L, "Anchor", "160", "Голубой", "Синие", 170, 210, 250));

        // Gamma - Синие (Синие)
        database.add(new Floss(31L, "Gamma", "020", "Синий", "Синие", 30, 70, 150));
        database.add(new Floss(32L, "Gamma", "021", "Голубой", "Синие", 100, 180, 240));
        database.add(new Floss(33L, "Gamma", "022", "Бирюзовый", "Синие", 0, 150, 200));

        // DMC - Зеленые (Зеленые)
        database.add(new Floss(34L, "DMC", "699", "Рождественский зеленый", "Зеленые", 0, 100, 0));
        database.add(new Floss(35L, "DMC", "700", "Ярко-зеленый", "Зеленые", 0, 150, 0));
        database.add(new Floss(36L, "DMC", "701", "Светло-зеленый", "Зеленые", 50, 180, 50));
        database.add(new Floss(37L, "DMC", "702", "Травяной", "Зеленые", 70, 200, 70));
        database.add(new Floss(38L, "DMC", "703", "Салатовый", "Зеленые", 150, 255, 100));

        // Anchor - Зеленые (Зеленые)
        database.add(new Floss(39L, "Anchor", "210", "Темно-зеленый", "Зеленые", 10, 80, 10));
        database.add(new Floss(40L, "Anchor", "211", "Зеленый", "Зеленые", 30, 130, 30));
        database.add(new Floss(41L, "Anchor", "212", "Мятный", "Зеленые", 150, 220, 150));

        // Gamma - Зеленые (Зеленые)
        database.add(new Floss(42L, "Gamma", "031", "Темно-зеленый", "Зеленые", 20, 90, 20));
        database.add(new Floss(43L, "Gamma", "032", "Зеленый", "Зеленые", 40, 150, 40));
        database.add(new Floss(44L, "Gamma", "033", "Салатовый", "Зеленые", 170, 240, 100));

        // DMC - Желтые и оранжевые (Желтые)
        database.add(new Floss(45L, "DMC", "444", "Лимонный", "Желтые", 255, 200, 0));
        database.add(new Floss(46L, "DMC", "742", "Мандариновый", "Оранжевые", 255, 150, 50));
        database.add(new Floss(47L, "DMC", "743", "Средне-желтый", "Желтые", 255, 200, 100));
        database.add(new Floss(48L, "DMC", "744", "Бледно-желтый", "Желтые", 255, 240, 150));
        database.add(new Floss(49L, "DMC", "947", "Оранжевый", "Оранжевые", 255, 120, 0));

        // Anchor - Желтые и оранжевые (Желтые)
        database.add(new Floss(50L, "Anchor", "300", "Желтый", "Желтые", 250, 210, 0));
        database.add(new Floss(51L, "Anchor", "303", "Оранжевый", "Оранжевые", 240, 140, 20));

        // Gamma - Желтые и оранжевые (Желтые)
        database.add(new Floss(52L, "Gamma", "041", "Желтый", "Желтые", 250, 220, 0));
        database.add(new Floss(53L, "Gamma", "042", "Оранжевый", "Оранжевые", 250, 140, 0));

        // DMC - Фиолетовые (Фиолетовые)
        database.add(new Floss(54L, "DMC", "550", "Темно-фиолетовый", "Фиолетовые", 150, 0, 150));
        database.add(new Floss(55L, "DMC", "552", "Средне-фиолетовый", "Фиолетовые", 180, 100, 180));
        database.add(new Floss(56L, "DMC", "553", "Светло-фиолетовый", "Фиолетовые", 200, 150, 200));
        database.add(new Floss(57L, "DMC", "327", "Фиолетовый", "Фиолетовые", 100, 0, 100));
        database.add(new Floss(58L, "DMC", "153", "Лавандовый", "Фиолетовые", 230, 200, 250));

        // Gamma - Фиолетовые (Фиолетовые)
        database.add(new Floss(59L, "Gamma", "051", "Фиолетовый", "Фиолетовые", 140, 0, 140));
        database.add(new Floss(60L, "Gamma", "052", "Сирень", "Фиолетовые", 200, 160, 220));

        return database;
    }

//    @Bean
//    public CommandLineRunner commandScanner(CommandProcessor commandProcessor) {
//        return args -> {
//            try (Scanner scanner = new Scanner(System.in)) {
//                System.out.println("Введите команду. 'help' для списка команд, 'exit' для выхода.\n");
//
//                while (true) {
//                    // Показать приглашение для ввода
//                    System.out.print("> ");
//                    String input = scanner.nextLine();
//
//                    // Выход из цикла, если введена команда "exit"
//                    if ("exit".equalsIgnoreCase(input.trim())) {
//                        System.out.println("Выход из программы...");
//                        break;
//                    }
//
//                    // Пропускаем пустые строки
//                    if (input.trim().isEmpty()) {
//                        continue;
//                    }
//
//                    // Обработка команды
//                    try {
//                        commandProcessor.processCommand(input);
//                    } catch (Exception e) {
//                        System.out.println("Ошибка: " + e.getMessage());
//                    }
//
//                    System.out.println(); // Пустая строка для читаемости
//                }
//            }
//        };
//    }
}
