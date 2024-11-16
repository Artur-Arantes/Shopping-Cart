package br.com.liven.shopping.cart.dto;

import br.com.liven.shopping.cart.domain.Inventory;
import br.com.liven.shopping.cart.domain.Product;

import java.math.BigDecimal;

public record UpdateProductIntPutDto(

        long sku,
        BigDecimal quantity,
        String description,
        BigDecimal price,
        String name

) {

    public Product toProduct() {
        return Product.builder()
                .sku(sku)
                .name(name)
                .description(description)
                .price(price)
                .inventory(Inventory.builder()
                        .quantity(quantity)
                        .sku(sku)
                        .build())
                .build();
    }
}
