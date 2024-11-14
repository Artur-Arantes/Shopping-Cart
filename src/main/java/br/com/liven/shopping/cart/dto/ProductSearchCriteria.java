package br.com.liven.shopping.cart.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductSearchCriteria {
    private Long sku;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean inStock;


    public ProductSearchCriteria(Long sku, String name, BigDecimal minPrice, BigDecimal maxPrice, boolean inStock) {
        this.sku = sku;
        this.name = name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.inStock = inStock;
    }
}
