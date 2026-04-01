package ru.alinacozy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alinacozy.NauJava.dao.ProjectRepositoryCustom;
import ru.alinacozy.NauJava.entity.Project;

import java.util.List;

/**
 * Контроллер реализует mappings для кастомных методов ProjectRepositoryCustom
 */
@RestController
@RequestMapping("/custom/projects")
public class ProjectController {

    private final ProjectRepositoryCustom projectRepository;

    @Autowired
    public ProjectController(ProjectRepositoryCustom projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/findByUserIdAndProjectName")
    public List<Project> findByUserIdAndProjectName(@RequestParam Long userId, @RequestParam String projectName)
    {
        return projectRepository.findByUserIdAndProjectNameContainingIgnoreCase(userId, projectName);
    }

    @GetMapping("/findByRequiredFlossBrand")
    public List<Project> findByByRequiredFlossBrand(@RequestParam String brand)
    {
        return projectRepository.findByRequiredFlossBrand(brand);
    }


}
