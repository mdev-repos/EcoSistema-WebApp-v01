package com.semillero.ecosistemas.repository;

import com.semillero.ecosistemas.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameStartingWithIgnoreCase(String name);

    // Consulta para obtener productos con statusDate en los últimos 7 días
    List<Product> findByStatusDateAfter(LocalDateTime lastWeek);

    List<Product> findByDeletedFalse();
}