package com.semillero.ecosistemas.dto;

import com.semillero.ecosistemas.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistanceResult {
    private Product product;
    private double distance;

    public DistanceResult(Product product, double distance) {
        this.product = product;
        this.distance = distance;
    }
}
