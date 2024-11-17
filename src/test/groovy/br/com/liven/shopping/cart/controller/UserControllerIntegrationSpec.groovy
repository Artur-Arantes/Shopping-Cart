package br.com.liven.shopping.cart.controller

import br.com.liven.shopping.cart.BaseIntegrationSpec
import br.com.liven.shopping.cart.dto.UserDeleteInPutDto
import br.com.liven.shopping.cart.dto.UserInPutDto
import br.com.liven.shopping.cart.dto.UserOutPutDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.http.HttpStatus

import static org.assertj.core.api.Assertions.assertThat

class UserControllerIntegrationSpec extends BaseIntegrationSpec {

    def validToken

    def setup() {
        token = getToken()
        this.validToken = token
    }


    def "should add user"() {
        given: "valid form"
        def loginDto = new UserInPutDto("liven", "liven1@teste.com", "liven")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("${urlBase}/v1/user")

        then: "the response status is OK"
        response.statusCode == HttpStatus.OK.value()
    }


    def "should not add user because of invalid email"() {
        given: "a not valid form"
        def loginDto = new UserInPutDto("liven", "liven1@.com", "liven")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("${urlBase}/v1/user")

        then: "the response status is forbidden"
        response.statusCode == HttpStatus.FORBIDDEN.value()
    }


    def "should not add user because of duplicated account"() {
        given: "a valid form with duplicated email"
        def loginDto = new UserInPutDto("liven", "liven@teste.com", "liven")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("${urlBase}/v1/user")

        then: "the response status is Conflict"
        response.statusCode == HttpStatus.CONFLICT.value()
    }


    def "should return an error on get"() {

        given: "a valid email that not exists"
        def email = "liven2@teste.com"

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .param("id", email)
                .when()
                .get("${urlBase}/v1/user")

        then: "the response is not found"
        response.statusCode() == HttpStatus.NOT_FOUND.value()
    }


    def "should return a user on get"() {
        def validOutPut = new UserOutPutDto(1L, "liven@teste.com", true)

        given: "a valid email"
        def email = "liven@teste.com"

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .param("id", email)
                .when()
                .get("${urlBase}/v1/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath()
                .getObject("", UserOutPutDto.class);

        then: "the response is equal to what as expected"
        assertThat(response).isEqualTo(validOutPut);
    }

    def "should return an error on delete"() {
        given: "a valid input that not exists"
        def input = new UserDeleteInPutDto(20, "liven20@teste.com")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .delete("${urlBase}/v1/user")

        then: "the response is not found"
        response.statusCode() == HttpStatus.NOT_FOUND.value()
    }

}
