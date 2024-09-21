package com.semillero.ecosistemas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo nombre es obligatorio.")
    private String name;

    @NotBlank(message = "El campo descripción breve es obligatorio.")
    @Length(max = 50, message = "La cantidad máxima de caracteres es 50.")
    private String shortDescription;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @NotBlank(message = "El campo correo electronico es obligatorio.")
    private String email;

    @NotBlank(message = "El campo telefono/whatsapp es obligatorio.")
    private String phoneNumber;

    private String instagram;
    private String facebook;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id")
    private Province province;

    @NotBlank(message = "El campo ciudad es obligatorio.")
    private String city;

    @Length(max = 300, message = "La cantidad máxima de caracteres es 300.")
    private String longDescription;

    @ElementCollection
    @Size(max = 3)
    private List<String> imagesURLs;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;

    private Boolean deleted;
    private String feedback;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;

    private LocalDateTime statusDate;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.REVISION_INICIAL;
        }
        if (deleted == null) {
            deleted = false;
        }
        if (feedback == null) {
            feedback = "Proveedor en revisión";
        }
        statusDate = LocalDateTime.now();
    }
}