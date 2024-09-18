package com.miempresa.priceapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miempresa.priceapplication.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControlerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenGetPrice_thenReturnPrice() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("date", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value(35455))
                .andExpect(jsonPath("$[0].brandId").value(1))
                .andExpect(jsonPath("$[0].price").value(35.50))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }

    @Test
    public void whenInvalidDateFormat_thenBadRequest() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("date", "invalid-date-format")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request"))
                .andExpect(jsonPath("$.message").value("Formato de fecha inválido. Por favor, usa el formato ISO 8601: YYYY-MM-DDTHH:MM:SS"));
    }

    @Test
    public void whenPriceNotFound_thenNotFound() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("productId", "99999") // Un ID de producto que no existe
                        .param("brandId", "1")
                        .param("date", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No se encontraron precios para el producto, marca y fecha proporcionados."));
    }

    @Test
    public void whenInvalidProductIdOrBrandId_thenBadRequest() throws Exception {
        mockMvc.perform(get("/api/prices")
                        .param("productId", "-1") // ID de producto inválido
                        .param("brandId", "-1")   // ID de marca inválido
                        .param("date", "2020-06-14T10:00:00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request"))
                .andExpect(jsonPath("$.message").value("La solicitud contiene parámetros inválidos."));
    }

    // Happy path for POST request
    @Test
    public void whenCreatePriceWithValidData_thenStatus201() throws Exception {
        Price newPrice = new Price(
                null, // Auto-generated ID
                1, // brandId
                LocalDateTime.now().plusMinutes(1), // startDate (ensure unique)
                LocalDateTime.now().plusDays(1), // endDate (ensure unique)
                2, // priceList (ensure unique)
                99999, // productId (ensure unique)
                0, // priority
                49.99, // price
                "USD" // currency
        );

        mockMvc.perform(post("/api/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrice)))
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").isNotEmpty()) // Ensure the new price has an ID
                .andExpect(jsonPath("$.price").value(49.99)) // Check that price is correct
                .andExpect(jsonPath("$.currency").value("USD")); // Check currency is correct
    }

    // Error handling for invalid POST request (e.g., missing required fields)
    @Test
    public void whenCreatePriceWithInvalidData_thenBadRequest() throws Exception {
        Price newPrice = new Price(null, null, null, null, 1, 35455, 0, 39.99, "EUR");

        mockMvc.perform(post("/api/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPrice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request"))  // Ensure the value matches
                .andExpect(jsonPath("$.message").value("One or more fields are invalid"))
                .andExpect(jsonPath("$.brandId").value("El ID de la marca no puede ser nulo"))
                .andExpect(jsonPath("$.startDate").value("La fecha de inicio no puede ser nula"))
                .andExpect(jsonPath("$.endDate").value("La fecha de finalización no puede ser nula"));
    }

    @Test
    public void whenDuplicatePrice_thenStatus400() throws Exception {
        Price duplicatePrice = new Price(null, 1, LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                1, 35455, 0, 35.5, "EUR");

        mockMvc.perform(post("/api/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatePrice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Request"))
                .andExpect(jsonPath("$.message").value("The price for this product, brand, and date already exists."));
    }


}

