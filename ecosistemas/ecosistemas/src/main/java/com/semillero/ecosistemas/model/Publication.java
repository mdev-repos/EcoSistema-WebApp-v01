package com.semillero.ecosistemas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo título es obligatorio.")
    private String title;

    @NotBlank(message = "El campo descripción es obligatorio.")
    @Length(max = 2500, message = "La cantidad máxima de caracteres es 2500.")
    private String description;

    private boolean deleted;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate creationDate = LocalDate.now();

    @ElementCollection
    @Size(message = "No se pueden adjuntar más de 3 archivos.", max = 3)
    private List<String> imagesURLs;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @JsonBackReference
    private Admin creatorUser;

    private Integer viewCount = 0;

    public void incrementViewCount() {
        this.viewCount++;
    }
}