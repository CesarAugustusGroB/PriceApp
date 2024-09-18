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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de precios.
 * Proporciona los endpoints para crear, obtener y eliminar precios en el sistema.
 */
@RestController
@RequestMapping("/api/prices")
@Tag(name = "Price API", description = "API para consultar precios por fecha, producto y marca")
@Validated
@Slf4j
public class PriceController {

    @Autowired
    private PriceService priceService;

    /**
     * Crea un nuevo precio basado en los detalles proporcionados.
     *
     * @param price Objeto Price con los detalles del precio a crear.
     * @return ResponseEntity con el precio creado y el código de estado 201.
     */
    @Operation(summary = "Crear un nuevo precio", description = "Crea un precio basado en los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precio creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Price> createPrice(@Valid @RequestBody Price price) {
        log.info("Solicitud de creación de precio recibida: {}", price);
        Price createdPrice = priceService.createPrice(price);
        log.info("Precio creado exitosamente: {}", createdPrice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrice); // Retorna 201 Created
    }

    /**
     * Obtiene una lista de precios aplicables basada en el producto, marca y fecha proporcionados.
     *
     * @param productId ID del producto.
     * @param brandId ID de la marca.
     * @param date Fecha en formato ISO a considerar para el precio aplicable.
     * @return Lista de precios aplicables.
     */
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
    public ResponseEntity<List<Price>> getPrices(
            @RequestParam @Min(1) Integer productId,
            @RequestParam @Min(1) Integer brandId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String date) {
        return ResponseEntity.ok(priceService.getApplicablePrices(productId, brandId, date));
    }

    /**
     * Elimina un precio existente basado en el ID proporcionado.
     *
     * @param id ID del precio a eliminar.
     * @return ResponseEntity con el código de estado 204 si se eliminó correctamente, o 404 si no se encontró.
     */
    @Operation(summary = "Eliminar un precio por ID", description = "Elimina un precio existente por su ID de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "El precio se eliminó correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "El precio con el ID proporcionado no fue encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        log.info("Precio con ID {} eliminado exitosamente", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
