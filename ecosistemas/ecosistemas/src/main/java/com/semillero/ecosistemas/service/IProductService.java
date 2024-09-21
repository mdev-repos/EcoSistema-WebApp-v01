package com.semillero.ecosistemas.service;

import com.semillero.ecosistemas.dto.ProductDTO;
import com.semillero.ecosistemas.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IProductService {
        // Create
        public ProductDTO buildProductDTO(String name,
                                          String shortDescription,
                                          Long categoryId,
                                          String email,
                                          String phoneNumber,
                                          String instagram,
                                          String facebook,
                                          Long countryId,
                                          Long provinceId,
                                          String city,
                                          String longDescription);
        
        public Product createProduct(ProductDTO productDTO, List<MultipartFile> files, String token) throws IOException;

        // Find
        public Optional<Product> findProductById(Long id);
        public List<Product> findProductByName(String name);

        // Read
        public List<Product> getAllProducts();
        public List<Product> getProductsBySupplier(Long id);
        public List<Product> getProductsByCategory(Long id);
        public List<Product> getProductsModifiedInLastWeek();

        // Update
        public void setFeedStatus(Long id, String status, String feedback);
        public Product updateProduct(Long id, ProductDTO productDTO, List<String> URLsToDelete, List<MultipartFile> files) throws IOException;

        // Delete
        public String deleteProduct(Long id) throws IOException;
    }