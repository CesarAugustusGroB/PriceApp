package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.InvalidPriceRequestException;
import com.miempresa.priceapplication.exception.PriceNotFoundException;
import com.miempresa.priceapplication.exception.PriceServiceException;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.repository.PriceRepository;
import com.miempresa.priceapplication.messaging.PriceEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceCommandService {

    private final PriceRepository priceRepository;
    private final PriceEventPublisher eventPublisher;
    private final PriceEventService eventService;

    public Price createPrice(Price price) {
        log.debug("Checking if price already exists for product {}, brand {}, startDate {}",
                price.getProductId(), price.getBrandId(), price.getStartDate());

        Optional<Price> existingPrice = priceRepository.findByProductIdAndBrandIdAndStartDate(
                price.getProductId(), price.getBrandId(), price.getStartDate());

        if (existingPrice.isPresent()) {
            log.error("Price already exists for this product, brand, and date: {}", existingPrice.get());
            throw new InvalidPriceRequestException("The price for this product, brand, and date already exists.");
        }

        List<Price> overlaps = priceRepository.findOverlappingPrices(
                price.getProductId(), price.getBrandId(), price.getStartDate(), price.getEndDate());

        if (!overlaps.isEmpty()) {
            log.error("Overlapping prices found: {}", overlaps);
            throw new InvalidPriceRequestException("The new price overlaps with an existing price range.");
        }

        log.debug("Saving new price...");
        try {
            Price savedPrice = priceRepository.save(price);
            log.info("Price saved successfully: {}", savedPrice);
            eventService.recordEvent(savedPrice, "CREATED");
            eventPublisher.publishPriceUpdated(savedPrice.getId());
            return savedPrice;
        } catch (Exception e) {
            log.error("Error saving the price: {}", e.getMessage(), e);
            throw new InvalidPriceRequestException("Error saving the price. Please verify the data.");
        }
    }

    public void deletePrice(Long id) {
        log.debug("Attempting to delete price with id {}", id);
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException("Price with id " + id + " not found"));

        priceRepository.delete(price);
        log.info("Price deleted successfully: {}", id);
        eventService.recordEvent(price, "DELETED");
        eventPublisher.publishPriceUpdated(id);
    }

    public Price updatePrice(Long id, Price price) {
        log.debug("Attempting to update price with id {}", id);

        Price existingPrice = priceRepository.findById(id)
                .orElseThrow(() -> new PriceNotFoundException("Price with id " + id + " not found"));

        Optional<Price> duplicate = priceRepository.findByProductIdAndBrandIdAndStartDate(
                price.getProductId(), price.getBrandId(), price.getStartDate());

        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            log.error("Price already exists for this product, brand, and date: {}", duplicate.get());
            throw new InvalidPriceRequestException("The price for this product, brand, and date already exists.");
        }

        try {
            price.setId(id);
            Price savedPrice = priceRepository.save(price);
            log.info("Price updated successfully: {}", savedPrice);
            eventService.recordEvent(savedPrice, "UPDATED");
            eventPublisher.publishPriceUpdated(savedPrice.getId());
            return savedPrice;
        } catch (Exception e) {
            log.error("Error updating the price: {}", e.getMessage(), e);
            throw new PriceServiceException("Error updating the price. Please verify the data.");
        }
    }
}
