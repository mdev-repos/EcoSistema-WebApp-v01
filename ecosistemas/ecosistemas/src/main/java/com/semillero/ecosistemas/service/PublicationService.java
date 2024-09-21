package com.semillero.ecosistemas.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semillero.ecosistemas.dto.PublicationDTO;
import com.semillero.ecosistemas.jwt.JwtService;
import com.semillero.ecosistemas.model.Admin;
import com.semillero.ecosistemas.model.Publication;
import com.semillero.ecosistemas.repository.IAdminRepository;
import com.semillero.ecosistemas.repository.IPublicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PublicationService implements IPublicationService {
    @Autowired
    private IPublicationRepository publicationRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IAdminRepository adminRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Publication savePublication(PublicationDTO publicationDTO, List<MultipartFile> files, String token) throws IOException {
        Long adminId = adminService.extractAdminIdFromToken(token);
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el admin con id: " + adminId));

        if (files == null || files.isEmpty() || files.stream().allMatch(file -> file.isEmpty())) {
            throw new IllegalArgumentException("Debe adjuntar al menos un archivo.");
        }
        if (files.size() > 3) {
            throw new IllegalArgumentException("No puede adjuntar más de 3 archivos.");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadImagePublication(file, imageUrls);
        }

        publicationDTO.setImagesURLs(imageUrls);
        Publication publication = PublicationDTO.toEntity(publicationDTO);
        publicationRepository.save(publication);
        publication.setCreatorUser(foundAdmin);

        foundAdmin.getPublicationList().add(publication);
        adminRepository.save(foundAdmin);

        return publication;
    }

    @Override
    public Publication updatePublication(Long id, PublicationDTO publicationDTO, List<MultipartFile> files, String token) throws IOException {
        Long adminId = adminService.extractAdminIdFromToken(token);
        Admin foundAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el admin con id: " + adminId));

        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));

        if (publicationDTO.getUrlsToDelete() != null && !publicationDTO.getUrlsToDelete().isEmpty()) {
            for (String url : publicationDTO.getUrlsToDelete()) {
                deleteImagePublication(url);
                foundPublication.getImagesURLs().remove(url);
            }
        }

        foundPublication.setTitle(publicationDTO.getTitle());
        foundPublication.setDescription(publicationDTO.getDescription());

        if (files != null && !files.isEmpty()) { // Solo si existen nuevos archivos
            List<String> existingImageUrls = foundPublication.getImagesURLs();
            List<String> newImageUrls = new ArrayList<>(existingImageUrls);
            for (MultipartFile file : files) {
                if (newImageUrls.size() >= 3) break;
                uploadImagePublication(file, newImageUrls);
            }
            foundPublication.setImagesURLs(newImageUrls);
        }

        return publicationRepository.save(foundPublication);
    }

    // OBTIENE UNA PUBLICACION SIN MODIFICAR LAS VIEWS
    @Override
    public Publication getOnePublicationById(Long id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Publicacion no encontrada con ID: " + id));
    }

    // OBTENER LAS 3 ULTIMAS PUBLICACIONES ACTIVAS ORDENADAS DESDE LA MAS RECIENTE HASTA LA MAS ANTIGUA
    @Override
    public List<Publication> getLastThreePublications() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));
        return publicationRepository.findTop3ByDeletedFalse(pageable);
    }

    // INCREMENTA 1 VIEW SI ES USER O SUPPLIER, NO INCREMENTA SI ES ADMIN
    @Override
    public Publication getPublicationById(Long id, String token) throws Exception {
        String role = null;

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token != null && !token.trim().isEmpty()) {
            try {
                role = jwtService.extractRole(token);
            } catch (Exception e) {
                throw new Exception("Error al extraer el rol del token.");
            }
        }

        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));

        if (role == null || !role.equals("ADMIN")) {
            foundPublication.incrementViewCount();
            publicationRepository.save(foundPublication);
        }
//        return foundPublication;
        return null;
    }

    @Override
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }

    @Override
    public List<Publication> getAllActivePublications() {
        return publicationRepository.findByDeletedFalse();
    }

    @Override
    public Publication markAsDeleted(Long id) {
        Publication foundPublication = publicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la publicación con id: " + id));
        if(foundPublication.isDeleted()){
            foundPublication.setDeleted(false);
        }else{
            foundPublication.setDeleted(true);
        }
        publicationRepository.save(foundPublication);
        return foundPublication;
    }

    @Override
    public void deleteImagePublication(String url) throws IOException {
        String publicId = extractPublicIdFromUrl(url);
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new IOException("Error al eliminar la imagen.");
        }
    }

    private String extractPublicIdFromUrl(String url) {
        int startIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf('.');
        return url.substring(startIndex, endIndex);
    }

    private void uploadImagePublication(MultipartFile file, List<String> imagesUrls) throws IOException {
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String newImageUrl = uploadResult.get("url").toString();
            imagesUrls.add(newImageUrl);
        }
    }

}
