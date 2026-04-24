package ru.alinacozy.NauJava.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAndLogoutTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(new ChromeOptions());
        //явное ожидание
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Тест, покрывающий позитивный сценарий успешного входа с правильными учетными данными
     */
    @Test
    void loginWithValidCredentials() {
        String baseUrl = "http://localhost:" + port;
        // выполняем вход
        performLogin(baseUrl, "john_doe", "123");
    }


    /**
     * Тест, покрывающий позитивный сценарий успешного выхода из приложения
     */
    @Test
    void logoutSuccessfully() {
        String baseUrl = "http://localhost:" + port;

        // выполняем вход
        performLogin(baseUrl, "john_doe", "123");

        // выполняем выход и проверяем результат
        performLogout();
    }

    /**
     * Метод для осуществления входа в систему
     * @param baseUrl базовый URL
     * @param username имя пользователя
     * @param password пароль
     */
    private void performLogin(String baseUrl, String username, String password) {
        driver.get(baseUrl + "/login");

        String beforeLoginUrl = driver.getCurrentUrl();

        try {
            WebElement usernameField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.name("username"))
            );
            WebElement passwordField = driver.findElement(By.name("password"));

            usernameField.sendKeys(username);
            passwordField.sendKeys(password);

            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
            loginButton.click();

            // явно ждем изменения URL
            wait.until(webDriver -> !webDriver.getCurrentUrl().equals(beforeLoginUrl));

            // ждем загрузки домашней страницы
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".floss-list, .table, .card")
            ));

            // проверяем успешность входа
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("/custom/floss/view/list"),
                    "После входа должны быть на странице списка ниток, URL: " + currentUrl);

        } catch (TimeoutException e) {
            fail("Не удалось выполнить вход (таймаут 5 секунд). Текущий URL: " + driver.getCurrentUrl());
        }
    }

    /**
     * Метод выхода из системы
     */
    private void performLogout() {
        String beforeLogoutUrl = driver.getCurrentUrl();

        try {
            // находим и нажимаем кнопку выхода
            WebElement logoutButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-danger"))
            );
            logoutButton.click();

            // ждем изменения URL
            wait.until(webDriver -> !webDriver.getCurrentUrl().equals(beforeLogoutUrl));

            // проверяем URL после выхода
            String currentUrl = driver.getCurrentUrl();
            assertAll(
                    () -> assertTrue(currentUrl.contains("/login"),
                            "После выхода должны быть на странице логина, URL: " + currentUrl),
                    () -> assertTrue(currentUrl.contains("logout"),
                            "URL после выхода должен содержать параметр logout, URL: " + currentUrl)
            );

            // Проверяем сообщение об успешном выходе
            WebElement logoutMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.alert.alert-info")
                    )
            );

            String messageText = logoutMessage.getText();
            assertTrue(
                    messageText.contains("успешно вышли из системы") ||
                            messageText.contains("successfully logged out"),
                    "Сообщение о выходе отсутствует или некорректно. Текст: " + messageText
            );

            // Проверяем, что снова можно войти (поля ввода доступны)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("password")));

        } catch (TimeoutException e) {
            fail("Не удалось выполнить выход. Текущий URL: " + driver.getCurrentUrl() +
                    ", Текущий заголовок: " + driver.getTitle());
        }
    }
}