package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.alinacozy.NauJava.entity.RequiredFloss;
import java.util.List;

public interface RequiredFlossRepository extends CrudRepository<RequiredFloss, Long> {

    /**
     * Находит все требуемые нитки для указанного проекта
     * @param projectId идентификатор проекта
     */
    List<RequiredFloss> findByProjectId(Long projectId);

    /**
     * Находит требуемые нитки по проекту с количеством меньше указанного
     * @param projectId идентификатор проекта
     * @param quantity минимальное количество
     */
    List<RequiredFloss> findByProjectIdAndQuantityLessThan(Long projectId, int quantity);
}
