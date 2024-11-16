package br.com.liven.shopping.cart.domain;


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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = User.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ToString.Exclude
    private User user;

    @Column(name = "total_amount")
    private BigDecimal amount;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "product_order", joinColumns = @JoinColumn(name = "id_order", referencedColumnName = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "sku", referencedColumnName = "sku"))
    List<Product> products;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "id_cart", referencedColumnName = "id_cart")
    @ToString.Exclude
    private Cart cart;
}
