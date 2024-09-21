package com.semillero.ecosistemas.dto;

import com.semillero.ecosistemas.model.Publication;
import jakarta.persistence.ElementCollection;
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
@Builder
public class PublicationDTO {
    private Long id;

    @NotBlank(message = "El campo título es obligatorio.")
    private String title;

    @NotBlank(message = "El campo descripción es obligatorio.")
    @Length(max = 2500, message = "La cantidad maxima de caracteres es 2500.")
    private String description; // Consultar si existe una cantidad minima

    private boolean deleted;

    private LocalDate creationDate = LocalDate.now();
    private Integer viewCount = 0;

    @ElementCollection
    @Size(message = "No se pueden adjuntar más de 3 archivos.", max = 3)
    private List<String> imagesURLs;

    private List<String> urlsToDelete;

    public static Publication toEntity(PublicationDTO publicationDto) {
        return Publication.builder()
                .id(publicationDto.getId())
                .title(publicationDto.getTitle())
                .description(publicationDto.getDescription())
                .deleted(publicationDto.isDeleted())
                .creationDate(publicationDto.getCreationDate())
                .imagesURLs(publicationDto.getImagesURLs())
                .viewCount(publicationDto.getViewCount())
                .build();
    }

    public static PublicationDTO fromEntity(Publication publication) {
        return PublicationDTO.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .description(publication.getDescription())
                .deleted(publication.isDeleted())
                .creationDate(publication.getCreationDate())
                .imagesURLs(publication.getImagesURLs())
                .viewCount(publication.getViewCount())
                .build();
    }
}
