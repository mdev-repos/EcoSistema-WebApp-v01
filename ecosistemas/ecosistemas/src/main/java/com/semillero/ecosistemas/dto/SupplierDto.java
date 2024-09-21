package com.semillero.ecosistemas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDto {
    private String name;
    private String lastname;
    private String email;
    private String picture;

    public SupplierDto(String name, String lastname, String email, String picture) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.picture = picture;
    }
}
