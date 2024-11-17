package br.com.liven.shopping.cart.service

import br.com.liven.shopping.cart.domain.Person
import br.com.liven.shopping.cart.domain.User
import spock.lang.Specification

class TokenServiceSpec extends Specification {

    def tokenService = new TokenService()

    def setup() {
        tokenService.secretKey = "testSecretKey"
    }

    def "should generate a valid token for a user"() {
        given: "a valid user"
        def person = new Person(email: "test@example.com")
        def user = new User(person: person)

        when: "the token is generated"
        def token = tokenService.tokenGenerate(user)

        then: "the token is not null and contains the user's email"
        token != null
        tokenService.verifyToken(token) == "test@example.com"
    }

    def "should throw exception when token generation fails"() {
        given: "a token service with an invalid secret key"
        tokenService.secretKey = null

        when: "token generation is attempted"
        tokenService.tokenGenerate(new User(person: new Person(email: "test@example.com")))

        then: "a IllegalArgumentException is thrown"
        thrown(IllegalArgumentException)
    }

    def "should verify a valid token successfully"() {
        given: "a valid token"
        def person = new Person(email: "test@example.com")
        def user = new User(person: person)
        def token = tokenService.tokenGenerate(user)

        when: "the token is verified"
        def subject = tokenService.verifyToken(token)

        then: "the returned subject matches the user's email"
        subject == "test@example.com"
    }

    def "should return empty string for invalid token"() {
        given: "an invalid token"
        def invalidToken = "invalid.token.here"

        when: "the token is verified"
        def subject = tokenService.verifyToken(invalidToken)

        then: "an empty string is returned"
        subject == ""
    }

    def "should extract token from valid authorization header"() {
        given: "a valid authorization header"
        def authorizationHeader = "Bearer validToken"

        when: "the token is extracted"
        def extractedToken = tokenService.extractToken(authorizationHeader)

        then: "the extracted token matches the token in the header"
        extractedToken == "validToken"
    }

    def "should throw exception for invalid authorization header"() {
        given: "an invalid authorization header"
        def invalidHeader = "InvalidHeader"

        when: "token extraction is attempted"
        tokenService.extractToken(invalidHeader)

        then: "an IllegalArgumentException is thrown"
        def e = thrown(IllegalArgumentException)
        e.message == "Invalid token"
    }

    def "should extract email from authorization header"() {
        given: "a valid authorization header and user"
        def person = new Person(email: "test@example.com")
        def user = new User(person: person)
        def token = tokenService.tokenGenerate(user)
        def authorizationHeader = "Bearer ${token}"

        when: "email is extracted using the authorization header"
        def email = tokenService.getEmailByAuthorizationHeader(authorizationHeader)

        then: "the email matches the user's email"
        email == "test@example.com"
    }
}
