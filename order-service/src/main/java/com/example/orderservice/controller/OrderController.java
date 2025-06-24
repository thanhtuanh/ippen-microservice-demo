// REST-Controller für Bestellungen, geschützt via OAuth2 (JWT)
package com.example.orderservice.controller;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// REST-Controller für Bestellungen, gesichert über JWT/OIDC. Liefert Bestellungen des authentifizierten Users.
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository repo;
    public OrderController(OrderRepository repo) { this.repo = repo; }
    @GetMapping
    public List<Order> getMyOrders(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("preferred_username");
        return repo.findByUser(username);
    }
    @PostMapping
    public Order createOrder(@AuthenticationPrincipal Jwt principal, @RequestBody Order order) {
        order.setUser(principal.getClaimAsString("preferred_username"));
        return repo.save(order);
    }
}
