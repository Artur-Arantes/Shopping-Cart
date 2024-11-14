package br.com.liven.shopping.cart.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductOutPutDto {

    private long sku;
    private BigDecimal quantity;
    private String description;
    private BigDecimal price;
    private String name;
}
