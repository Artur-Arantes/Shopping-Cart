package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class OrderCheckoutOutPutDto {
    @JsonProperty("order_id")
    private long id;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private List<ProductOutPutDto> products;
}
