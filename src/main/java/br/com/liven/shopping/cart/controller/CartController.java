package br.com.liven.shopping.cart.controller;

import br.com.liven.shopping.cart.dto.CheckoutInPutDto;
import br.com.liven.shopping.cart.dto.GetCartOutPutDto;
import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import br.com.liven.shopping.cart.dto.UpdateCartInPutDto;
import br.com.liven.shopping.cart.service.CartService;
import br.com.liven.shopping.cart.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("v1/cart")
public class CartController {

    private final CartService service;
    private final TokenService tokenService;


    @PostMapping
    public ResponseEntity<GetCartOutPutDto> addCart(@RequestHeader("Authorization") String authorizationHeader) {
        String token = tokenService.extractToken(authorizationHeader);
        String email = tokenService.verifyToken(token);
        GetCartOutPutDto outPut = service.getValidCart(email);
        return ResponseEntity.ok(outPut);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateCartProducts(@RequestBody @Valid @NonNull final UpdateCartInPutDto inPut,
                                                         @RequestHeader("Authorization") String authorizationHeader) {
        String token = tokenService.extractToken(authorizationHeader);
        String email = tokenService.verifyToken(token);
        service.update(inPut, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<GetCartOutPutDto> getCartById(@RequestHeader("Authorization") String authorizationHeader,
                                                        @RequestParam long id) {
        String token = tokenService.extractToken(authorizationHeader);
        String email = tokenService.verifyToken(token);
        GetCartOutPutDto outPut = service.getCartById(id, email);
        return ResponseEntity.ok(outPut);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutOutPutDto> checkout(@RequestHeader("Authorization") String authorizationHeader,
                                                           @RequestBody CheckoutInPutDto input) {
        String token = tokenService.extractToken(authorizationHeader);
        String email = tokenService.verifyToken(token);
        OrderCheckoutOutPutDto outPut = service.processCheckout(input, email);
        return ResponseEntity.ok(outPut);
    }
}
