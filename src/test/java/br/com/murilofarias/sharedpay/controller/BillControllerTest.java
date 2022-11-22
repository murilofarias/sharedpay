package br.com.murilofarias.sharedpay.controller;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;


@SpringBootTest
class BillControllerTest {

    @Test
    void registerBill() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .basePath("/bills")
            .port(8080)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());



    }

    @Test
    void getBillPayments() {
        enableLoggingOfRequestAndResponseIfValidationFails();

        given()
            .basePath("/bills//payments")
            .port(8080)
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void requestPayments() {
    }
}