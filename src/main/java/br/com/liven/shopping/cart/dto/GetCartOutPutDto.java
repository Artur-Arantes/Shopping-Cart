package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record GetCartOutPutDto(
long id,
List<ProductOutPutDto> products,
@JsonProperty("total_amount")
BigDecimal totalAmount
) {
}
