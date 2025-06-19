package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetAllUsers_returnsUserList() throws Exception {
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
