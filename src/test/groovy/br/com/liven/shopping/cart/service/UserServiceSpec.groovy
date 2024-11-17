package br.com.liven.shopping.cart.service

import br.com.liven.shopping.cart.domain.Person
import br.com.liven.shopping.cart.domain.User
import br.com.liven.shopping.cart.dto.UserDeleteInPutDto
import br.com.liven.shopping.cart.dto.UserInPutDto
import br.com.liven.shopping.cart.dto.UserOutPutDto
import br.com.liven.shopping.cart.enums.EnumUserPermission
import br.com.liven.shopping.cart.exception.ObjectNotFoundException
import br.com.liven.shopping.cart.repository.PersonRepository
import br.com.liven.shopping.cart.repository.UserRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

class UserServiceSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def personRepository = Mock(PersonRepository)
    def passwordEncoder = new BCryptPasswordEncoder()

    @Subject
    def userService = new UserService(userRepository, personRepository)

    def "should successfully add a user"() {
        given: "a valid UserInPutDto"
        def userInputDto = new UserInPutDto("Test User", "test@example.com", "123456")
        def user = userInputDto.toUser()
        user.setPassword(passwordEncoder.encode("123456"))

        and: "person does not exist and no user with the same person exists"
        personRepository.existsById(userInputDto.email) >> false
        userRepository.findByPerson(_) >> Optional.empty()

        when: "the addUser method is called"
        userService.addUser(userInputDto)

        then: "the user is saved and permissions are assigned"
        1 * userRepository.save({ it.person.email == "test@example.com" })
        1 * userRepository.savePermissionForUser(_, EnumUserPermission.USER.code as Long)
    }

    def "should throw DuplicateKeyException if user already exists"() {
        given: "a UserInPutDto with an existing email"
        def userInputDto = new UserInPutDto("Test User", "test@example.com", "123456")
        def existingPerson = new Person(email: "test@example.com")

        and: "person already exists in the database"
        personRepository.existsById(userInputDto.email) >> true

        when: "the addUser method is called"
        userService.addUser(userInputDto)

        then: "a DuplicateKeyException is thrown"
        thrown(DuplicateKeyException)
    }

    def "should get user output by ID"() {
        given: "a valid user and person in the database"
        def person = new Person(email: "test@example.com")
        def user = new User(id: 1L, person: person, password: "encoded-password")

        personRepository.findById("test@example.com") >> Optional.of(person)
        userRepository.findByPerson(person) >> Optional.of(user)

        when: "the getUserOutPut method is called"
        def result = userService.getUserOutPut("test@example.com")

        then: "a UserOutPutDto is returned"
        result instanceof UserOutPutDto
        result.email == "test@example.com"
    }

    def "should throw ObjectNotFoundException if user is not found"() {
        given: "a person exists but the user does not"
        def person = new Person(email: "test@example.com")
        personRepository.findById("test@example.com") >> Optional.of(person)
        userRepository.findByPerson(person) >> Optional.empty()

        when: "the getUserOutPut method is called"
        userService.getUserOutPut("test@example.com")

        then: "an ObjectNotFoundException is thrown"
        thrown(ObjectNotFoundException)
    }

    def "should delete a user successfully"() {
        given: "a valid UserDeleteInPutDto with an existing user ID"
        def deleteInputDto = new UserDeleteInPutDto(1L, "")

        and: "user exists in the repository"
        userRepository.existsById(1L) >> true

        when: "the deleteUser method is called"
        userService.deleteUser(deleteInputDto)

        then: "the user is deleted"
        1 * userRepository.deleteById(1L)
    }

    def "should throw ObjectNotFoundException if user to delete does not exist"() {
        given: "a UserDeleteInPutDto with a non-existent user ID"
        def deleteInputDto = new UserDeleteInPutDto(1L, "")

        and: "user does not exist in the repository"
        userRepository.existsById(1L) >> false

        when: "the deleteUser method is called"
        userService.deleteUser(deleteInputDto)

        then: "an ObjectNotFoundException is thrown"
        thrown(ObjectNotFoundException)
    }
}


