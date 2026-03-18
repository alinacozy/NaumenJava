package ru.alinacozy.NauJava.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.User;

import java.util.List;
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
        user.setRole("USER");
        userRepository.save(user);

        // Поиск по имени пользователя
        List<User> found = userRepository.findByUsername(username);

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(username, found.getFirst().getUsername());
        Assertions.assertEquals("USER", found.getFirst().getRole());
    }
}
