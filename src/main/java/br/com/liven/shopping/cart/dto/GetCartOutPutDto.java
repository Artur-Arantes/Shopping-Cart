package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Cart")
public record GetCartOutPutDto(
        @Schema(description = "Cart Id")
        long id,
        @Schema(description = "Products in the cart")
        List<ProductOutPutDto> products,
        @JsonProperty("total_amount")
        BigDecimal totalAmount
) {
}
