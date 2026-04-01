package ru.alinacozy.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alinacozy.NauJava.entity.Project;
import java.util.List;

@RepositoryRestResource(path = "projects")
public interface ProjectRepository extends CrudRepository<Project, Long> {

    /**
     * Находит все проекты пользователя
     * @param userId идентификатор пользователя
     */
    List<Project> findByUserId(Long userId);

    /**
     * Находит проекты пользователя по названию (содержит указанный текст)
     * @param userId часть названия проекта
     * @param projectName часть названия проекта
     */
    List<Project> findByUserIdAndProjectNameContainingIgnoreCase(Long userId, String projectName);

    /**
     * Находит проекты пользователя по статусу
     * @param userId часть названия проекта
     * @param status статус проекта
     */
    List<Project> findByUserIdAndStatus(Long userId,String status);

    /**
     * Находит все проекты, в которых используется нитка с указанным брендом
     * (метод с JPQL для поиска через связанную сущность)
     * @param brand бренд нитки
     */
    @Query("SELECT DISTINCT p FROM Project p JOIN p.requiredFlosses rf JOIN rf.floss f WHERE f.brand = :brand")
    List<Project> findByRequiredFlossBrand(@Param("brand") String brand);

}
