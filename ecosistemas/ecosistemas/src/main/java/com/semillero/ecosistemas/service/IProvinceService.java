package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Province;

import java.util.List;

public interface IProvinceService {

    Province getProvinceById(Long id);
    List<Province> getProvincesByCountryId(Long countryId);
}
