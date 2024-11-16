package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.dto.UserDeleteInPutDto;
import br.com.liven.shopping.cart.dto.UserInPutDto;
import br.com.liven.shopping.cart.dto.UserOutPutDto;
import br.com.liven.shopping.cart.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "Create,update and delete user")
public class UserController {

    final private UserService service;


    @PostMapping
    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "200", description = "User created", content = @Content)
    @ApiResponse(responseCode = "409", description = "An account already exists with this email", content = @Content)
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid @NonNull final UserInPutDto userInPutDto) {
        service.addUser(userInPutDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get User")
    @ApiResponse(responseCode = "200", description = "Found User",
            content = @Content(schema = @Schema(implementation = UserOutPutDto.class)))
    @ApiResponse(responseCode = "404", description = "User with the given ID does not exist.", content = @Content)
    public ResponseEntity<UserOutPutDto> getUser(@NotEmpty @RequestParam(name = "id") final String id) {
        final var outPut = service.getUserOutPut(id);
        return ResponseEntity.ok(outPut);
    }

    @DeleteMapping
    @Operation(summary = "Delete User")
    @ApiResponse(responseCode = "200", description = "User deleted", content = @Content)
    @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content)
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody @Valid @NonNull final UserDeleteInPutDto userInPutDto) {
        service.deleteUser(userInPutDto);
        return ResponseEntity.ok().build();
    }
}