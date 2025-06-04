package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.InvalidPriceRequestException;
import com.miempresa.priceapplication.exception.PriceNotFoundException;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    /**
     * Obtiene los precios aplicables según el producto, marca y fecha proporcionados.
     * Si no se encuentran precios, se lanza una excepción personalizada PriceNotFoundException.
     *
     * @param productId El ID del producto.
     * @param brandId El ID de la marca.
     * @param date La fecha como cadena en formato ISO 8601 ("YYYY-MM-DDTHH:MM:SS").
     * @return Una lista de precios aplicables.
     */
    public List<Price> getApplicablePrices(Integer productId, Integer brandId, LocalDateTime date) {
        log.info("Consultando precios para ProductID: {}, BrandID: {}, Fecha: {}", productId, brandId, date);
        LocalDateTime dateTime = date;

        List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, dateTime);

        if (prices.isEmpty()) {
            log.warn("No se encontraron precios para ProductID: {}, BrandID: {}, Fecha: {}", productId, brandId, date);
            throw new PriceNotFoundException("No se encontraron precios para el producto, marca y fecha proporcionados.");
        }

        log.info("Precios encontrados: {}", prices.size());
        // Solo devolver el precio con mayor prioridad (primero de la lista)
        Price selected = prices.get(0);
        return List.of(selected);
    }

    /**
     * Crea un nuevo registro de precio en la base de datos.
     *
     * Este método recibe un objeto {@link Price}, lo valida y lo almacena en la base de datos
     * utilizando el repositorio de JPA. En caso de que el proceso de guardado falle, se registra
     * un mensaje de error en los logs y se lanza una excepción personalizada.
     *
     * @param price El objeto {@link Price} que contiene los detalles del nuevo precio.
     * @return El objeto {@link Price} almacenado en la base de datos con su ID generado.
     * @throws InvalidPriceRequestException si ocurre un error durante el proceso de almacenamiento en la base de datos.
     */
    public Price createPrice(Price price) {
        log.debug("Checking if price already exists for product {}, brand {}, startDate {}",
                price.getProductId(), price.getBrandId(), price.getStartDate());

        Optional<Price> existingPrice = priceRepository.findByProductIdAndBrandIdAndStartDate(
                price.getProductId(), price.getBrandId(), price.getStartDate());

        if (existingPrice.isPresent()) {
            log.error("Price already exists for this product, brand, and date: {}", existingPrice.get());
            throw new InvalidPriceRequestException("The price for this product, brand, and date already exists.");
        }

        log.debug("Saving new price...");
        try {
            Price savedPrice = priceRepository.save(price);
            log.info("Price saved successfully: {}", savedPrice);
            return savedPrice;
        } catch (Exception e) {
            log.error("Error saving the price: {}", e.getMessage(), e);
            throw new InvalidPriceRequestException("Error saving the price. Please verify the data.");
        }
    }

    /**
     * Elimina un precio por su identificador.
     *
     * @param id identificador del precio a eliminar
     * @throws PriceNotFoundException si no existe el precio con el id proporcionado
     */
    public void deletePrice(Long id) {
        log.debug("Attempting to delete price with id {}", id);
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException("Price with id " + id + " not found"));

        priceRepository.delete(price);
        log.info("Price deleted successfully: {}", id);
    }
}
