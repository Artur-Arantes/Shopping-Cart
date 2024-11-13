package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.domain.User;
import br.com.liven.shopping.cart.dto.LoginInPutDto;
import br.com.liven.shopping.cart.dto.TokenDto;
import br.com.liven.shopping.cart.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authorizationManager;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity<TokenDto> auth(@RequestBody final LoginInPutDto loginInPutDto) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginInPutDto.email(),
                loginInPutDto.password()
        );
        Authentication auth = authorizationManager.authenticate(usernamePassword);
        String token = tokenService.tokenGenerate((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenDto(token));
    }
}
