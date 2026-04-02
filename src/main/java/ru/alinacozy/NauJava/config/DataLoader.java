package ru.alinacozy.NauJava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.alinacozy.NauJava.entity.*;
import ru.alinacozy.NauJava.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlossRepository flossRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RequiredFlossRepository requiredFlossRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        boolean isEmpty = userRepository.count() == 0
                && flossRepository.count() == 0
                && projectRepository.count() == 0
                && inventoryRepository.count() == 0
                && requiredFlossRepository.count() == 0;

        if (!isEmpty) {
            System.out.println("База данных не пуста. Загрузка данных пропущена.");
            return;
        }

        System.out.println("База данных пуста. Загрузка тестовых данных...");

        // Пользователи
        User user1 = new User("alinacozy", passwordEncoder.encode("123"), Role.ADMIN);
        User user2 = new User("john_doe", passwordEncoder.encode("123"), Role.USER);
        userRepository.saveAll(List.of(user1, user2));

        // Нитки
        Floss floss1 = new Floss("DMC", "321", "Christmas Red", "Red", 201, 35, 38);
        Floss floss2 = new Floss("DMC", "310", "Black", "Black", 0, 0, 0);
        Floss floss3 = new Floss("DMC", "5200", "Snow White", "White", 255, 255, 255);
        Floss floss4 = new Floss("Anchor", "100", "Red", "Red", 200, 40, 45);
        Floss floss5 = new Floss("Anchor", "403", "Black", "Black", 10, 10, 15);
        Floss floss6 = new Floss("Madeira", "1001", "Scarlet", "Red", 210, 30, 35);
        Floss floss7 = new Floss("Gamma", "111", "Bright Red", "Red", 215, 25, 30);
        flossRepository.saveAll(List.of(floss1, floss2, floss3, floss4, floss5, floss6, floss7));

        // Проекты
        Project project1 = new Project("Шотландка", user1,
                "Фенечка с шотландской клеткой",
                LocalDateTime.now(), "in_progress", "/patterns/scottish.pdf");
        Project project2 = new Project("Гейзер", user1,
                "Классическая фенечка гейзер",
                LocalDateTime.now(), "planning", "/patterns/geyser.pdf");
        Project project3 = new Project("Розовый закат", user1,
                "Вышивка заката над морем",
                LocalDateTime.now(), "completed", "/patterns/sunset.pdf");
        Project project4 = new Project("Лесная поляна", user2,
                "Цветущая поляна в лесу",
                LocalDateTime.now(), "in_progress", "/patterns/forest.pdf");
        Project project5 = new Project("Морской бриз", user2,
                "Морской пейзаж с парусником",
                LocalDateTime.now(), "planning", "/patterns/sea.pdf");
        projectRepository.saveAll(List.of(project1, project2, project3, project4, project5));

        // Инвентарь
        inventoryRepository.saveAll(List.of(
                new Inventory(floss1, user1, 5),
                new Inventory(floss2, user1, 3),
                new Inventory(floss3, user1, 10),
                new Inventory(floss4, user1, 2),
                new Inventory(floss7, user1, 4),
                new Inventory(floss1, user2, 3),
                new Inventory(floss5, user2, 7),
                new Inventory(floss6, user2, 2),
                new Inventory(floss2, user2, 5),
                new Inventory(floss4, user2, 1)
        ));

        // Требуемые нитки
        requiredFlossRepository.save(new RequiredFloss(project1, floss1, 3));
        requiredFlossRepository.save(new RequiredFloss(project1, floss2, 2));
        requiredFlossRepository.save(new RequiredFloss(project1, floss4, 1));
        requiredFlossRepository.save(new RequiredFloss(project1, 180, 100, 50, "Коричневый", 2));

        requiredFlossRepository.save(new RequiredFloss(project2, floss3, 4));
        requiredFlossRepository.save(new RequiredFloss(project2, floss6, 2));
        requiredFlossRepository.save(new RequiredFloss(project2, floss1, 1));

        requiredFlossRepository.save(new RequiredFloss(project3, floss1, 5));
        requiredFlossRepository.save(new RequiredFloss(project3, floss3, 3));
        requiredFlossRepository.save(new RequiredFloss(project3, 255, 200, 200, "Нежно-розовый", 2));
        requiredFlossRepository.save(new RequiredFloss(project3, 255, 150, 150, "Средний розовый", 2));

        requiredFlossRepository.save(new RequiredFloss(project4, floss7, 3));
        requiredFlossRepository.save(new RequiredFloss(project4, floss5, 2));
        requiredFlossRepository.save(new RequiredFloss(project4, 100, 150, 80, "Лесной зеленый", 4));
        requiredFlossRepository.save(new RequiredFloss(project4, 210, 180, 100, "Песочный", 2));

        requiredFlossRepository.save(new RequiredFloss(project5, floss4, 3));
        requiredFlossRepository.save(new RequiredFloss(project5, floss3, 5));
        requiredFlossRepository.save(new RequiredFloss(project5, 50, 100, 180, "Морская волна", 3));
        requiredFlossRepository.save(new RequiredFloss(project5, 150, 200, 210, "Голубой", 2));

        System.out.println("Тестовые данные успешно загружены");
    }
}
