package br.com.liven.shopping.cart.dto;

import br.com.liven.shopping.cart.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User")
public record UserOutPutDto(Long id, String email, boolean active) {

    public UserOutPutDto(User user) {
        this(user.getId(), user.getPerson().getEmail(), user.isEnabled());
    }

}
