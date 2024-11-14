package br.com.liven.shopping.cart.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "cart")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "version", column = @Column(name = "ver_cart")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "cre_at_cart")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "upd_at_cart"))
})
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = User.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ToString.Exclude
    private User user;

    @Column(name = "closed")
    private boolean closed;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "id_cart", referencedColumnName = "id_cart"),
            inverseJoinColumns = @JoinColumn(name = "sku", referencedColumnName = "sku"))
    List<Product> products;

}
