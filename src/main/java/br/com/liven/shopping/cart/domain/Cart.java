package br.com.liven.shopping.cart.domain;

import br.com.liven.shopping.cart.dto.GetCartOutPutDto;
import br.com.liven.shopping.cart.dto.ProductOutPutDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
        @AttributeOverride(name = "version", column = @Column(name = "cart_version")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "cart_created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "cart_updated_at"))
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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCart> products;



    public GetCartOutPutDto toGetCartOutPutDto(){
        List<ProductOutPutDto> productOutPutDto = products.stream().map(ProductCart::toOutPutDto).toList();
        BigDecimal totalAmount = productOutPutDto.stream().map(ProductOutPutDto::getTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return  new GetCartOutPutDto(id, productOutPutDto,totalAmount);
    }

}
