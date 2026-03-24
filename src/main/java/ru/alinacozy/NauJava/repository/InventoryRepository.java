package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alinacozy.NauJava.entity.Inventory;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "inventory")
public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    /**
     * Находит все записи инвентаря для указанного пользователя
     * @param userId идентификатор пользователя
     */
    List<Inventory> findByUserId(Long userId);

    /**
     * Находит запись инвентаря по пользователю и нитке
     * @param userId идентификатор пользователя
     * @param flossId идентификатор нитки
     */
    Optional<Inventory> findByUserIdAndFlossId(Long userId, Long flossId);
}
