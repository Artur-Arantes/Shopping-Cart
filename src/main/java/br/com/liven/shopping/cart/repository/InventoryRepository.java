package br.com.liven.shopping.cart.repository;

import br.com.liven.shopping.cart.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
