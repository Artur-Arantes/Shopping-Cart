package br.com.liven.shopping.cart.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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

}

