package com.semillero.ecosistemas.repository;

import com.semillero.ecosistemas.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findSupplierByEmail(String email);
}