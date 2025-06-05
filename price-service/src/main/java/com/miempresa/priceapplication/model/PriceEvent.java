package com.miempresa.priceapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PriceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long priceId;

    private String eventType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String eventData;

    private LocalDateTime timestamp;
}
