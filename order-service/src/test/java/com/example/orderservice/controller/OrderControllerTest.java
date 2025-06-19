package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void testGetMyOrders_returnsOrders() throws Exception {
        // Vorbereitung: Testdaten
        Order mockOrder = new Order(); // createdAt wird im Konstruktor automatisch gesetzt
        mockOrder.setUser("alice");
        mockOrder.setProduct("Testprodukt");

        when(orderRepository.findByUser("alice")).thenReturn(List.of(mockOrder));

        // Test: GET /orders mit JWT-Simulation
        mockMvc.perform(get("/orders")
                .with(jwt().jwt(jwt -> jwt.claim("preferred_username", "alice"))))
                .andExpect(status().isOk());
    }
}
