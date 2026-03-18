package ru.alinacozy.NauJava.dao;

import ru.alinacozy.NauJava.entity.Project;
import java.util.List;

public interface ProjectRepositoryCustom {

    /**
     * Находит проекты по идентификатору пользователя и части названия проекта (без учета регистра)
     *
     * @param userId идентификатор пользователя
     * @param projectName часть названия проекта
     * @return список проектов, удовлетворяющих условиям
     */
    List<Project> findByUserIdAndProjectNameContainingIgnoreCase(Long userId, String projectName);

    /**
     * Находит все проекты, в которых используется нитка с указанным брендом
     *
     * @param brand бренд нитки
     * @return список проектов
     */
    List<Project> findByRequiredFlossBrand(String brand);
}
