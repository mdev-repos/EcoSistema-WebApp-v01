package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.DashboardDTO;

import java.util.List;
import java.util.Map;

public interface IDashboardService {
    List<DashboardDTO> getLastFivePublications();
    List<DashboardDTO> getFiveMostViewedPublications();
    Map<String, Long> getSupplierCountByCategory();
    Map<String, Long> getCreatedProducts();
    Map<String, Object> getDashboardInfo();
}
