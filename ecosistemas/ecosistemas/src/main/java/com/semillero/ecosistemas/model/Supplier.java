package com.semillero.ecosistemas.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Supplier extends User {

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 3)
    @JsonManagedReference
    private List<Product> productList = new ArrayList<>();

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();
        setRole(Role.SUPPLIER);
        setDeleted(false);
    }
}