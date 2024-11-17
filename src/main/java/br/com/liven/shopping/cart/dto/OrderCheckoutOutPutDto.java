package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCheckoutOutPutDto {
    @JsonProperty("order_id")
    private long id;
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private List<ProductOutPutDto> products;
}
