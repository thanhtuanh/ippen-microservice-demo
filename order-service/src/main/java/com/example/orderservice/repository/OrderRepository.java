// Spring Data Repository für Bestellungen
package com.example.orderservice.repository;
import com.example.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(String user);
}
