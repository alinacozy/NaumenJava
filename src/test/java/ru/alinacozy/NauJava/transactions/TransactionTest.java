package ru.alinacozy.NauJava.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.*;
import ru.alinacozy.NauJava.repository.*;
import ru.alinacozy.NauJava.service.ProjectService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class TransactionTest {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final RequiredFlossRepository requiredFlossRepository;
    private final UserRepository userRepository;
    private final FlossRepository flossRepository;

    @Autowired
    TransactionTest(ProjectService projectService,
                    ProjectRepository projectRepository,
                    RequiredFlossRepository requiredFlossRepository,
                    UserRepository userRepository,
                    FlossRepository flossRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.requiredFlossRepository = requiredFlossRepository;
        this.userRepository = userRepository;
        this.flossRepository = flossRepository;
    }

    /**
     * Тест успешного удаления проекта со всеми связанными нитками
     */
    @Test
    void testDeleteProjectWithRequiredFlosses_Success() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        project.setStartDate(LocalDateTime.now());
        project.setStatus("IN_PROGRESS");
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
        requiredFloss.setQuantity(3);
        requiredFlossRepository.save(requiredFloss);

        // Проверяем, что все создалось
        Assertions.assertTrue(projectRepository.findById(project.getId()).isPresent());
        Assertions.assertEquals(1, requiredFlossRepository.findByProjectId(project.getId()).size());

        // Удаляем проект с нитками
        projectService.deleteProjectWithRequiredFlosses(project.getId());

        // Проверяем, что все удалилось
        Optional<Project> foundProject = projectRepository.findById(project.getId());
        Assertions.assertTrue(foundProject.isEmpty());

        List<RequiredFloss> foundRequiredFlosses = requiredFlossRepository.findByProjectId(project.getId());
        Assertions.assertTrue(foundRequiredFlosses.isEmpty());
    }

    /**
     * Тест отката транзакции при ошибке (негативный кейс)
     */
    @Test
    void testDeleteProjectWithRequiredFlosses_Rollback() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем проект
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setUser(user);
        project.setStartDate(LocalDateTime.now());
        project.setStatus("IN_PROGRESS");
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
        requiredFloss.setQuantity(3);
        requiredFlossRepository.save(requiredFloss);

        // Проверяем, что все создалось
        Assertions.assertTrue(projectRepository.findById(project.getId()).isPresent());
        Assertions.assertEquals(1, requiredFlossRepository.findByProjectId(project.getId()).size());

        // Пытаемся удалить несуществующий проект - должно вызвать ошибку
        Long nonExistentId = 99999L;

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            projectService.deleteProjectWithRequiredFlosses(nonExistentId);
        });

        Assertions.assertEquals("Project with id " + nonExistentId + " not found", exception.getMessage());

        // Проверяем, что исходные данные НЕ удалились (транзакция откатилась)
        Optional<Project> foundProject = projectRepository.findById(project.getId());
        Assertions.assertTrue(foundProject.isPresent());

        List<RequiredFloss> foundRequiredFlosses = requiredFlossRepository.findByProjectId(project.getId());
        Assertions.assertEquals(1, foundRequiredFlosses.size());
    }
}