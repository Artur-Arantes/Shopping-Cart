package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.domain.User;
import br.com.liven.shopping.cart.dto.LoginInPutDto;
import br.com.liven.shopping.cart.dto.TokenDto;
import br.com.liven.shopping.cart.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authorizationManager;

    private TokenService tokenService;


    @PostMapping
    public ResponseEntity<TokenDto> auth(@RequestBody final LoginInPutDto loginInPutDto) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginInPutDto.email(),
                loginInPutDto.password()
        );
        final var auth = authorizationManager.authenticate(usernamePassword);
        final var token = tokenService.tokenGenerate((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenDto(token));
    }
}
