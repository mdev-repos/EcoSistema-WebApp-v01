package com.semillero.ecosistemas.repository;
import com.semillero.ecosistemas.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProvinceRepository extends JpaRepository<Province, Long> {
    List<Province> findByCountryId(Long countryId);
}
