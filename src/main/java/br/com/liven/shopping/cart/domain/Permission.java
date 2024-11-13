package br.com.liven.shopping.cart.domain;

import br.com.liven.shopping.cart.enums.EnumUserPermission;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "permission")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
public class Permission implements GrantedAuthority {

    @Id
    @Column(name = "id_prm")
    private long id;

    @Column(name = "nam")
    @Enumerated(EnumType.STRING)
    private EnumUserPermission name;

    @Override
    public String getAuthority() {
        return name.getRole();
    }
}
