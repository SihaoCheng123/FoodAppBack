package com.app.foodapp.controllers;

import com.app.foodapp.models.Product;
import com.app.foodapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final String UPLOAD_DIR= "uploads/";

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Optional<Product> optProduct = this.productService.getProductById(id);
        return optProduct.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("price") BigDecimal price,
        @RequestParam("file") MultipartFile file){

        if (file.isEmpty()){
            return ResponseEntity.badRequest().body(null); //BadRequest es error 400
        }
        try{
            Files.createDirectories(Paths.get(UPLOAD_DIR)); //Si no existe, lo crea

            //Subo un archivo que se llama archivo1.png
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); //modificar el nombre del archivo - 0832423_archivo1.png
            String filePath = UPLOAD_DIR + fileName; //uplads el archivo
            file.transferTo(new File(filePath)); //Mever el archivo subido a la carpeta uploads

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImage(filePath); //almacenar la ruta completa de la ubicacion del archivo

            return ResponseEntity.ok(this.productService.createProduct(product));

        }catch (IOException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
