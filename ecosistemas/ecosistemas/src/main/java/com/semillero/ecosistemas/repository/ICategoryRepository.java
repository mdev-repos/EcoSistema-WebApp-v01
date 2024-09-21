package com.semillero.ecosistemas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.semillero.ecosistemas.model.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
}