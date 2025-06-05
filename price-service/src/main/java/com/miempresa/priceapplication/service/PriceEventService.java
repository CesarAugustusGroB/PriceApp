package com.miempresa.priceapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.model.PriceEvent;
import com.miempresa.priceapplication.repository.PriceEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceEventService {

    private final PriceEventRepository repository;
    private final ObjectMapper objectMapper;

    public void recordEvent(Price price, String type) {
        try {
            String data = objectMapper.writeValueAsString(price);
            PriceEvent event = new PriceEvent(null, price.getId(), type, data, LocalDateTime.now());
            repository.save(event);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize price event: {}", e.getMessage());
        }
    }

    public List<PriceEvent> getEvents(Long priceId) {
        return repository.findByPriceIdOrderByTimestampAsc(priceId);
    }
}
