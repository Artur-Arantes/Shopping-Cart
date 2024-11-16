package br.com.liven.shopping.cart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductOrderId implements Serializable {

    @Column(name = "id_order")
    private Long orderId;

    @Column(name = "sku")
    private Long productId;
}
