package br.com.liven.shopping.cart.service;


import br.com.liven.shopping.cart.domain.Product;
import br.com.liven.shopping.cart.dto.ProductInPutDto;
import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import br.com.liven.shopping.cart.dto.ProductSearchCriteria;
import br.com.liven.shopping.cart.exception.ObjectNotFoundException;
import br.com.liven.shopping.cart.repository.InventoryRepository;
import br.com.liven.shopping.cart.repository.ProductRepository;
import br.com.liven.shopping.cart.utils.ProductSpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository repository, InventoryRepository inventoryRepository) {
        this.repository = repository;
        this.inventoryRepository = inventoryRepository;
    }

    public void addProduct(final ProductInPutDto productInPutDto) {
        final Product product = productInPutDto.toProduct();
        if (!repository.existsById(product.getSku())) {
            repository.save(product);
            return;
        }
        throw new DuplicateKeyException("this Product already exist");
    }

    public void update(final ProductInPutDto productInPutDto) {
        Product product = repository.findById(productInPutDto.sku()).orElseThrow(() ->
                new ObjectNotFoundException("product do not exists"));

        repository.saveAndFlush(product);
    }

    public Page<ProductOutPutDto> getFilteredProducts(final ProductSearchCriteria searchCriteria,
                                                      final Pageable pageable) {

        Specification<Product> specs = Specification.where(ProductSpecifications.hasStock());

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

        Page<Product> products = repository.findAll(specs, pageable);
        return products.map(Product :: toOutPutDto);
    }
}
