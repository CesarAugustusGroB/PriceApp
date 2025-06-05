package com.miempresa.priceapplication.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCustomerUpdated(Long customerId) {
        try {
            rabbitTemplate.convertAndSend("customer.events", "customer.updated", customerId);
            log.info("Customer update event published: {}", customerId);
        } catch (Exception e) {
            log.warn("Failed to publish customer update event: {}", e.getMessage());
        }
    }
}
