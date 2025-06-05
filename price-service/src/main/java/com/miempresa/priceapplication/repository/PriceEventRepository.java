package com.miempresa.priceapplication.repository;

import com.miempresa.priceapplication.model.PriceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceEventRepository extends JpaRepository<PriceEvent, Long> {
    List<PriceEvent> findByPriceIdOrderByTimestampAsc(Long priceId);
}
