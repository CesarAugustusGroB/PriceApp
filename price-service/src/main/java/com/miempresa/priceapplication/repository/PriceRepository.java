package com.miempresa.priceapplication.repository;

import com.miempresa.priceapplication.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p FROM Price p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :date BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC")
    List<Price> findApplicablePrices(@Param("productId") Integer productId,
                                     @Param("brandId") Integer brandId,
                                     @Param("date") LocalDateTime date);


    @Query("SELECT p FROM Price p WHERE p.productId = :productId AND p.brandId = :brandId AND p.startDate = :startDate")
    Optional<Price> findByProductIdAndBrandIdAndStartDate(@Param("productId") Integer productId,
                                                          @Param("brandId") Integer brandId,
                                                          @Param("startDate") LocalDateTime startDate);

    @Query("SELECT p FROM Price p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND p.startDate < :endDate " +
            "AND p.endDate > :startDate")
    List<Price> findOverlappingPrices(@Param("productId") Integer productId,
                                      @Param("brandId") Integer brandId,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Price p WHERE p.productId = :productId AND p.brandId = :brandId ORDER BY p.startDate ASC")
    List<Price> findByProductIdAndBrandIdOrderByStartDateAsc(@Param("productId") Integer productId,
                                                             @Param("brandId") Integer brandId);

}
