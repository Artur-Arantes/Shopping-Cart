package br.com.liven.shopping.cart.service

import br.com.liven.shopping.cart.domain.*
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto
import br.com.liven.shopping.cart.repository.OrderRepository
import org.springframework.dao.DuplicateKeyException
import spock.lang.Specification

class CheckoutServiceSpec extends Specification {

    def orderRepository = Mock(OrderRepository)
    def productService = Mock(ProductService)
    def checkoutService = new CheckoutService(orderRepository, productService)

    def "should checkout successfully"() {
        given: "a cart with products"
        def user = new User(id: 1L, person: new Person(email: "test@example.com"))
        def product1 = new Product(sku: 1L, name: "Product 1", price: BigDecimal.TEN, inventory: new Inventory(quantity: BigDecimal.TEN))
        def product2 = new Product(sku: 2L, name: "Product 2", price: BigDecimal.valueOf(20), inventory: new Inventory(quantity: BigDecimal.TEN))

        def productCart1 = new ProductCart(id: new ProductCartId(1L, product1.sku), quantity: BigDecimal.ONE)
        def productCart2 = new ProductCart(id: new ProductCartId(1L, product2.sku), quantity: BigDecimal.valueOf(2))

        def list = List.of(productCart1, productCart2)

        def cart = new Cart(1L, user, list)

        productCart1.cart = cart
        productCart2.cart = cart
        productCart1.product = product1
        productCart2.product = product2
        productCart1.totalItem = BigDecimal.TEN
        productCart2.totalItem = new BigDecimal("40")

        and: "the repository has no existing order for the cart"
        orderRepository.findByCart(cart) >> Optional.empty()

        and: "products exist and have sufficient stock"
        productService.getProductFromId(product1.sku) >> product1
        productService.getProductFromId(product2.sku) >> product2

        and: "productService.hasStock does not throw an exception"
        productService.hasStock(_, _) >> { }

        when: "checkout is called"
        def output = checkoutService.checkout(cart)

        then: "the products' inventory is updated"
        1 * productService.updateProduct({ it.sku == product1.sku && it.inventory.quantity == BigDecimal.valueOf(9) })
        1 * productService.updateProduct({ it.sku == product2.sku && it.inventory.quantity == BigDecimal.valueOf(8) })

        and: "an order is saved"
        1 * orderRepository.saveAndFlush(_) >> { Order order ->
            assert order.amount == BigDecimal.valueOf(50)
            assert order.products.size() == 2
            return order
        }

        and: "a valid output is returned"
        output instanceof OrderCheckoutOutPutDto
    }


    def "should throw exception when an order already exists for the cart"() {
        given: "a cart with products"
        def cart = new Cart(id: 1L)

        and: "an order already exists for the cart"
        orderRepository.findByCart(cart) >> Optional.of(new Order())

        when: "checkout is called"
        checkoutService.checkout(cart)

        then: "a DuplicateKeyException is thrown"
        def e = thrown(DuplicateKeyException)
        e.message == "Already exist a order for this cart"
    }

    def "should throw exception when a product has insufficient stock"() {
        given: "a cart with products"
        def inventory = new Inventory(quantity: BigDecimal.ONE)
        def product = new Product(12345678911L, "Product Name", BigDecimal.TEN, "Description", inventory)
        product.inventory.product = product
        def productCart = new ProductCart(id: new ProductCartId(1L,product.sku), quantity: BigDecimal.TEN)
        def cart = new Cart(id: 1L, products: [productCart] as List)

        and: "the product exists but has insufficient stock"
        productService.getProductFromId(product.sku) >> product
        productService.hasStock(_, _) >> { throw new IllegalArgumentException("Insufficient stock") }

        and: "the repository has no existing order for the cart"
        orderRepository.findByCart(cart) >> Optional.empty()

        when: "checkout is called"
        checkoutService.checkout(cart)

        then: "an IllegalArgumentException is thrown"
        def e = thrown(IllegalArgumentException)
        e.message == "Insufficient stock"
    }
}
