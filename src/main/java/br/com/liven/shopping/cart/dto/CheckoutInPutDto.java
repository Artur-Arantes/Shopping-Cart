package br.com.liven.shopping.cart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record CheckoutInPutDto(
        @Positive
        @NotEmpty
        long id
) {
}
