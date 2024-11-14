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
    @Column(name = "id_permission")
    private long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private EnumUserPermission name;

    @Override
    public String getAuthority() {
        return this.name != null ? this.name.getRole() : null;
    }

}
