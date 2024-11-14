package br.com.liven.shopping.cart.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "sku")
@ToString(callSuper = true)
@Entity
@Table(name = "inventory")
@AttributeOverrides({
        @AttributeOverride(name = "version", column = @Column(name = "ver_inv")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "cre_at_inv")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "upd_at_inv"))
})
public class Inventory extends BaseEntity{


    @Id
    private long sku;

    private BigDecimal quantity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
    private Product product;
}
