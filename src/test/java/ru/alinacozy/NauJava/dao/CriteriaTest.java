package ru.alinacozy.NauJava.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.*;
import ru.alinacozy.NauJava.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class CriteriaTest {

    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FlossRepository flossRepository;
    private final RequiredFlossRepository requiredFlossRepository;

    @Autowired
    CriteriaTest(ProjectRepositoryCustom projectRepositoryCustom,
                 UserRepository userRepository,
                 ProjectRepository projectRepository,
                 FlossRepository flossRepository,
                 RequiredFlossRepository requiredFlossRepository) {
        this.projectRepositoryCustom = projectRepositoryCustom;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.flossRepository = flossRepository;
        this.requiredFlossRepository = requiredFlossRepository;
    }

    /**
     * Тест для метода findByUserIdAndProjectNameContainingIgnoreCase (Criteria API)
     */
    @Test
    void testFindByUserIdAndProjectNameContainingIgnoreCase() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем проекты
        Project project1 = new Project();
        project1.setProjectName("CROSS STITCH PATTERN");
        project1.setUser(user);
        project1.setStartDate(LocalDateTime.now());
        projectRepository.save(project1);

        Project project2 = new Project();
        project2.setProjectName("EMBROIDERY FLOWERS");
        project2.setUser(user);
        project2.setStartDate(LocalDateTime.now());
        projectRepository.save(project2);

        // Поиск через Criteria API
        List<Project> found = projectRepositoryCustom.findByUserIdAndProjectNameContainingIgnoreCase(
                user.getId(), "stitch");

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("CROSS STITCH PATTERN", found.get(0).getProjectName());
    }

    /**
     * Тест для метода findByRequiredFlossBrand (Criteria API)
     */
    @Test
    void testFindByRequiredFlossBrand() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        projectRepository.save(project);

        // Создаем нитку DMC
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

        // Поиск через Criteria API
        List<Project> found = projectRepositoryCustom.findByRequiredFlossBrand("DMC");

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals("Test Project", found.get(0).getProjectName());
    }
}