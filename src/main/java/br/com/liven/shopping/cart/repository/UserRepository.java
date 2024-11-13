package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Person;
import br.com.liven.shopping.cart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPerson(Person person);

    List<User> findAllBySendAlertNotification(boolean sendAlertNotification);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO user_permission (id_use, id_prm) VALUES (:userId, :permissionId)")
    void savePermissionForUser(@Param("userId") Long userId, @Param("permissionId") Long permissionId);

}