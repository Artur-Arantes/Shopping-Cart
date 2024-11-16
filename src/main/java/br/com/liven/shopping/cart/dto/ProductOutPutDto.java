package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductOutPutDto implements Serializable {

    private long sku;
    private BigDecimal quantity;
    private String description;
    private BigDecimal price;
    private String name;
    @JsonProperty("total_item")
    private BigDecimal totalItem;
}
