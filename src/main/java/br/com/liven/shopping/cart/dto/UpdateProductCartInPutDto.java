package br.com.liven.shopping.cart.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductCartInPutDto(
        long sku,

        @Positive
        BigDecimal quantity
) {
}
