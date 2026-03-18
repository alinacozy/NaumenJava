package ru.alinacozy.NauJava.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.alinacozy.NauJava.entity.Inventory;
import ru.alinacozy.NauJava.entity.User;
import ru.alinacozy.NauJava.entity.Floss;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
class InventoryTest {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final FlossRepository flossRepository;

    @Autowired
    InventoryTest(InventoryRepository inventoryRepository,
                  UserRepository userRepository,
                  FlossRepository flossRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.flossRepository = flossRepository;
    }

    /**
     * Тест для метода findByUserId
     */
    @Test
    void testFindByUserId() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем нитку
        Floss floss = new Floss();
        floss.setBrand("DMC");
        floss.setColorNumber("310");
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        flossRepository.save(floss);

        // Создаем запись в инвентаре
        Inventory inventory = new Inventory();
        inventory.setUser(user);
        inventory.setFloss(floss);
        inventory.setQuantity(5);
        inventoryRepository.save(inventory);

        // Поиск по userId
        List<Inventory> found = inventoryRepository.findByUserId(user.getId());

        // Проверки
        Assertions.assertNotNull(found);
        Assertions.assertEquals(1, found.size());
        Assertions.assertEquals(5, found.get(0).getQuantity());
    }

    /**
     * Тест для метода findByUserIdAndFlossId
     */
    @Test
    void testFindByUserIdAndFlossId() {
        // Создаем пользователя
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setRole("USER");
        userRepository.save(user);

        // Создаем нитку
        Floss floss = new Floss();
        floss.setBrand("DMC");
        floss.setColorNumber("310");
        floss.setColorName("Black");
        floss.setRed(0);
        floss.setGreen(0);
        floss.setBlue(0);
        flossRepository.save(floss);

        // Создаем запись в инвентаре
        Inventory inventory = new Inventory();
        inventory.setUser(user);
        inventory.setFloss(floss);
        inventory.setQuantity(5);
        inventoryRepository.save(inventory);

        // Поиск по userId и flossId
        Optional<Inventory> found = inventoryRepository.findByUserIdAndFlossId(
                user.getId(), floss.getId());

        // Проверки
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(5, found.get().getQuantity());
    }
}
