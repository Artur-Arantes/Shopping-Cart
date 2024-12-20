package br.com.liven.shopping.cart.domain;

import br.com.liven.shopping.cart.dto.ProductOutPutDto;
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
@Table(name = "product")
@AttributeOverrides({
        @AttributeOverride(name = "version", column = @Column(name = "product_version")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "product_created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "product_updated_at"))
})
public class Product extends BaseEntity {

    @Id
    @Column(name = "sku")
    private long sku;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Inventory inventory;


    public ProductOutPutDto toOutPutDto() {
        return ProductOutPutDto.builder()
                .sku(sku)
                .quantity(inventory.getQuantity())
                .description(description)
                .price(price)
                .name(name)
                .build();
    }
}
