package br.com.liven.shopping.cart.controller

import br.com.liven.shopping.cart.BaseIntegrationSpec
import br.com.liven.shopping.cart.dto.*
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.http.HttpStatus

import static org.assertj.core.api.Assertions.assertThat

class CarControllerIntegrationSpec extends BaseIntegrationSpec {

    def validToken

    def setup() {
        token = getToken()
        this.validToken = token
    }

    def "should create a cart with success"() {
        def expectedReturn = new CreateCartOutPutDto(2L, [], BigDecimal.ZERO);
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .when()
                .post("${urlBase}/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getObject("", CreateCartOutPutDto.class);

        then: "the out put match with the expected object "
        assertThat(response).isEqualTo(expectedReturn)
    }


    def "should return an error because cart does not exists"() {
        def productCart = new UpdateProductCartInPutDto(11111111111, BigDecimal.TEN)
        def input = new UpdateCartInPutDto(List.of(productCart), 55L);
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .put("${urlBase}/v1/cart")

        then: "the response status is Not Found"
        response.statusCode() == HttpStatus.NOT_FOUND.value()
    }

    def "should get a cart by ID with success"() {
        def expectedReturn = 1L
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .param("id", "1")
                .when()
                .get("${urlBase}/v1/cart")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getObject("", GetCartByIdOutPutDto.class);

        then: "the out put match with the expected object "
        assertThat(response.id()).isEqualTo(expectedReturn)
    }


    def "should return an error because cart ID does not exists"() {
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .param("id", "99")
                .when()
                .get("${urlBase}/v1/cart")

        then: "the response status is Not Found"
        response.statusCode() == HttpStatus.NOT_FOUND.value()
    }


    def "should do the checkout with success"() {
        def expectedReturn = 1L
        def inPut = new CheckoutInPutDto(1L)
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(inPut)
                .when()
                .post("${urlBase}/v1/cart/checkout")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getObject("", OrderCheckoutOutPutDto.class);

        then: "the out put match with the expected object "
        assertThat(response.getId()).isEqualTo(expectedReturn)
    }


    def "should return an error on checkout because cart ID does not exists"() {
        def inPut = new CheckoutInPutDto(55L)
        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(inPut)
                .when()
                .post("${urlBase}/v1/cart/checkout")

        then: "the response status is Not Found"
        response.statusCode() == HttpStatus.NOT_FOUND.value()
    }

}
