package ru.alinacozy.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс-сущность проекта (например, конкретной вышивки).
 * Каждый проект связан с одним пользователем.
 * Для каждого проекта необходимо какое-то количество ниток (RequiredFloss)
 */
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    private String status;

    @Column(name = "pattern_src")
    private String patternSrc;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequiredFloss> requiredFlosses;

    // Constructors
    public Project() {}

    public Project(String projectName, User user, String description,
                   LocalDateTime startDate, String status, String patternSrc) {
        this.projectName = projectName;
        this.user = user;
        this.description = description;
        this.startDate = startDate;
        this.status = status;
        this.patternSrc = patternSrc;
        this.requiredFlosses = new ArrayList<>();
    }

    public Project(String projectName, User user, String description,
                   LocalDateTime startDate, String status, String patternSrc,
                   List<RequiredFloss> requiredFlosses) {
        this.projectName = projectName;
        this.user = user;
        this.description = description;
        this.startDate = startDate;
        this.status = status;
        this.patternSrc = patternSrc;
        this.requiredFlosses = requiredFlosses != null ? requiredFlosses : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatternSrc() {
        return patternSrc;
    }

    public void setPatternSrc(String patternSrc) {
        this.patternSrc = patternSrc;
    }

    public List<RequiredFloss> getRequiredFlosses() {
        return requiredFlosses;
    }

    public void setRequiredFlosses(List<RequiredFloss> requiredFlosses) {
        this.requiredFlosses = requiredFlosses;
    }

    // вспомогательные методы
    public void addRequiredFloss(RequiredFloss requiredFloss) {
        requiredFlosses.add(requiredFloss);
        requiredFloss.setProject(this);
    }

    public void removeRequiredFloss(RequiredFloss requiredFloss) {
        requiredFlosses.remove(requiredFloss);
        requiredFloss.setProject(null);
    }
}
