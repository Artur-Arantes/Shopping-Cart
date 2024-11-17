package br.com.liven.shopping.cart.service

import br.com.liven.shopping.cart.domain.*
import br.com.liven.shopping.cart.dto.*
import br.com.liven.shopping.cart.exception.ObjectNotFoundException
import br.com.liven.shopping.cart.repository.CartRepository
import jakarta.persistence.OptimisticLockException
import jakarta.validation.ValidationException
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
class CartServiceSpec extends Specification {

    CartRepository repository = Mock()
    UserService userService = Mock()
    ProductService productService = Mock()
    CartStateService cartStateService = Mock()
    CheckoutService checkoutService = Mock()

    @Subject
    CartService cartService = new CartService(repository, userService, productService, cartStateService, checkoutService)


    def "should throw ObjectNotFoundException when cart ID is invalid"() {
        given:
        repository.findById(1L) >> Optional.empty()

        when:
        cartService.getCartById(1L, "test@example.com")

        then:
        thrown(ObjectNotFoundException)
    }

    def "should update cart with valid products"() {
        def inventory = new Inventory(quantity: BigDecimal.TEN)
        def product = new Product(sku: 12345678911, price: BigDecimal.TEN, inventory: inventory)
        inventory.product = product
        given:
        def cart = new Cart(id: 1L, user: new User(person: new Person(email: "test@example.com")), products :[])
        repository.findById(1L) >> Optional.of(cart)
        cartStateService.isCartExpired(_) >> false
        productService.getProductFromId(12345678911) >> { Long sku ->
            assert sku == 12345678911
            product
        }
        productService.hasStock(_, _) >> { BigDecimal quantity, Product prod ->
            assert quantity == BigDecimal.ONE
            assert prod == product
            true
        }


        def updateInput = new UpdateCartInPutDto([new UpdateProductCartInPutDto(12345678911, BigDecimal.ONE)], 1L)


        when:
        cartService.update(updateInput, "test@example.com")

        then:
        noExceptionThrown()
        1 * repository.save(_)
    }

    def "should throw ValidationException for cart owned by another user"() {
        given:
        def cart = new Cart(id: 1L, user: new User(person: new Person(email: "other@example.com")))
        repository.findById(1L) >> Optional.of(cart)

        when:
        cartService.getCartById(1L, "test@example.com")

        then:
        thrown(ValidationException)
    }

    def "should process checkout for valid cart"() {
        given:
        def inventory = new Inventory(quantity: BigDecimal.TEN)
        def product = new Product(sku: 12345678911, price: BigDecimal.TEN, inventory: inventory)
        inventory.product = product
        def cart = new Cart(id: 1L, products:
                [new ProductCart(product: product, quantity: BigDecimal.ONE, totalItem: BigDecimal.TEN)
        ], user: new User(person: new Person(email: "test@example.com")))

        repository.findById(1L) >> Optional.of(cart)
        cartStateService.isCartExpired(_) >> false
        checkoutService.checkout(_) >> new OrderCheckoutOutPutDto(123,BigDecimal.TEN, [new ProductOutPutDto()] )

        def checkoutInput = new CheckoutInPutDto(1L)

        when:
        def result = cartService.processCheckout(checkoutInput, "test@example.com")

        then:
        result != null
        result.id == 123
    }

    def "should throw IllegalStateException for empty cart during checkout"() {
        given:
        def cart = new Cart(id: 1L, products: [], user: new User(person: new Person(email: "test@example.com")))
        repository.findById(1L) >> Optional.of(cart)
        cartStateService.isCartExpired(_) >> false

        def checkoutInput = new CheckoutInPutDto(1L)

        when:
        cartService.processCheckout(checkoutInput, "test@example.com")

        then:
        thrown(IllegalStateException)
    }

    def "should handle OptimisticLockException during checkout"() {
        given:
        def inventory = new Inventory(quantity: BigDecimal.TEN)
        def product = new Product(sku: 12345678911, price: BigDecimal.TEN, inventory: inventory)
        inventory.product = product
        def cart = new Cart(id: 1L, products: [
                new ProductCart(product: product, quantity: BigDecimal.ONE, totalItem: BigDecimal.TEN)
        ], user: new User(person: new Person(email: "test@example.com")))

        repository.findById(1L) >> Optional.of(cart)
        cartStateService.isCartExpired(_) >> false
        checkoutService.checkout(_) >> { throw new OptimisticLockException() }

        def checkoutInput = new CheckoutInPutDto(1L)

        when:
        cartService.processCheckout(checkoutInput, "test@example.com")

        then:
        thrown(ValidationException)
    }
}
