package com.example.userservice.model;

// WICHTIG: Ab Spring Boot 3.x werden JPA-Annotationen aus 'jakarta.persistence' importiert!
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private String username;
    private String email;

    // Getter und Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}