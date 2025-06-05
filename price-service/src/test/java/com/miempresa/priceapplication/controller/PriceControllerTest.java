package com.miempresa.priceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = com.miempresa.priceapplication.PriceServiceApplication.class)
@AutoConfigureMockMvc
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PriceRepository priceRepository;

    private Long existingPriceId;
    private String authHeader;

    // Inicializa datos de prueba antes de cada test
    @BeforeEach
    public void setUp() throws Exception {
        priceRepository.deleteAll(); // Limpia la base de datos antes de cada test

        Price price1 = new Price(null, 1, LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59), 1, 35455, 0, new BigDecimal("35.50"), "EUR");
        Price price2 = new Price(null, 1, LocalDateTime.of(2020, 6, 14, 15, 0), LocalDateTime.of(2020, 6, 14, 18, 30), 2, 35455, 1, new BigDecimal("25.45"), "EUR");
        Price price3 = new Price(null, 1, LocalDateTime.of(2020, 6, 15, 0, 0), LocalDateTime.of(2020, 6, 15, 11, 0), 3, 35455, 1, new BigDecimal("30.50"), "EUR");
        Price price4 = new Price(null, 1, LocalDateTime.of(2020, 6, 15, 16, 0), LocalDateTime.of(2020, 12, 31, 23, 59), 4, 35455, 1, new BigDecimal("38.95"), "EUR");

        price1 = priceRepository.save(price1);
        priceRepository.save(price2);
        priceRepository.save(price3);
        priceRepository.save(price4);

        existingPriceId = price1.getId();

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andReturn().getResponse().getContentAsString();
        authHeader = "Bearer " + objectMapper.readTree(response).get("token").asText();
    }

    // Test 1: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1000OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(35.5))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 2: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1600OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .param("date", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(25.45))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 3: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt2100OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .param("date", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(35.50))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 4: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1000OnJune15() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .param("date", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(30.50))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 5: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt2100OnJune16() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .param("date", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price").value(38.95))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    @Test
    public void testDeleteExistingPrice() throws Exception {
        Long idToDelete = existingPriceId;
        mockMvc.perform(delete("/api/prices/{id}", idToDelete)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isNoContent());
        assertFalse(priceRepository.findById(idToDelete).isPresent());
    }

    @Test
    public void testDeleteNonExistingPrice() throws Exception {
        mockMvc.perform(delete("/api/prices/{id}", 999L)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateExistingPrice() throws Exception {
        Price updated = new Price(null, 1, LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59), 1, 35455, 0, new BigDecimal("40.0"), "EUR");

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/api/prices/{id}", existingPriceId)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(40.0));
    }

    @Test
    public void testUpdateNonExistingPrice() throws Exception {
        Price updated = new Price(null, 1, LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59), 1, 35455, 0, new BigDecimal("40.0"), "EUR");

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/api/prices/{id}", 999L)
                        .header(org.springframework.http.HttpHeaders.AUTHORIZATION, authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }
}
