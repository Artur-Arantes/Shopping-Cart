package br.com.liven.shopping.cart.domain;

import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "product_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductOrder {

    @EmbeddedId
    private ProductOrderId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "sku", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "total_item")
    private BigDecimal totalItem;


    public ProductOutPutDto toOutPutDto(){
        return ProductOutPutDto.builder()
                .sku(product.getSku())
                .quantity(quantity)
                .price(product.getPrice())
                .totalItem(totalItem)
                .description(product.getDescription())
                .name(product.getName())
                .build();
    }

}
