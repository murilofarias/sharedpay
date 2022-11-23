package br.com.murilofarias.sharedpay.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BillControllerIT {

    @LocalServerPort
    private int port;


    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
    }


    @Test
    @Sql("classpath:test-data.sql")
    @Sql(scripts = "classpath:clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerBill_WhenBodyIsValid_ShouldReturnStatusCode201AndBodyWithIdNotNull() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        String bodyTest = "{\n" +
                "    \"additionals\": 8.00,\n" +
                "    \"discounts\": 20.00,\n" +
                "    \"hasWaiterService\": false,\n" +
                "    \"includeOwnerPayment\": true," +
                "      \"ownerCpf\": \"61330346025\",\n" +
                "    \"individualSpendings\": [\n" +
                "      {\n" +
                "        \"value\": 42.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Eduardo\",\n" +
                "          \"lastName\": \"Monteiro\",\n" +
                "          \"cpf\": \"01942539290\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"value\": 8.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Alan\",\n" +
                "          \"lastName\": \"Santos\",\n" +
                "          \"cpf\": \"61330346025\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        given()
                .basePath("/bills")
                .body(bodyTest)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("id", Matchers.notNullValue())
                .statusCode(HttpStatus.CREATED.value());

    }

    @Test
    @Sql("classpath:test-data.sql")
    @Sql(scripts = "classpath:clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerBill_WhenBodyIncludeOwnerPaymentIsTrue_ShouldReturnPaymentsArrayEqualTo2() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        String bodyTest = "{\n" +
                "    \"additionals\": 8.00,\n" +
                "    \"discounts\": 20.00,\n" +
                "    \"hasWaiterService\": false,\n" +
                "    \"includeOwnerPayment\": true," +
                "      \"ownerCpf\": \"61330346025\",\n" +
                "    \"individualSpendings\": [\n" +
                "      {\n" +
                "        \"value\": 42.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Eduardo\",\n" +
                "          \"lastName\": \"Monteiro\",\n" +
                "          \"cpf\": \"01942539290\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"value\": 8.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Alan\",\n" +
                "          \"lastName\": \"Santos\",\n" +
                "          \"cpf\": \"61330346025\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        given()
                .basePath("/bills")
                .body(bodyTest)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("payments", Matchers.hasSize(2))
                .statusCode(HttpStatus.CREATED.value());

    }

    @Test
    @Sql("classpath:test-data.sql")
    @Sql(scripts = "classpath:clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerBill_WhenBodyIncludeOwnerPaymentIsTrue_ShouldReturnCorrectValuesForPayments() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        String bodyTest = "{\n" +
                "    \"additionals\": 8.00,\n" +
                "    \"discounts\": 20.00,\n" +
                "    \"hasWaiterService\": false,\n" +
                "    \"includeOwnerPayment\": true," +
                "      \"ownerCpf\": \"61330346025\",\n" +
                "    \"individualSpendings\": [\n" +
                "      {\n" +
                "        \"value\": 42.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Eduardo\",\n" +
                "          \"lastName\": \"Monteiro\",\n" +
                "          \"cpf\": \"01942539290\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"value\": 8.00,\n" +
                "        \"person\": {\n" +
                "          \"firstName\": \"Alan\",\n" +
                "          \"lastName\": \"Santos\",\n" +
                "          \"cpf\": \"61330346025\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        given()
                .basePath("/bills")
                .body(bodyTest)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .body("payments.value",   hasItems(31.92f, 6.08f))
                .statusCode(HttpStatus.CREATED.value());

    }

    @Test
    @Sql("classpath:test-data.sql")
    @Sql(scripts = "classpath:clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBillPayments_whenBillIsInDB_ShouldReturnStatusCode200() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        given()
                .basePath("/bills/245/payments")
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @Sql("classpath:test-data.sql")
    @Sql(scripts = "classpath:clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBillPayments_whenBillIsNotInDB_ShouldReturnStatusCode404() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        given()
                .basePath("/bills/244/payments")
                .port(port)
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void requestPayments() {
    }
}