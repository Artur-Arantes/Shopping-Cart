package br.com.liven.shopping.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Schema(description = "product")
public class ProductOutPutDto implements Serializable {
    private long sku;
    @Schema(description = "Product quantity")
    private BigDecimal quantity;
    private String description;
    private BigDecimal price;
    private String name;
    @Schema(description = "Quantity multiplied by price")
    @JsonProperty("total_item")
    private BigDecimal totalItem;
}
