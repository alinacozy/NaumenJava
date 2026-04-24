package ru.alinacozy.NauJava.rest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProjectControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    // -------------------- /custom/projects/findByUserIdAndProjectName --------------------

    /**
     * Тест, покрывающий позитивный сценарий поиска по id пользователя и названию проекта
     */
    @Test
    void findByUserIdAndProjectName_positive() {
        given()
                .queryParam("userId", 1L)
                .queryParam("projectName", "Шотландка")
                .when()
                .get("/custom/projects/findByUserIdAndProjectName")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].projectName", notNullValue());
    }

    /**
     * Тест, покрывающий позитивный сценарий поиска по id пользователя и названию проекта
     * Сценарий: название проекта содержит конкретную подстроку
     */
    @Test
    void findByUserIdAndProjectName_partialMatch() {
        given()
                .queryParam("userId", 1L)
                .queryParam("projectName", "шот")
                .when()
                .get("/custom/projects/findByUserIdAndProjectName")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    /**
     * Тест, покрывающий негативный сценарий поиска по id пользователя и названию проекта
     * (id несуществующего пользователя)
     * Должен вернуться пустой список
     */
    @Test
    void findByUserIdAndProjectName_emptyByUserId() {
        given()
                .queryParam("userId", 999L)  // нет такого userId
                .queryParam("projectName", "Шотландка")
                .when()
                .get("/custom/projects/findByUserIdAndProjectName")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("isEmpty()", equalTo(true));
    }

    /**
     * Тест, покрывающий негативный сценарий поиска по id пользователя и названию проекта
     * (проект с несуществующим именем)
     * Должен вернуться пустой список
     */
    @Test
    void findByUserIdAndProjectName_nonexistentProjectName() {
        given()
                .queryParam("userId", 1L)
                .queryParam("projectName", "NonexistentProjectName")
                .when()
                .get("/custom/projects/findByUserIdAndProjectName")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("isEmpty()", equalTo(true));
    }

    /**
     * Тест, покрывающий негативный сценарий поиска по id пользователя и названию проекта
     * (проект с пустым именем)
     * Должен вернуться пустой список
     */
    @Test
    void findByUserIdAndProjectName_emptyProjectName() {
        given()
                .queryParam("userId", 1L)
                .queryParam("projectName", "")
                .when()
                .get("/custom/projects/findByUserIdAndProjectName")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("isEmpty()", equalTo(true));
    }

    // -------------------- /custom/projects/findByRequiredFlossBrand --------------------

    /**
     * Тест, покрывающий позитивный сценарий поиска по бренду required floss
     */
    @Test
    void findByRequiredFlossBrand_positive() {
        given()
                .queryParam("brand", "DMC")
                .when()
                .get("/custom/projects/findByRequiredFlossBrand")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    /**
     * Тест, покрывающий негативный сценарий поиска по бренду required floss
     * (ищем по несуществующему бренду)
     * Должен вернуться пустой список
     */
    @Test
    void findByRequiredFlossBrand_nonexistentBrand() {
        given()
                .queryParam("brand", "NonexistentBrand")
                .when()
                .get("/custom/projects/findByRequiredFlossBrand")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("isEmpty()", equalTo(true));
    }

    /**
     * Тест, покрывающий негативный сценарий поиска по бренду required floss
     * (ищем по пустому бренду)
     * Должен вернуться пустой список
     */
    @Test
    void findByRequiredFlossBrand_emptyBrand() {
        given()
                .queryParam("brand", "")
                .when()
                .get("/custom/projects/findByRequiredFlossBrand")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("isEmpty()", equalTo(true));
    }

}