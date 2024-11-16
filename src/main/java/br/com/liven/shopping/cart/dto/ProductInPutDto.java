package br.com.liven.shopping.cart.dto;

import br.com.liven.shopping.cart.domain.Inventory;
import br.com.liven.shopping.cart.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductInPutDto(

        long sku,
        @Positive(message = "quantity cannot be negative")
        BigDecimal quantity,
        String description,
        @Positive(message = "price cannot be negative")
        BigDecimal price,
        @NotBlank(message = "Product must have name")
        @NotEmpty(message = "Product must have name")
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
