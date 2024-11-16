package br.com.liven.shopping.cart.dto;

import java.util.List;

public record UpdateCartInPutDto(
        List<UpdateProductCartInPutDto> products,
        long id
) {
}
