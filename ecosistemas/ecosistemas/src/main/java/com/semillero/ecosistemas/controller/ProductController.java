package com.semillero.ecosistemas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semillero.ecosistemas.dto.ProductDTO;
import com.semillero.ecosistemas.model.Product;
import com.semillero.ecosistemas.model.Supplier;
import com.semillero.ecosistemas.service.IProductService;
import com.semillero.ecosistemas.service.ICategoryService;
import com.semillero.ecosistemas.service.ICountryService;
import com.semillero.ecosistemas.service.IProvinceService;
import com.semillero.ecosistemas.service.ISupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Producto", description = "Listado de operaciones de la entidad Product")
public class ProductController {

    //Dependency Injection
    @Autowired
    private IProductService productService;
    @Autowired
    private ISupplierService supplierService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICountryService countryService;
    @Autowired
    private IProvinceService provinceService;

    //CREATE A PRODUCT (SUPPLIER) / CREAR PRODUCTO (USUARIO PROVEEDOR)
    @PreAuthorize("hasRole('SUPPLIER')")
    @Operation(summary = "Crear un Producto", description = "Crea un Producto recibiendo los parametros establecidos, los arhivos de imagenes y el token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados."),
            @ApiResponse(responseCode = "500", description = "Error interno del Servidor.")
    })
    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "instagram") String instagram,
            @RequestParam(required = false, name = "facebook") String facebook,
            @RequestParam("countryId") Long countryId,
            @RequestParam("provinceId") Long provinceId,
            @RequestParam("city") String city,
            @RequestParam(required = false, name = "longDescription") String longDescription,
            @RequestParam("files") List<MultipartFile> files,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // Extraer el token del header
            String token = authorizationHeader.replace("Bearer ", "");


            // Construir el ProductDTO
            ProductDTO productDTO = productService.buildProductDTO(
                    name,
                    shortDescription,
                    categoryId,
                    email,
                    phoneNumber,
                    instagram,
                    facebook,
                    countryId,
                    provinceId,
                    city,
                    longDescription
            );

            // Crear el producto usando el ProductDTO, archivos y token
            Product product = productService.createProduct(productDTO, files, token);

            if(product==null){
                throw new IllegalArgumentException("El Proveedor ya alcanzo el limite de 3 productos.");
            }
            else{
                return ResponseEntity.ok(product);
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    //UPDATE PRODUCT (SUPPLIER) / ACTUALIZAR PRODUCTO (USUARIO PROVEEDOR)
//    @PreAuthorize("hasRole('SUPPLIER')")
    @Operation(summary = "Actualizar un Producto", description = "Actualiza un Producto recibiendo el ID del Producto a modificar, los campos establecidos a modificar, el listado de URLs de las imágenes a eliminar (Opcional) y los archivos de imagenes a cargar (Opcional).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, error en el archivo o datos proporcionados."),
            @ApiResponse(responseCode = "500", description = "Error interno del Servidor.")
    })
    @SecurityRequirement(name = "Authorization")
    @PutMapping("/update/{productID}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productID,
            @RequestParam("name") String name,
            @RequestParam("shortDescription") String shortDescription,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "instagram") String instagram,
            @RequestParam(required = false, name = "facebook") String facebook,
            @RequestParam("countryId") Long countryId,
            @RequestParam("provinceId") Long provinceId,
            @RequestParam("city") String city,
            @RequestParam(required = false, name = "longDescription") String longDescription,
            @RequestParam(required = false, name = "URLsToDelete") String URLsToDeleteJson, // Recibimos el JSON como String
            @RequestParam(required = false, name = "files") List<MultipartFile> files) {

        try {
            List<String> URLsToDelete = new ArrayList<>();

            if(URLsToDeleteJson!=null){
                // Deserializar el JSON de URLsToDelete en una lista de Strings
                URLsToDelete = new ObjectMapper().readValue(URLsToDeleteJson, List.class);
            }

            // Construir el ProductDTO
            ProductDTO productDTO = productService.buildProductDTO(
                    name,
                    shortDescription,
                    categoryId,
                    email,
                    phoneNumber,
                    instagram,
                    facebook,
                    countryId,
                    provinceId,
                    city,
                    longDescription
            );

            // Pasar la lista deserializada al servicio
            return ResponseEntity.ok(productService.updateProduct(productID, productDTO, URLsToDelete, files));

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    //GET SUPPLIER'S PRODUCTS (SUPPLIER) / OBTENER LOS PRODUCTOS DE UN PROVEEDOR (USUARIO PROVEEDOR)
    @PreAuthorize("hasRole('SUPPLIER')")
    @Operation(summary = "Obtener los Productos de un Proveedor mediante ID", description = "Devuelve la lista de productos del Proveedor ingreasdo.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{supplierID}")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable Long supplierID){
        List<Product>productsBySupplier = productService.getProductsBySupplier(supplierID);
        return ResponseEntity.ok(productsBySupplier);
    }

    //DELETE PRODUCT (SUPPLIER) / ELIMINAR PRODUCTO (USUARIO PROVEEDOR)
    @PreAuthorize("hasRole('SUPPLIER')")
    @Operation(summary = "Eliminar Producto por ID", description = "Elimina un Producto mediante su ID.")
    @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente.")
    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/delete/{productID}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productID) throws IOException {
        String response = productService.deleteProduct(productID);
        return ResponseEntity.ok(response);
    }

    //FIND PRODUCT BY ID (ADMIN) / OBTENER PRODUCTO POR ID (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPLIER')")
    @Operation(summary = "Obtener Producto por ID", description = "Devuelve un Producto por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/find/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id){
        Optional<Product> optionalProduct = productService.findProductById(id);
        return optionalProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //GET ALL PRODUCTS (ADMIN) / OBTENER TODOS LOS PRODUCTOS (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener todos los Productos", description = "Devuelve la lista con todos los productos en base de datos.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product>allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    //GET PRODUCTS WITH "REVISION_INICIAL" (ADMIN) / OBTENER LOS PRODUCTOS EN "REVISION_INICIAL" (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener los Productos 'En Revision'", description = "Devuelve la lista con todos los productos con Estado = En revision.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/revision")
    public ResponseEntity<List<Product>> getProductsToRevision(){
        List<Product>allProducts = productService.getAllProducts();
        List<Product>revisionProducts = new ArrayList<>();
        for(Product product:allProducts){
            if(product.getStatus().toString().equals("REVISION_INICIAL")&&!product.getDeleted()){
                revisionProducts.add(product);
            }
        }
        return ResponseEntity.ok(revisionProducts);
    }

    //GET PRODUCTS WITH "CAMBIOS_REALIZADOS" (ADMIN) / OBTENER PRODUCTOS CON "CAMBIOS_REALIZADOS" (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener los Productos con 'Cambios Realizados'", description = "Devuelve la lista con todos los productos con Estado = Cambios Realizados.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @SecurityRequirement(name = "Authorization")
    @GetMapping("/withchanges")
    public ResponseEntity<List<Product>> getProductsWithChanges(){
        List<Product>allProducts = productService.getAllProducts();
        List<Product>changedProducts = new ArrayList<>();
        for(Product product:allProducts){
            if(product.getStatus().toString().equals("CAMBIOS_REALIZADOS")&&!product.getDeleted()){
                changedProducts.add(product);
            }
        }
        return ResponseEntity.ok(changedProducts);
    }

    //SEND FEEDBACK AND SET STATUS (ADMIN) / ENVIAR FEEDBACK Y ACTUALIZAR STATUS (USUARIO ADMINISTRADOR)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modificar el Status y otorgar Feedback al Producto", description = "Modifica el Status y permite entregar un Feedback al Producto mediante su ID.")
    @ApiResponse(responseCode = "200", description = "Se ha actualizado el STATUS del producto y se ha enviado el feedback al Proveedor.")
    @SecurityRequirement(name = "Authorization")
    @PatchMapping("/feedback/{productID}")
    public ResponseEntity<String> sendFeedbackStatus(@PathVariable Long productID,
                                                     @RequestParam String status,
                                                     @RequestParam String feedback){
        productService.setFeedStatus(productID, status, feedback);
        return ResponseEntity.ok("Se ha actualizado el STATUS del producto y se ha enviado el feedback al Proveedor.");
    }

    //GET ALL VISIBLE PRODUCTS (ANY) / VER TODOS LOS PRODUCTOS ACEPTADOS (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Obtener los Productos 'Aceptados'", description = "Devuelve la lista con todos los productos con Estado = Aceptado.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @GetMapping
    public ResponseEntity<List<Product>> getActiveProducts(){
        List<Product>allProducts = productService.getAllProducts();
        List<Product>acceptedProducts = new ArrayList<>();
        for(Product product:allProducts){
            if(product.getStatus().toString().equals("ACEPTADO")&&!product.getDeleted()){
                acceptedProducts.add(product);
            }
        }
        return ResponseEntity.ok(acceptedProducts);
    }

    //GET PRODUCTS BY CATEGORY (ANY) / OBTENER LOS PRODUCTOS POR CATEGORIA (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Obtener los Productos por Categoria (ID de Categoria)", description = "Devuelve la lista con todos los productos pertenecientes a la categoria ingresada.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryID){
        List<Product>productsByCategory = productService.getProductsByCategory(categoryID);
        List<Product>acceptedProducts = new ArrayList<>();
        for(Product product:productsByCategory){
            if(product.getStatus().toString().equals("ACEPTADO")&&!product.getDeleted()){
                acceptedProducts.add(product);
            }
        }
        return ResponseEntity.ok(acceptedProducts);
    }

    //FIND PRODUCT BY NAME (SEARCHBAR - ANY) / BUSCAR PRODUCTOS POR SU NOMBRE (CUALQUIERA SIN LOGUEAR)
    @Operation(summary = "Obtener un Producto por nombre", description = "Devuelve una lista con todos los productos cuyo nombre comience con el campo de texto ingresado.")
    @ApiResponse(responseCode = "200", description = "Lista de Productos obtenida exitosamente.")
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Product>> findProductByName(@PathVariable String name){
        List<Product> allProducts = productService.findProductByName(name);
        List<Product> acceptedProducts = new ArrayList<>();
        for(Product product:allProducts){
            if(product.getStatus().toString().equals("ACEPTADO")){
                acceptedProducts.add(product);
            }
        }
        return ResponseEntity.ok(acceptedProducts);
    }
}