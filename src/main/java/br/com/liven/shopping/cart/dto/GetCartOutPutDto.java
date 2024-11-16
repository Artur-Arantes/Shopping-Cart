package br.com.liven.shopping.cart.dto;

import java.util.List;

public record GetCartOutPutDto(
long id,
List<ProductOutPutDto> products
) {
}
