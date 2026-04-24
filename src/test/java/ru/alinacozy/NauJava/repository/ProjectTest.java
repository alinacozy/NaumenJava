package ru.alinacozy.NauJava.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class ProjectTest {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FlossRepository flossRepository;
    private final RequiredFlossRepository requiredFlossRepository;

    @Autowired
    ProjectTest(ProjectRepository projectRepository,
                UserRepository userRepository,
                FlossRepository flossRepository,
                RequiredFlossRepository requiredFlossRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.flossRepository = flossRepository;
        this.requiredFlossRepository = requiredFlossRepository;
    }

    /**
     * Тест для метода findByUserId
     */
    @Test
    void testFindByUserId() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        project.setDescription("Test Description");
        project.setStartDate(LocalDateTime.now());
        project.setStatus("IN_PROGRESS");
        projectRepository.save(project);

        // Поиск проектов по userId
        List<Project> found = projectRepository.findByUserId(user.getId());

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("Test Project", found.get(0).getProjectName());
    }

    /**
     * Тест для метода findByUserIdAndProjectNameContainingIgnoreCase
     */
    @Test
    void testFindByUserIdAndProjectNameContainingIgnoreCase() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("CROSS STITCH PATTERN");
        project.setUser(user);
        projectRepository.save(project);

        // Поиск по части названия (без учета регистра)
        List<Project> found = projectRepository.findByUserIdAndProjectNameContainingIgnoreCase(
                user.getId(), "stitch");

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("CROSS STITCH PATTERN", found.get(0).getProjectName());
    }

    /**
     * Тест для метода findByUserIdAndStatus
     */
    @Test
    void testFindByUserIdAndStatus() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        project.setStatus("COMPLETED");
        projectRepository.save(project);

        // Поиск по статусу
        List<Project> found = projectRepository.findByUserIdAndStatus(user.getId(), "COMPLETED");

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("COMPLETED", found.getFirst().getStatus());
    }

    /**
     * Тест для метода findByRequiredFlossBrand (JPQL запрос через связанную сущность)
     */
    @Test
    void testFindByRequiredFlossBrand() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        projectRepository.save(project);

        // Создаем нитку
        Floss floss = new Floss();
        floss.setBrand("DMC");
        floss.setColorNumber("310");
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        flossRepository.save(floss);

        // Создаем RequiredFloss для проекта
        RequiredFloss requiredFloss = new RequiredFloss();
        requiredFloss.setProject(project);
        requiredFloss.setFloss(floss);
        requiredFloss.setQuantity(2);
        requiredFlossRepository.save(requiredFloss);

        // Поиск проектов по бренду нитки
        List<Project> found = projectRepository.findByRequiredFlossBrand("DMC");

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("Test Project", found.getFirst().getProjectName());
    }
}