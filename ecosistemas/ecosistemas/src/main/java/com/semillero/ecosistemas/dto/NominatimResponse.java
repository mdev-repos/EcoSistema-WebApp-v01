package com.semillero.ecosistemas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NominatimResponse {
    private String lat;
    private String lon;

    @Override
    public String toString() {
        return "NominatimResponse{lat=" + lat + ", lon=" + lon + "}";
    }
}
