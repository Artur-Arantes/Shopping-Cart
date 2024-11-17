package br.com.liven.shopping.cart

import br.com.liven.shopping.cart.dto.LoginInPutDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Stepwise
@ContextConfiguration
class BaseIntegrationSpec extends Specification {

    @Shared
    static ShoppingCartDataBaseContainer shoppingCartDataBase = ShoppingCartDataBaseContainer.getInstance()

    @LocalServerPort
    protected int port

    protected String urlBase

    protected token

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        shoppingCartDataBase.start()
    }

    def setup() {
        this.urlBase = "http://localhost:${port}"
    }


    protected String getToken() {
        def loginDto = new LoginInPutDto("liven@teste.com", "liven")

        def response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("${urlBase}/auth")

        if (response.statusCode == HttpStatus.OK.value()) {
            return response.jsonPath().getString("token")
        } else {
            throw new RuntimeException("Failed to obtain token: HTTP ${response.statusCode}")
        }
    }

}

