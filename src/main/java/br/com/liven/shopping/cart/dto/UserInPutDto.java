package br.com.liven.shopping.cart.dto;

import br.com.liven.shopping.cart.domain.Person;
import br.com.liven.shopping.cart.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInPutDto(
        @NotBlank(message = "Name cannot be blank")
        @NotNull(message = "Name cannot be null")
        String name,

        @Email(message = "email is not valid")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @NotNull(message = "Password cannot be null")
        @Min(5)
        String password
) {
    public User toUser() {
        return User.builder()
                .enabled(true)
                .password(password)
                .person(Person.builder().email(email).name(name).build())
                .build();
    }
}
