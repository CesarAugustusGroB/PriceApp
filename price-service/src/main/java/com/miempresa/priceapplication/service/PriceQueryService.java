package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.PriceNotFoundException;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceQueryService {

    private final PriceRepository priceRepository;

    public List<Price> getApplicablePrices(Integer productId, Integer brandId, LocalDateTime date) {
        log.info("Consultando precios para ProductID: {}, BrandID: {}, Fecha: {}", productId, brandId, date);
        LocalDateTime dateTime = date;

        List<Price> prices = priceRepository.findApplicablePrices(productId, brandId, dateTime);

        if (prices.isEmpty()) {
            log.warn("No se encontraron precios para ProductID: {}, BrandID: {}, Fecha: {}", productId, brandId, date);
            throw new PriceNotFoundException("No se encontraron precios para el producto, marca y fecha proporcionados.");
        }

        log.info("Precios encontrados: {}", prices.size());
        Price selected = prices.get(0);
        return List.of(selected);
    }
}
