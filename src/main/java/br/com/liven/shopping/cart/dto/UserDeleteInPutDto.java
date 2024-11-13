package br.com.liven.shopping.cart.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public record UserDeleteInPutDto(
        @Positive
        Long id,
        @Email(message = "must be an email on a valid format")
        String email
) {
}
