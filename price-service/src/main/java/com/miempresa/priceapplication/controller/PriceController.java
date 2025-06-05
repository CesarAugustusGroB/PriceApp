package com.miempresa.priceapplication.controller;

import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@Tag(name = "Price API", description = "API para consultar precios por fecha, producto y marca")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @Operation(summary = "Crear un nuevo precio", description = "Crea un precio basado en los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precio creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<Price>> createPrice(@Valid @RequestBody Price price) {
        log.info("Solicitud de creación de precio recibida: {}", price);
        return Mono.fromCallable(() -> priceService.createPrice(price))
                .map(createdPrice -> {
                    log.info("Precio creado exitosamente: {}", createdPrice);
                    return ResponseEntity.status(201).body(createdPrice);
                });
    }

    @Operation(summary = "Obtener precios aplicables", description = "Devuelve el precio aplicable basado en producto, marca y fecha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio encontrado exitosamente",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Price.class)) }),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o validación fallida",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Precio no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public Mono<ResponseEntity<List<Price>>> getPrices(
            @RequestParam @Min(1) Integer productId,
            @RequestParam @Min(1) Integer brandId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return Mono.fromCallable(() -> priceService.getApplicablePrices(productId, brandId, date))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Eliminar un precio", description = "Elimina un precio existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Precio eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Precio no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePrice(@PathVariable @Min(1) Long id) {
        return Mono.fromRunnable(() -> priceService.deletePrice(id))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Operation(summary = "Actualizar un precio", description = "Actualiza un precio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precio actualizado exitosamente",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Price.class)) }),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Precio no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Price>> updatePrice(@PathVariable @Min(1) Long id,
                                             @Valid @RequestBody Price price) {
        return Mono.fromCallable(() -> priceService.updatePrice(id, price))
                .map(ResponseEntity::ok);
    }
}
