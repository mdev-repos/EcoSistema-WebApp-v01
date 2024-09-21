package com.semillero.ecosistemas.repository;

import com.semillero.ecosistemas.model.Publication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByDeletedFalse();
    Publication findByImagesURLs(String imageUrl);
    @Query("SELECT p FROM Publication p WHERE p.deleted = false ORDER BY p.id DESC")
    List<Publication> findTop3ByDeletedFalse(Pageable pageable);
}
