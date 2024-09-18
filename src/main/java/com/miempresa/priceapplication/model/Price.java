package com.miempresa.priceapplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la marca no puede ser nulo")
    @Min(value = 1, message = "El ID de la marca debe ser mayor que 0")
    private Integer brandId;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de finalización no puede ser nula")
    private LocalDateTime endDate;

    @NotNull(message = "La lista de precios no puede ser nula")
    @Min(value = 1, message = "La lista de precios debe ser mayor que 0")
    private Integer priceList;

    @NotNull(message = "El ID del producto no puede ser nulo")
    @Min(value = 1, message = "El ID del producto debe ser mayor que 0")
    private Integer productId;

    @NotNull(message = "La prioridad no puede ser nula")
    private Integer priority;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private Double price;

    @NotBlank(message = "La moneda no puede estar vacía")
    @Size(min = 3, max = 3, message = "La moneda debe tener un código ISO de 3 caracteres")
    private String currency;
}
