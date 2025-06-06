package com.miempresa.priceapplication.controller;

import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.model.PriceEvent;
import com.miempresa.priceapplication.service.PriceCommandService;
import com.miempresa.priceapplication.service.PriceQueryService;
import com.miempresa.priceapplication.service.PriceEventService;
import com.miempresa.priceapplication.service.PricePredictionService;
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
import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@Tag(name = "Price API", description = "API para consultar precios por fecha, producto y marca")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PriceController {

    private final PriceCommandService commandService;
    private final PriceQueryService queryService;
    private final PriceEventService eventService;
    private final PricePredictionService predictionService;

    @Operation(summary = "Crear un nuevo precio", description = "Crea un precio basado en los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precio creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public Mono<ResponseEntity<Price>> createPrice(@Valid @RequestBody Price price) {
        log.info("Solicitud de creación de precio recibida: {}", price);
        return Mono.fromCallable(() -> commandService.createPrice(price))
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
        return Mono.fromCallable(() -> queryService.getApplicablePrices(productId, brandId, date))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Eliminar un precio", description = "Elimina un precio existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Precio eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Precio no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePrice(@PathVariable @Min(1) Long id) {
        return Mono.fromRunnable(() -> commandService.deletePrice(id))
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
        return Mono.fromCallable(() -> commandService.updatePrice(id, price))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Predecir precio dinámico", description = "Utiliza aprendizaje automático sencillo para sugerir un precio basado en el historial")
    @GetMapping("/predict")
    public Mono<ResponseEntity<BigDecimal>> predictPrice(
            @RequestParam @Min(1) Integer productId,
            @RequestParam @Min(1) Integer brandId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return Mono.fromCallable(() -> predictionService.predictPrice(productId, brandId, date))
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Obtener eventos de un precio", description = "Devuelve el historial de eventos para un precio")
    @GetMapping("/{id}/events")
    public Mono<ResponseEntity<List<PriceEvent>>> getPriceEvents(@PathVariable @Min(1) Long id) {
        return Mono.fromCallable(() -> eventService.getEvents(id))
                .map(ResponseEntity::ok);
    }
}
