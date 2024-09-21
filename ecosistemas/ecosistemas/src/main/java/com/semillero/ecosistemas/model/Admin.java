package com.semillero.ecosistemas.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
public class Admin extends User{

    @OneToMany(mappedBy = "creatorUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Publication> publicationList = new ArrayList<>();

    @PrePersist
    @Override
    public void prePersist() {
        super.prePersist();

        setRole(Role.ADMIN);
        setDeleted(false);
        if (publicationList == null) {
            publicationList = new ArrayList<>();
        }
    }
}