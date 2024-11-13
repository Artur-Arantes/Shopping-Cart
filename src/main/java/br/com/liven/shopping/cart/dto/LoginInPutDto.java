package br.com.liven.shopping.cart.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginInPutDto(

        @NotBlank(message = "Login cannot be blank")
        @NotNull(message = "Login cannot be null")
        @Email(message = "Must be a valid email")
        String email,
        @NotBlank(message = "Login cannot be blank")
        @NotNull(message = "Login cannot be null")
        @Min(5)
        String password

) {
}
