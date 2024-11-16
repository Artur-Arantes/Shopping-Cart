package br.com.liven.shopping.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record GetCartByIdOutPutDto(
        long id,
        List<ProductOutPutDto> products,
        BigDecimal total_amount
) {

}
