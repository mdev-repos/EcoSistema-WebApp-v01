package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.NominatimResponse;
import com.semillero.ecosistemas.model.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    @Value("${nominatim.api.url}")
    private String nominatimApiUrl;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Coordinates getCoordinatesByName(String placeName) {
        String adjustedPlaceName = placeName;

        String url = UriComponentsBuilder.fromHttpUrl(nominatimApiUrl)
                .queryParam("q", adjustedPlaceName)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .toUriString();

        NominatimResponse[] response = restTemplate.getForObject(url, NominatimResponse[].class);

        if (response == null || response.length == 0) {
            logger.error("No coordinates found for the given place name: {}", placeName);
            throw new IllegalArgumentException("No coordinates found for the given place name.");
        }
        NominatimResponse result = response[0];
        return new Coordinates(result.getLat(), result.getLon());
    }
}
