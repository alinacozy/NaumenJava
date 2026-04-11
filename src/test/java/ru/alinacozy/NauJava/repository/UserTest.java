package ru.alinacozy.NauJava.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.Role;
import ru.alinacozy.NauJava.entity.User;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class UserTest {

    private final UserRepository userRepository;

    @Autowired
    UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Тест для метода findByUsername
     */
    @Test
    void testFindByUsername() {
        // Создаем пользователя
        String username = "user-" + UUID.randomUUID();

        User user = new User();
        user.setUsername(username);
        user.setRole(Role.USER);
        userRepository.save(user);

        // Поиск по имени пользователя
        Optional<User> found = userRepository.findByUsername(username);

        // Проверки
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(username, found.get().getUsername());
        Assertions.assertEquals(Role.USER, found.get().getRole());
    }
}
