package br.com.liven.shopping.cart.domain;

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
        @AttributeOverride(name = "version", column = @Column(name = "inventory_version")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "inventory_created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "inventory_updated_at"))
})
public class Inventory extends BaseEntity{


    @Id
    private long sku;

    private BigDecimal quantity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
    @ToString.Exclude
    private Product product;
}
