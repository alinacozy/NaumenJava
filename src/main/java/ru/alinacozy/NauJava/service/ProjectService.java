package ru.alinacozy.NauJava.service;

public interface ProjectService {
    /**
     * Удаляет проект со всеми связанными RequiredFloss
     * @param projectId id проекта
     */
    void deleteProjectWithRequiredFlosses(Long projectId);
}
