// REST-Controller für Benutzerverwaltung
package com.example.userservice.controller;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;
    public UserController(UserRepository repo) { this.repo = repo; }
    @GetMapping
    public List<User> getAll() { return repo.findAll(); }
    @PostMapping
    public User create(@RequestBody User user) {
        return repo.save(user);
    }
}
