package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.model.Country;
import java.util.List;

public interface ICountryService {

    List<Country> getAllCountries();

    Country getCountryById(Long id);
}
