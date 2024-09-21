package com.semillero.ecosistemas.repository;
import com.semillero.ecosistemas.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICountryRepository extends JpaRepository<Country, Long> {
}
