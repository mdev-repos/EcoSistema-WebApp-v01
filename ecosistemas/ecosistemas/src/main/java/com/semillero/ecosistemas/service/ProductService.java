package com.semillero.ecosistemas.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.semillero.ecosistemas.dto.ProductDTO;
import com.semillero.ecosistemas.jwt.JwtService;
import com.semillero.ecosistemas.model.*;
import com.semillero.ecosistemas.repository.IProductRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JwtService jwtService;


    // Método para construir el ProductDTO
    @Override
    public ProductDTO buildProductDTO(
            String name,
            String shortDescription,
            Long categoryId,
            String email,
            String phoneNumber,
            String instagram,
            String facebook,
            Long countryId,
            Long provinceId,
            String city,
            String longDescription) {

        Category category = categoryService.findCategoryById(categoryId);
        Country country = countryService.getCountryById(countryId);
        Province province = provinceService.getProvinceById(provinceId);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setShortDescription(shortDescription);
        productDTO.setCategory(category);
        productDTO.setEmail(email);
        productDTO.setPhoneNumber(phoneNumber);
        productDTO.setInstagram(instagram != null ? instagram : "");
        productDTO.setFacebook(facebook != null ? facebook : "");
        productDTO.setCountry(country);
        productDTO.setProvince(province);
        productDTO.setCity(city);
        productDTO.setLongDescription(longDescription != null ? longDescription : "");

        return productDTO;
    }


    @Override
    public Product createProduct(ProductDTO productDTO,
                                 List<MultipartFile> files,
                                 String token) throws IOException {

        //Obtenemos el Usuario SUPPLIER que esta creando el producto a partir del TOKEN
        Long supplierId = extractSupplierIdFromToken(token);
        Supplier supplier = supplierService.findSupplierById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        //Chequeamos que la cantidad de productos del Supplier no haya alcanzado el limite de 3
        if(supplier.getProductList().size()>=3){
            return null;
        }
        else{
            //Subimos los Archivos de imagen a CLOUDINARY y creamos el ARRAY de URLs
            List<String> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                String imageURL = uploadImage(file);
                productImages.add(imageURL);
            }

            //Creamos el Producto y completamos sus variables con los campos del ProductDTO, el Array de URLs y el Supplier
            Product product = Product.builder()
                    .name(productDTO.getName())
                    .shortDescription(productDTO.getShortDescription())
                    .category(productDTO.getCategory())
                    .email(productDTO.getEmail())
                    .phoneNumber(productDTO.getPhoneNumber())
                    .instagram(productDTO.getInstagram())
                    .facebook(productDTO.getFacebook())
                    .country(productDTO.getCountry())
                    .province(productDTO.getProvince())
                    .city(productDTO.getCity())
                    .longDescription(productDTO.getLongDescription())
                    .imagesURLs(productImages)
                    .supplier(supplier)
                    .build();

            //Guardamos el Producto En Base de Datos
            productRepository.save(product);

            //Agregamos el producto a la lista de productos del Supplier
            List<Product>addProduct = supplier.getProductList();
            addProduct.add(product);

            //Retornamos el Producto para la Response
            return product;
        }
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO, List<String> URLsToDelete, List<MultipartFile> files) throws IOException {
        // Traer el producto original
        Product previousProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Lista de imágenes del producto
        List<String> newImages = new ArrayList<>(previousProduct.getImagesURLs());

        if (URLsToDelete != null && !URLsToDelete.isEmpty()) {
            // Iteramos sobre las URLs a eliminar
            for (String urlToDelete : URLsToDelete) {
                // Eliminación de imágenes que contienen la URL a eliminar
                newImages.removeIf(imageUrl -> imageUrl.contains(urlToDelete.trim()));

                // Eliminación de Imágenes obsoletas del Producto (en Cloudinary)
                this.deleteImageProduct(urlToDelete.trim());
            }
        }

        // Subimos los nuevos archivos de imagen a CLOUDINARY y agregamos la URL a la lista
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String imageURL = uploadImage(file);
                    newImages.add(imageURL);
                }
            }
        }

        // Verificar que la cantidad total de imágenes no exceda el máximo permitido
        if (newImages.isEmpty() || newImages.size() > 3) {
            throw new IllegalArgumentException("El producto debe contener al menos una imagen y no puede tener más de tres.");
        }

        // Seteamos los nuevos valores al Producto existente
        previousProduct.setName(productDTO.getName());
        previousProduct.setShortDescription(productDTO.getShortDescription());
        previousProduct.setCategory(productDTO.getCategory());
        previousProduct.setEmail(productDTO.getEmail());
        previousProduct.setPhoneNumber(productDTO.getPhoneNumber());
        previousProduct.setInstagram(productDTO.getInstagram() != null ? productDTO.getInstagram() : "");
        previousProduct.setFacebook(productDTO.getFacebook() != null ? productDTO.getFacebook() : "");
        previousProduct.setCountry(productDTO.getCountry());
        previousProduct.setProvince(productDTO.getProvince());
        previousProduct.setCity(productDTO.getCity());
        previousProduct.setLongDescription(productDTO.getLongDescription() != null ? productDTO.getLongDescription() : "");
        previousProduct.setImagesURLs(newImages);

        if (previousProduct.getStatus().toString().equals("REQUIERE_CAMBIOS") || previousProduct.getStatus().toString().equals("ACEPTADO")) {
            previousProduct.setStatus(Status.CAMBIOS_REALIZADOS);
            previousProduct.setStatusDate(LocalDateTime.now());
        }

        // Guardamos el Producto modificado en la Base de Datos y lo retornamos para la response
        return productRepository.save(previousProduct);
    }


    // Find
    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findProductByName(String name) {
        return productRepository.findByNameStartingWithIgnoreCase(name);
    }

    // Read
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsBySupplier(Long id) {
        Optional<Supplier> supplierOptional = supplierService.findSupplierById(id);
        if(supplierOptional.isPresent()){
            Supplier supplier = supplierOptional.get();
            return supplier.getProductList();
        }
        return null;
    }

    @Override
    public List<Product> getProductsByCategory(Long id) {
        Category category = categoryService.findCategoryById(id);
        List<Product> allProducts = this.getAllProducts();
        List<Product> categoryProducts = new ArrayList<>();
        for(Product product : allProducts){
            if(product.getCategory().equals(category)){
                categoryProducts.add(product);
            }
        }
        return categoryProducts;
    }

    @Override
    public List<Product> getProductsModifiedInLastWeek() {
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        return productRepository.findByStatusDateAfter(lastWeek);
    }

    @Override
    public void setFeedStatus(Long id, String status, String feedback) {
        Optional<Product> optionalProduct = this.findProductById(id);

        if (optionalProduct.isPresent()) {
            Product toFeedbackProduct = optionalProduct.get();
            try {
                Status newStatus = Status.valueOf(status.toUpperCase());
                toFeedbackProduct.setStatus(newStatus);
                toFeedbackProduct.setFeedback(feedback);
                toFeedbackProduct.setStatusDate(LocalDateTime.now());
                productRepository.save(toFeedbackProduct);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("El status proporcionado no es válido: " + status);
            }
        } else {
            throw new IllegalArgumentException("Producto no encontrado con el ID: " + id);
        }
    }



    @Override
    public String deleteProduct(Long id) throws IOException {
        Product product = productRepository.findById(id).orElse(null);
        if(product!=null){
            List<String>imagesToDelete = product.getImagesURLs();
            for(String url:imagesToDelete){
                this.deleteImageProduct(url);
            }
            productRepository.deleteById(id);
            return "Producto eliminado correctamente";
        }
        else return "El producto con el ID " + id + " no existe.";
    }

    //Aux Methods
    private String uploadImage(MultipartFile file) throws IOException {
        Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url");
    }

    public void deleteImageProduct(String url) throws IOException {
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

    private Long extractSupplierIdFromToken(String token) {
        Claims claims = jwtService.extractClaims(token);
        return claims.get("id", Long.class);
    }
}