package br.com.liven.shopping.cart.domain;

import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductCart {

    @EmbeddedId
    private ProductCartId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "id_cart", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "sku", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "total_item", nullable = false)
    private BigDecimal totalItem;


    public ProductOrder toProductOrder() {
        return ProductOrder.builder()
                .id(ProductOrderId.builder()
                        .productId(product.getSku())
                        .build())
                .product(product)
                .quantity(quantity)
                .totalItem(totalItem)
                .build();
    }


    public ProductOutPutDto toOutPutDto() {
        return ProductOutPutDto.builder()
                .sku(getProduct().getSku())
                .quantity(quantity)
                .price(product.getPrice())
                .totalItem(totalItem)
                .description(product.getDescription())
                .name(product.getName())
                .build();
    }
}

