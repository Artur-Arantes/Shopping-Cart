package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.dto.UserDeleteInPutDto;
import br.com.liven.shopping.cart.dto.UserInPutDto;
import br.com.liven.shopping.cart.dto.UserOutPutDto;
import br.com.liven.shopping.cart.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("v1/user")
public class UserController {

    final private UserService service;


    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid @NonNull final UserInPutDto userInPutDto) {
        service.addUser(userInPutDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserOutPutDto> getUser(@NotEmpty @RequestParam(name = "id") final String id) {
        final var outPut = service.getUserOutPut(id);
        return ResponseEntity.ok(outPut);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody @Valid @NonNull final UserDeleteInPutDto userInPutDto) {
        service.deleteUser(userInPutDto);
        return ResponseEntity.ok().build();
    }
}