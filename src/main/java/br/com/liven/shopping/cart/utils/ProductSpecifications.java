package br.com.liven.shopping.cart.utils;

import br.com.liven.shopping.cart.domain.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> hasStock() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("inventory").get("quantity"), 0);
    }


    public static Specification<Product> skuEquals(Long sku) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("sku"), sku);
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }
}


