package org.tyaa.demo.spring.springreactmysqlmongo.test.application.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tyaa.demo.spring.springreactmysqlmongo.SpringReactMysqlMongoApplication;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ProductModel;
import org.tyaa.demo.spring.springreactmysqlmongo.models.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

// включение режима теста приложения с запуском на реальном веб-сервере
// и с доступом к контексту приложения
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringReactMysqlMongoApplication.class
)
// режим создания одиночного экземпляра класса тестов для всех кейсов
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// активация контейнера выполнения кейсов согласно пользовательской нумерации
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerRequestsTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    final String baseUrl = "/api";

    @Test
    @Order(1)
    public void givenNameAndQuantity_whenRequestsListOfORCLProducts_thenCorrect() throws Exception {
        // получение от REST API списка товаров, у которых название ORCL
        // и цена выше 1500
        ResponseEntity<ResponseModel> response =
            testRestTemplate.getForEntity(
            baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=name:ORCL;quantity>1500",
                ResponseModel.class
            );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
            (ArrayList) response.getBody().getData();
        assertNotNull(products);
        // такой товар должен быть найден толко один
        assertEquals(1, products.size());
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
            (new ObjectMapper())
                .convertValue(products, new TypeReference<List<ProductModel>>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            assertEquals("ORCL", product.getTitle());
            assertTrue(product.getQuantity() > 1500);
        });
    }

    @Test
    @Order(2)
    public void givenCategoryIdAndQuantity_whenRequestsListOfProducts_thenCorrect() throws Exception {
        // получение от REST API списка товаров, у которых id категории равен 1 или 2
        // и цена ниже 2000
        ResponseEntity<ResponseModel> response =
            testRestTemplate.getForEntity(
                baseUrl + "/products/filtered::orderBy:id::sortingDirection:DESC/?search=category:[1,2];quantity<2000",
                ResponseModel.class
            );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ArrayList products =
            (ArrayList) response.getBody().getData();
        assertNotNull(products);
        // сложный тестовый веб-клиент testRestTemplate десериализует множество данных моделей
        // как список словарей, поэтому нужно явное преоразование в список моделей ProductModel
        List<ProductModel> productModels =
            (new ObjectMapper())
                .convertValue(products, new TypeReference<List<ProductModel>>() { });
        // у каждого найденного товара должны быть значения полей,
        // соотетствующие параметрам поискового запроса
        productModels.forEach(product -> {
            List<Long> categoryIds = Lists.newArrayList(1L, 2L);
            Matcher<Iterable<? super Long>> matcher = hasItem(product.getCategory().getId());
            assertThat(categoryIds, matcher);
        });
    }
}
