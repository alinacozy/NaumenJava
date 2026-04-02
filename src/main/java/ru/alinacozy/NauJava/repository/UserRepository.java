package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alinacozy.NauJava.entity.User;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Поиск пользователя по имени пользователя
     * @param username имя пользователя
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверка существования по имени пользователя
     * @param username имя пользователя
     */
    boolean existsByUsername(String username);
}
