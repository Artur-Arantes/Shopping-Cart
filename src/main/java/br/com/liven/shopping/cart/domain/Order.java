package br.com.liven.shopping.cart.domain;


import br.com.liven.shopping.cart.dto.OrderCheckoutOutPutDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "orders")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "version", column = @Column(name = "order_version")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "order_created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "order_updated_at"))
})
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private long id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = User.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ToString.Exclude
    private User user;

    @Column(name = "total_amount")
    private BigDecimal amount;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductOrder> products;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "id_cart", referencedColumnName = "id_cart")
    @ToString.Exclude
    private Cart cart;

    public OrderCheckoutOutPutDto toCheckoutOutPut() {

        return OrderCheckoutOutPutDto.builder()
                .id(id)
                .totalAmount(amount)
                .products(products.stream().map(ProductOrder::toOutPutDto).toList())
                .build();
    }
}
