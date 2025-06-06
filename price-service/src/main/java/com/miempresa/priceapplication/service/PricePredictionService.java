package com.miempresa.priceapplication.service;

import com.miempresa.priceapplication.exception.PriceNotFoundException;
import com.miempresa.priceapplication.model.Price;
import com.miempresa.priceapplication.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricePredictionService {

    private final PriceRepository priceRepository;

    public BigDecimal predictPrice(Integer productId, Integer brandId, LocalDateTime date) {
        List<Price> prices = priceRepository.findByProductIdAndBrandIdOrderByStartDateAsc(productId, brandId);
        if (prices.isEmpty()) {
            throw new PriceNotFoundException("No price history for prediction");
        }

        // Convert dates to epoch seconds and prices to double
        int n = prices.size();
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            Price p = prices.get(i);
            x[i] = p.getStartDate().toEpochSecond(ZoneOffset.UTC);
            y[i] = p.getPrice().doubleValue();
        }

        double meanX = 0;
        double meanY = 0;
        for (int i = 0; i < n; i++) {
            meanX += x[i];
            meanY += y[i];
        }
        meanX /= n;
        meanY /= n;

        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < n; i++) {
            double dx = x[i] - meanX;
            numerator += dx * (y[i] - meanY);
            denominator += dx * dx;
        }

        if (denominator == 0) {
            // All timestamps equal - return average price
            return BigDecimal.valueOf(meanY).setScale(2, RoundingMode.HALF_UP);
        }

        double slope = numerator / denominator;
        double intercept = meanY - slope * meanX;

        double predicted = intercept + slope * date.toEpochSecond(ZoneOffset.UTC);
        return BigDecimal.valueOf(predicted).setScale(2, RoundingMode.HALF_UP);
    }
}
