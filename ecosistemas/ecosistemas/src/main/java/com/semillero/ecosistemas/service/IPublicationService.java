package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.model.Publication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IPublicationService {
    Publication savePublication(PublicationDTO publicationDTO, List<MultipartFile> files, String token) throws IOException;

    Publication updatePublication(Long idPublication, PublicationDTO publicationDTO, List<MultipartFile> files, String token) throws IOException;

    Publication getPublicationById(Long id, String token) throws Exception;

    List<Publication> getAllPublications();

    List<Publication> getAllActivePublications();

    Publication markAsDeleted(Long id);

    void deleteImagePublication(String url) throws IOException;

    Publication getOnePublicationById(Long id);

    List<Publication> getLastThreePublications();
}
