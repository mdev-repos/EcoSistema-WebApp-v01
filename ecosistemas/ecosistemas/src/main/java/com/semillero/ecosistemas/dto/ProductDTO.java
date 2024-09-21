package com.semillero.ecosistemas.dto;

import com.semillero.ecosistemas.model.Category;
import com.semillero.ecosistemas.model.Country;
import com.semillero.ecosistemas.model.Province;
import com.semillero.ecosistemas.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    @NotBlank(message = "El campo nombre es obligatorio.")
    private String name;

    @NotBlank(message = "El campo descripción breve es obligatorio.")
    @Size(max = 50, message = "La cantidad máxima de caracteres es 50.")
    private String shortDescription;

    private Category category;

    @NotBlank(message = "El campo correo electrónico es obligatorio.")
    private String email;

    @NotBlank(message = "El campo teléfono/whatsapp es obligatorio.")
    private String phoneNumber;

    private String instagram;
    private String facebook;

    private Country country;
    private Province province;

    private String city;

    @NotBlank(message = "El campo descripción completa es obligatorio.")
    @Size(max = 300, message = "La cantidad máxima de caracteres es 300.")
    private String longDescription;


    public static Product toEntity(ProductDTO productDto) {
        return Product.builder()
                .name(productDto.getName())
                .shortDescription(productDto.getShortDescription())
                .category(productDto.getCategory())
                .email(productDto.getEmail())
                .phoneNumber(productDto.getPhoneNumber())
                .instagram(productDto.getInstagram())
                .facebook(productDto.getFacebook())
                .country(productDto.getCountry())
                .province(productDto.getProvince())
                .city(productDto.getCity())
                .longDescription(productDto.getLongDescription())
                .build();
    }

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .name(product.getName())
                .shortDescription(product.getShortDescription())
                .category(product.getCategory())
                .email(product.getEmail())
                .phoneNumber(product.getPhoneNumber())
                .instagram(product.getInstagram())
                .facebook(product.getFacebook())
                .country(product.getCountry())
                .province(product.getProvince())
                .city(product.getCity())
                .longDescription(product.getLongDescription())
                .build();
    }
}