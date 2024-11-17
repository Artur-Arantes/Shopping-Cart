package br.com.liven.shopping.cart.controller

import br.com.liven.shopping.cart.BaseIntegrationSpec
import br.com.liven.shopping.cart.dto.ProductInPutDto
import br.com.liven.shopping.cart.dto.ProductOutPutDto
import br.com.liven.shopping.cart.dto.UpdateProductIntPutDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import org.springframework.http.HttpStatus

class ProductControllerIntegrationSpec extends BaseIntegrationSpec {

    def validToken

    def setup() {
        token = getToken()
        this.validToken = token
    }


    def "should add a product with success"() {
        given: "valid form of a product"
        def productInPut = new ProductInPutDto(33333333333, new BigDecimal("30.00"),
                "dsc", new BigDecimal("30.00"), "livenProduct")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(productInPut)
                .when()
                .post("${urlBase}/v1/products")

        then: "the response status is OK"
        response.statusCode == HttpStatus.OK.value()
    }


    def "should return an error duplicated sku"() {
        given: "valid form with sku that already exists on DB"
        def productInPut = new ProductInPutDto(11111111111, BigDecimal.ONE,
                "dsc", BigDecimal.TEN, "livenProduct")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(productInPut)
                .when()
                .post("${urlBase}/v1/products")

        then: "the response status is Conflict"
        response.statusCode == HttpStatus.CONFLICT.value()
    }


    def "should update a product with success"() {
        given: "valid form of a product"
        def productInPut = new UpdateProductIntPutDto(11111111111, BigDecimal.ONE,
                "dsc", BigDecimal.TEN, "livenProduct")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(productInPut)
                .when()
                .patch("${urlBase}/v1/products")

        then: "the response status is OK"
        response.statusCode == HttpStatus.OK.value()
    }


    def "should return an error because sku must be 11 digits"() {
        given: "form of a product that own sku with less than 11 digits"
        def productInPut = new UpdateProductIntPutDto(111111111, BigDecimal.ONE,
                "dsc", BigDecimal.TEN, "livenProduct")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(productInPut)
                .when()
                .patch("${urlBase}/v1/products")

        then: "the response status is Forbidden"
        response.statusCode == HttpStatus.FORBIDDEN.value()
    }


    def "should return an error because sku does not exists"() {
        given: "form of a product with sku that not exist on DB"
        def productInPut = new UpdateProductIntPutDto(55555555555, BigDecimal.ONE,
                "dsc", BigDecimal.TEN, "livenProduct")

        when: "authenticating with the API"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .body(productInPut)
                .when()
                .patch("${urlBase}/v1/products")

        then: "the response status is Not Found"
        response.statusCode == HttpStatus.NOT_FOUND.value()
    }


    def "should retrieve paginated products filtered by sku criteria"() {
        given: "a set of search criteria and pagination parameters"
        def searchCriteria = [
                sku     : 11111111111,
                name    : "",
                minPrice: 0.00,
                maxPrice: 0.00,
                inStock : true
        ]
        def page = 0
        def size = 5

        when: "requesting the paginated list of products"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .queryParams(searchCriteria + [page: page, size: size])
                .when()
                .get("${urlBase}/v1/products")
                .then()
                .log().body()
                .extract()
                .response()

        then: "the response status is OK and a valid page of products is returned"
        response.statusCode == HttpStatus.OK.value()

        then: "the response contains a paginated list of products"
        JsonPath jsonPath = response.jsonPath()

        def content = jsonPath.getList("content", ProductOutPutDto)
        content != null
        content.size() <= size

        and: "the products match the search criteria"
        content.every {
            it.sku == searchCriteria.sku
        }
    }

    def "should retrieve paginated products filtered by range price criteria "() {
        given: "a set of search criteria and pagination parameters"
        def searchCriteria = [
                sku     : 0,
                name    : "",
                minPrice: 1.00,
                maxPrice: 19.99,
                inStock : true
        ]
        def page = 0
        def size = 5

        when: "requesting the paginated list of products"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .queryParams(searchCriteria + [page: page, size: size])
                .when()
                .get("${urlBase}/v1/products")
                .then()
                .log().body()
                .extract()
                .response()

        then: "the response status is OK and a valid page of products is returned"
        response.statusCode == HttpStatus.OK.value()

        then: "the response contains a paginated list of products"
        JsonPath jsonPath = response.jsonPath()

        def content = jsonPath.getList("content", ProductOutPutDto)
        content != null
        content.size() <= size

        and: "the products match the search criteria"
        content.every {
            it.sku == 11111111111
        }
    }


    def "should retrieve paginated product filtered by range price criteria "() {
        given: "a set of search criteria and pagination parameters"
        def searchCriteria = [
                sku     : 0,
                name    : "",
                minPrice: 19.99,
                maxPrice: 20.01,
                inStock : true
        ]
        def page = 0
        def size = 5

        when: "requesting the paginated list of products"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .queryParams(searchCriteria + [page: page, size: size])
                .when()
                .get("${urlBase}/v1/products")
                .then()
                .log().body()
                .extract()
                .response()

        then: "the response status is OK and a valid page of products is returned"
        response.statusCode == HttpStatus.OK.value()

        then: "the response contains a paginated list of products"
        JsonPath jsonPath = response.jsonPath()

        def content = jsonPath.getList("content", ProductOutPutDto)
        content != null
        content.size() <= size

        and: "the products match the search criteria"
        content.every {
            it.sku == 22222222222
        }
    }


    def "should retrieve paginated products filtered by name criteria "() {
        given: "a set of search criteria and pagination parameters"
        def searchCriteria = [
                sku     : 0,
                name    : "product1",
                minPrice: 0.00,
                maxPrice: 0.00,
                inStock : true
        ]
        def page = 0
        def size = 5

        when: "requesting the paginated list of products"
        def response = RestAssured.given()
                .header("Authorization", "Bearer " + validToken)
                .contentType(ContentType.JSON)
                .queryParams(searchCriteria + [page: page, size: size])
                .when()
                .get("${urlBase}/v1/products")
                .then()
                .log().body()
                .extract()
                .response()

        then: "the response status is OK and a valid page of products is returned"
        response.statusCode == HttpStatus.OK.value()

        then: "the response contains a paginated list of products"
        JsonPath jsonPath = response.jsonPath()

        def content = jsonPath.getList("content", ProductOutPutDto)
        content != null
        content.size() <= size

        and: "the products match the search criteria"
        content.every {
            it.name == searchCriteria.name
        }
    }
}
