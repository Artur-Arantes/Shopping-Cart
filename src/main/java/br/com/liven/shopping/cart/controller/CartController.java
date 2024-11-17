package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.dto.*;
import br.com.liven.shopping.cart.service.CartService;
import br.com.liven.shopping.cart.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("v1/cart")
@Tag(name = "Cart", description = "Endpoints to GET,PUT,POST cart")
public class CartController {

    private final CartService service;
    private final TokenService tokenService;


    @PostMapping
    @Operation(summary = "Create cart endpoint, must have token and valid user to create a cart")
    @ApiResponse(responseCode = "200", description = "Cart created", content = @Content)
    public ResponseEntity<CreateCartOutPutDto> addCart(@RequestHeader("Authorization") String authorizationHeader) {
        final var email = tokenService.getEmailByAuthorizationHeader(authorizationHeader);
        final var outPut = service.getValidCart(email);
        return ResponseEntity.ok(outPut);
    }

    @PutMapping
    @Operation(summary = "Update cart endpoint, the products given in this endpoint will be the only products on the cart")
    @ApiResponse(responseCode = "200", description = "Cart successfully updated", content = @Content)
    @ApiResponse(responseCode = "404", description = "Cart does not exist", content = @Content)
    @ApiResponse(responseCode = "403", description =
            "Validation error, such as unauthorized access to another user's cart or expired cart", content = @Content)
    public ResponseEntity<HttpStatus> updateCartProducts(@RequestBody @Valid @NonNull final UpdateCartInPutDto inPut,
                                                         @RequestHeader("Authorization") String authorizationHeader) {
        final var email = tokenService.getEmailByAuthorizationHeader(authorizationHeader);
        service.update(inPut, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get cart by ID")
    @ApiResponse(responseCode = "200", description = "Success to get cart",
            content = @Content(schema = @Schema(implementation = CreateCartOutPutDto.class)))
    @ApiResponse(responseCode = "404", description = "Cart does not exist", content = @Content)
    @ApiResponse(responseCode = "403", description =
            "Validation error, such as unauthorized access to another user's cart or expired cart", content = @Content)
    public ResponseEntity<CreateCartOutPutDto> getCartById(@RequestHeader("Authorization") String authorizationHeader,
                                                           @RequestParam long id) {
        final var email = tokenService.getEmailByAuthorizationHeader(authorizationHeader);
        final var outPut = service.getCartById(id, email);
        return ResponseEntity.ok(outPut);
    }

    @PostMapping("/checkout")
    @Operation(summary = "Checkout Cart and get an order, here the inventory of the product will be subtracted")
    @ApiResponse(responseCode = "200", description = "Checkout Success",
            content = @Content(schema = @Schema(implementation = OrderCheckoutOutPutDto.class)))
    @ApiResponse(responseCode = "404", description = "Cart does not exist", content = @Content)
    @ApiResponse(responseCode = "403", description =
            "Validation error, such as unauthorized access to another user's cart or expired cart.Conflict due to simultaneous checkout attempts."
            , content = @Content)
    @ApiResponse(responseCode = "409",
            description = "Conflicted, cart already did the checkout, cart can have only one checkout", content = @Content)
    public ResponseEntity<OrderCheckoutOutPutDto> checkout(@RequestHeader("Authorization") String authorizationHeader,
                                                           @RequestBody CheckoutInPutDto input) {
        final var email = tokenService.getEmailByAuthorizationHeader(authorizationHeader);
        final var outPut = service.processCheckout(input, email);
        return ResponseEntity.ok(outPut);
    }
}
