package com.app.ecommere.controller;

import com.app.ecommere.model.ProductDTO;
import com.app.ecommere.payload.request.CreateProductRequest;
import com.app.ecommere.payload.response.ProductResponse;
import com.app.ecommere.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;

import static com.app.ecommere.utils.AppConstants.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public ProductResponse getAllProduct(@RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return productService.getAllProduct(pageNo, pageSize, sortBy, sortDir);

    }

    @PostMapping
    public ResponseEntity<?> newProduct(@Valid @RequestBody CreateProductRequest createProductRequest) {
        if (productService.existedByName(createProductRequest.getName())) {
            return new ResponseEntity<>("There already a product with this name!", HttpStatus.BAD_REQUEST);
        }

        productService.createNewProduct(createProductRequest);
        return new ResponseEntity<>("Create new product successfully", OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(productService.getProductByID(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") Integer id) {
        productService.delete(id);

        return new ResponseEntity<>("Product entity deleted successfully", OK);
    }

    @GetMapping("/search/price")
    public ProductResponse getAllProductByRangePrice(@RequestParam (name = "startPrice")BigDecimal startPrice,
                                                     @RequestParam (name = "endPirce")BigDecimal endPrice,
                                                     @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                                     @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return productService.getProductByPriceRange(startPrice,endPrice,pageNo, pageSize, sortBy, sortDir);

    }

    @GetMapping("/search/keyword")
    public ProductResponse getProductByName(@RequestParam(name = "keyword") String keyword,
                                            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return productService.searchProductByName(keyword,pageNo,pageSize,sortBy,sortDir);
    }
    @GetMapping("/category/{id}")
    public ProductResponse getProductByCategory(@PathVariable(name = "id")int categoryId,
                                            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                                            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return productService.getAllProductByCategory(categoryId,pageNo,pageSize,sortBy,sortDir);
    }
}
