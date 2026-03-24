package ru.alinacozy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.alinacozy.NauJava.entity.User;
import java.util.List;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Поиск пользователей по имени пользователя
     * @param username имя пользователя
     */
    List<User> findByUsername(String username);
}
