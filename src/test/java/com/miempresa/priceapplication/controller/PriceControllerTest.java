package com.miempresa.priceapplication.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceRepository priceRepository;

    // Inicializa datos de prueba antes de cada test
    @BeforeEach
    public void setUp() {
        priceRepository.deleteAll(); // Limpia la base de datos antes de cada test

        Price price1 = new Price(1L, 1, LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59), 1, 35455, 0, 35.50, "EUR");
        Price price2 = new Price(2L, 1, LocalDateTime.of(2020, 6, 14, 15, 0), LocalDateTime.of(2020, 6, 14, 18, 30), 2, 35455, 1, 25.45, "EUR");
        Price price3 = new Price(3L, 1, LocalDateTime.of(2020, 6, 15, 0, 0), LocalDateTime.of(2020, 6, 15, 11, 0), 3, 35455, 1, 30.50, "EUR");
        Price price4 = new Price(4L, 1, LocalDateTime.of(2020, 6, 15, 16, 0), LocalDateTime.of(2020, 12, 31, 23, 59), 4, 35455, 1, 38.95, "EUR");

        priceRepository.save(price1);
        priceRepository.save(price2);
        priceRepository.save(price3);
        priceRepository.save(price4);
    }

    // Test 1: Petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1000OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("date", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(35.5))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 2: Petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1600OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("date", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(25.45))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 3: Petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt2100OnJune14() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("date", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(35.50))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 4: Petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt1000OnJune15() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("date", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(30.50))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }

    // Test 5: Petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
    @Test
    public void testPriceAt2100OnJune16() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("date", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(38.95))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].productId").value(35455));
    }
}