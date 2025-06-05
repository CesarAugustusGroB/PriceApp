package com.miempresa.priceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miempresa.priceapplication.model.Customer;
import com.miempresa.priceapplication.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.miempresa.priceapplication.CustomerServiceApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    private String authHeader;

    @BeforeEach
    public void login() throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andReturn().getResponse().getContentAsString();
        authHeader = "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    @Test
    public void testGetCustomers() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCustomer() throws Exception {
        Customer customer = new Customer(null, "Alice", "alice@example.com", "555");
        mockMvc.perform(post("/api/customers")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }
}
