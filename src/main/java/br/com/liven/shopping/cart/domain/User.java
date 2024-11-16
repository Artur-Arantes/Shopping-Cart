package br.com.liven.shopping.cart.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "version", column = @Column(name = "user_version")),
        @AttributeOverride(name = "createdAt", column = @Column(name = "user_created_at")),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "user_updated_at"))
})
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private boolean enabled;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Person.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "id_person",referencedColumnName = "email")
    @ToString.Exclude
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "user_permission", joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_permission", referencedColumnName = "id_permission"))
    List<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getUsername() {
        return this.getPerson().getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


}