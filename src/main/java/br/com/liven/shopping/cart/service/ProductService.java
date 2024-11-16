package br.com.liven.shopping.cart.service;


import br.com.liven.shopping.cart.domain.Product;
import br.com.liven.shopping.cart.dto.ProductInPutDto;
import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import br.com.liven.shopping.cart.dto.ProductSearchCriteria;
import br.com.liven.shopping.cart.dto.UpdateProductIntPutDto;
import br.com.liven.shopping.cart.exception.NegativeQuantityException;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.InventoryRepository;
import br.com.liven.shopping.cart.repository.ProductRepository;
import br.com.liven.shopping.cart.utils.NumberUtil;
import br.com.liven.shopping.cart.utils.ProductSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final InventoryRepository inventoryRepository;

    public void addProduct(final ProductInPutDto productInPutDto) {
        final var product = productInPutDto.toProduct();
        if (!repository.existsById(product.getSku())) {
            repository.save(product);
            return;
        }
        throw new DuplicateKeyException("this Product already exist");
    }

    @Transactional
    public void update(final UpdateProductIntPutDto productInPutDto) {

        if (!NumberUtil.hasElevenDigits(productInPutDto.sku())) {
            throw new IllegalArgumentException("Invalid Sku");
        }

        final var product = repository.findById(productInPutDto.sku()).orElseThrow(() ->
                new ObjectNotFoundException("product do not exists"));

        final var productInPut = productInPutDto.toProduct();
        if (productInPut.getName() != null && !productInPut.getName().trim().isEmpty()) {
            product.setName(productInPut.getName());
        }
        if (productInPut.getInventory().getQuantity().compareTo(BigDecimal.ZERO) >= 0) {
            product.getInventory().setQuantity(productInPut.getInventory().getQuantity());
        } else {
            throw new NegativeQuantityException("Stock cant be negative");
        }
        if (productInPut.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(productInPut.getPrice());
        }
        if (productInPut.getDescription() != null && !productInPut.getDescription().trim().isEmpty()) {
            product.setDescription(productInPut.getDescription());
        }
    }

    public Page<ProductOutPutDto> getFilteredProducts(final ProductSearchCriteria searchCriteria,
                                                      final Pageable pageable) {

        Specification<Product> specs = Specification.where(ProductSpecifications.hasStock());

        specs = getProductSpecification(searchCriteria, specs);

        Page<Product> products = repository.findAll(specs, pageable);
        return products.map(Product::toOutPutDto);
    }

    private static Specification<Product> getProductSpecification(ProductSearchCriteria searchCriteria, Specification<Product> specs) {
        if (searchCriteria.getSku() != null && searchCriteria.getSku() > 0) {
            specs = specs.and(ProductSpecifications.skuEquals(searchCriteria.getSku()));
        }
        if (searchCriteria.getName() != null && !searchCriteria.getName().isBlank()) {
            specs = specs.and(ProductSpecifications.nameContains(searchCriteria.getName()));
        }
        if (searchCriteria.getMinPrice() != null && searchCriteria.getMaxPrice() != null &&
                searchCriteria.getMinPrice().compareTo(BigDecimal.ZERO) > 0 &&
                searchCriteria.getMaxPrice().compareTo(BigDecimal.ZERO) > 0) {
            specs = specs.and(ProductSpecifications.priceBetween(searchCriteria.getMinPrice(), searchCriteria.getMaxPrice()));
        }
        return specs;
    }

    public Product getProductFromId(long id) {
        return repository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Product with SKU " + id + " does not exist"));
    }

    public void hasStock(final BigDecimal quantity, final Product product) {
        final var inventoryUpdated = product.getInventory().getQuantity().subtract(quantity);

        if (inventoryUpdated.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeQuantityException("Insufficient stock for product SKU: " + product.getSku());
        }
    }

    @Transactional
    public void updateProduct(Product product) {
        repository.saveAndFlush(product);
    }
}
