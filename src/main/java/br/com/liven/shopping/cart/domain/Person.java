package br.com.liven.shopping.cart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "person")
@AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString
@Entity
public class Person {


    @Column(name = "name")
    private String name;

    @Id
    @Column(name = "email")
    private String email;

}