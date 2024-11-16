package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.domain.User;
import br.com.liven.shopping.cart.dto.LoginInPutDto;
import br.com.liven.shopping.cart.dto.TokenDto;
import br.com.liven.shopping.cart.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Authentication endpoint")
public class AuthController {

    private AuthenticationManager authorizationManager;

    private TokenService tokenService;


    @PostMapping
    @Operation(summary = "Returns an authentication token for further requests.")
    @ApiResponse(responseCode = "200", description = "Authentication success", content =
    @Content(schema = @Schema(implementation = TokenDto.class)))
    @ApiResponse(responseCode = "404", description = "User does not exists", content = @Content)
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
