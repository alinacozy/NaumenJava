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
class RequiredFlossTest {

    private final RequiredFlossRepository requiredFlossRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FlossRepository flossRepository;

    @Autowired
    RequiredFlossTest(RequiredFlossRepository requiredFlossRepository,
                      ProjectRepository projectRepository,
                      UserRepository userRepository,
                      FlossRepository flossRepository) {
        this.requiredFlossRepository = requiredFlossRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.flossRepository = flossRepository;
    }

    /**
     * Вспомогательный метод для создания пользователя
     */
    private User createTestUser() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        return userRepository.save(user);
    }

    /**
     * Вспомогательный метод для создания проекта
     */
    private Project createTestProject(User user) {
        Project project = new Project();
        project.setProjectName("Test Project " + UUID.randomUUID());
        project.setUser(user);
        project.setStartDate(LocalDateTime.now());
        return projectRepository.save(project);
    }

    /**
     * Вспомогательный метод для создания нитки
     */
    private Floss createTestFloss() {
        Floss floss = new Floss();
        floss.setBrand("DMC");
        floss.setColorNumber("310");
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        return flossRepository.save(floss);
    }

    /**
     * Тест для метода findByProjectId
     */
    @Test
    void testFindByProjectId() {
        // Создаем пользователя и проект
        User user = createTestUser();
        Project project = createTestProject(user);

        // Создаем нитку
        Floss floss = createTestFloss();

        // Создаем RequiredFloss
        RequiredFloss requiredFloss = new RequiredFloss();
        requiredFloss.setProject(project);
        requiredFloss.setFloss(floss);
        requiredFloss.setQuantity(3);
        requiredFlossRepository.save(requiredFloss);

        // Поиск по projectId
        List<RequiredFloss> found = requiredFlossRepository.findByProjectId(project.getId());

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(3, found.get(0).getQuantity());
        Assertions.assertEquals("DMC", found.get(0).getFloss().getBrand());
    }

    /**
     * Тест для метода findByProjectIdAndQuantityLessThan
     */
    @Test
    void testFindByProjectIdAndQuantityLessThan() {
        // Создаем пользователя и проект
        User user = createTestUser();
        Project project = createTestProject(user);

        // Создаем нитку
        Floss floss = createTestFloss();

        // Создаем RequiredFloss с количеством 2
        RequiredFloss requiredFloss = new RequiredFloss();
        requiredFloss.setProject(project);
        requiredFloss.setFloss(floss);
        requiredFloss.setQuantity(2);
        requiredFlossRepository.save(requiredFloss);

        // Поиск с условием quantity < 5 (должен найти)
        List<RequiredFloss> found = requiredFlossRepository.findByProjectIdAndQuantityLessThan(
                project.getId(), 5);

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(2, found.get(0).getQuantity());

        // Поиск с условием quantity < 1 (не должен найти)
        List<RequiredFloss> notFound = requiredFlossRepository.findByProjectIdAndQuantityLessThan(
                project.getId(), 1);

        // Проверки
        Assertions.assertNotNull(notFound);
        Assertions.assertTrue(notFound.isEmpty());
    }
}