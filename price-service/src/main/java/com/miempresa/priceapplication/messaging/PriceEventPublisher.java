package com.miempresa.priceapplication.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPriceUpdated(Long priceId) {
        try {
            rabbitTemplate.convertAndSend("price.events", "price.updated", priceId);
            log.info("Price update event published: {}", priceId);
        } catch (Exception e) {
            log.warn("Failed to publish price update event: {}", e.getMessage());
        }
    }
}
