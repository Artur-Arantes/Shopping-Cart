package br.com.liven.shopping.cart.service

import br.com.liven.shopping.cart.domain.Inventory
import br.com.liven.shopping.cart.domain.Product
import br.com.liven.shopping.cart.dto.ProductInPutDto
import br.com.liven.shopping.cart.dto.ProductSearchCriteria
import br.com.liven.shopping.cart.dto.UpdateProductIntPutDto
import br.com.liven.shopping.cart.exception.NegativeQuantityException
import br.com.liven.shopping.cart.exception.ObjectNotFoundException
import br.com.liven.shopping.cart.repository.InventoryRepository
import br.com.liven.shopping.cart.repository.ProductRepository
import br.com.liven.shopping.cart.utils.NumberUtil
import br.com.liven.shopping.cart.utils.ProductSpecifications
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class ProductServiceSpec extends Specification {

    @Subject
    ProductService productService

    ProductRepository productRepository = Mock(ProductRepository)
    InventoryRepository inventoryRepository = Mock(InventoryRepository)
    NumberUtil numberUtil = Mock(NumberUtil)
    ProductSpecifications productSpecifications = Mock(ProductSpecifications)

    def setup() {
        productService = new ProductService(productRepository, inventoryRepository)
    }

    def "should add product successfully"() {
        given: "a product to be added"
        def productInPutDto = new ProductInPutDto(12345678911L, BigDecimal.TEN, "Product Name", BigDecimal.TEN, "Description")

        and: "the product does not exist in the repository"
        productRepository.existsById(productInPutDto.sku()) >> false

        when: "the addProduct method is called"
        productService.addProduct(productInPutDto)

        then: "the product is saved"
        1 * productRepository.save(_ as Product)
    }

    def "should throw DuplicateKeyException when product exists"() {
        given: "a product to be added"
        def productInPutDto = new ProductInPutDto(12345678911L, BigDecimal.TEN, "Product Name", BigDecimal.TEN, "Description")

        and: "the product already exists in the repository"
        productRepository.existsById(productInPutDto.sku()) >> true

        when: "the addProduct method is called"
        productService.addProduct(productInPutDto)

        then: "a DuplicateKeyException is thrown"
        thrown(DuplicateKeyException)
    }

    def "should update product successfully"() {
        given: "an existing product and an update DTO"
        def inventory = new Inventory(quantity: BigDecimal.TEN)
        def updateProductIntPutDto = new UpdateProductIntPutDto(12345678911L, BigDecimal.TEN, "Updated Description", BigDecimal.TEN, "name")
        def existingProduct = new Product(12345678911L, "Product Name", BigDecimal.TEN, "Description", inventory)
        existingProduct.inventory.product = existingProduct

        and: "the product exists in the repository"
        productRepository.findById(updateProductIntPutDto.sku()) >> Optional.of(existingProduct)

        when: "the update method is called"
        productService.update(updateProductIntPutDto)

        then: "the product is updated"
        notThrown(IllegalArgumentException)

    }

    def "should throw ObjectNotFoundException when updating non-existent product"() {
        given: "an update DTO"
        def updateProductIntPutDto = new UpdateProductIntPutDto(12345678911L, BigDecimal.TEN, "Updated Description", BigDecimal.TEN, "name")

        and: "the product does not exist in the repository"
        productRepository.findById(updateProductIntPutDto.sku()) >> Optional.empty()

        when: "the update method is called"
        productService.update(updateProductIntPutDto)

        then: "Exception throw"
        thrown(ObjectNotFoundException)
    }


    def "should throw NegativeQuantityException when stock is negative"() {
        given: "an existing product and an update DTO with negative stock"
        def inventory = new Inventory(quantity: BigDecimal.ONE)
        def updateProductIntPutDto = new UpdateProductIntPutDto(12345678911L, new BigDecimal(-10), "Updated Description", BigDecimal.TEN, "name")
        def existingProduct = new Product(12345678911L, "Product Name", BigDecimal.TEN, "Description", inventory)
        existingProduct.inventory.product = existingProduct

        and: "the product exists in the repository"
        productRepository.findById(updateProductIntPutDto.sku()) >> Optional.of(existingProduct)

        when: "the update method is called"
        productService.update(updateProductIntPutDto)

        then: "a NegativeQuantityException is thrown"
        thrown(NegativeQuantityException)
    }

    def "should return filtered products"() {
        given: "search criteria with a price range"
        def searchCriteria = new ProductSearchCriteria(12345678911L, "", BigDecimal.ONE, BigDecimal.TEN, true)
        final var pageable = PageRequest.of(0, 10);

        and: "the repository returns an empty page"
        def emptyPage = new PageImpl<>([], pageable, 0L)
        productRepository.findAll(_, _) >> emptyPage

        when: "the getFilteredProducts method is called"
        def result = productService.getFilteredProducts(searchCriteria, pageable)

        then: "an empty page of products is returned"
        result.content.isEmpty()
    }

    def "should get product by id successfully"() {
        given: "an existing product"
        def inventory = new Inventory(quantity: BigDecimal.ONE)
        def product = new Product(12345678911L, "Product Name", BigDecimal.TEN, "Description", inventory)
        product.inventory.product = product

        and: "the product exists in the repository"
        productRepository.findById(12345678911L) >> Optional.of(product)

        when: "the getProductFromId method is called"
        def result = productService.getProductFromId(12345678911L)

        then: "the correct product is returned"
        result.sku == 12345678911L
    }

    def "should throw ObjectNotFoundException when getting non-existent product"() {
        given: "a non-existent product"
        productRepository.findById(12345678911L) >> Optional.empty()

        when: "the getProductFromId method is called"
        productService.getProductFromId(12345678911L)

        then: "an ObjectNotFoundException is thrown"
        thrown(ObjectNotFoundException)
    }

    def "should throw NegativeQuantityException when insufficient stock"() {
        given: "a product with insufficient stock"
        def inventory = new Inventory(quantity: BigDecimal.ONE)
        def product = new Product(12345678911L, "Product Name", BigDecimal.TEN, "Description", inventory)
        product.inventory.product = product
        when: "the hasStock method is called with a quantity larger than the stock"
        productService.hasStock(new BigDecimal(101), product)

        then: "a NegativeQuantityException is thrown"
        thrown(NegativeQuantityException)
    }
}
