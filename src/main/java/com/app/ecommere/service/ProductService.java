package com.app.ecommere.service;

import com.app.ecommere.entity.Category;
import com.app.ecommere.entity.Product;
import com.app.ecommere.exception.ResourceNotFoundException;
import com.app.ecommere.model.ProductDTO;
import com.app.ecommere.payload.request.CreateProductRequest;
import com.app.ecommere.payload.response.ProductResponse;
import com.app.ecommere.repository.CategoryRepository;
import com.app.ecommere.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;


    public void createNewProduct(CreateProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","id", req.getCategoryId()));

        Product product = Product.builder()
                .active(true)
                .count_buy(0)
                .category(category)
                .description(req.getDescription())
                .detail(req.getDetail())
                .name(req.getName())
                .price(req.getPrice())
                .quantity(req.getQuantity())
                .build();

        productRepository.save(product);

    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public ProductDTO getProductByID(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToDTO(product);
    }

    public ProductResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        List<Product> listOfProducts = products.getContent();
        log.info("{}",listOfProducts);
        List<ProductDTO> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());
        log.info("{}",content);
        ProductResponse productResponse = new ProductResponse();

        productResponse.setProducts(content);
        productResponse.setPageNo(products.getNumber() + 1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public ProductResponse getProductByPriceRange(BigDecimal startPrice,BigDecimal endPrice ,
                                                  int pageNo , int pageSize , String sortBy, String sortDir ){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Product> products = productRepository.getAllProductByRangePrice(startPrice,endPrice,pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDTO> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(content);
        productResponse.setPageNo(products.getNumber() + 1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public ProductResponse searchProductByName(String keyword, Integer pageNo,
                                             Integer pageSize, String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Product> products = productRepository.searchProductByName(keyword,pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDTO> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(content);
        productResponse.setPageNo(products.getNumber() + 1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public ProductResponse getAllProductByCategory(int categoryId, Integer pageNo,
                                                   Integer pageSize, String sortBy,String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Product> products = productRepository.getProductByCategory(categoryId,pageable);

        List<Product> listOfProducts = products.getContent();

        List<ProductDTO> content = listOfProducts.stream().map(this::mapToDTO).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(content);
        productResponse.setPageNo(products.getNumber() + 1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }

    public boolean existedByName (String name){
        return productRepository.existsByName(name);
    }


    private ProductDTO mapToDTO(Product product) {
        return mapper.map(product, ProductDTO.class);
    }

    private Product mapToEntity(ProductDTO productDTO) {
        return mapper.map(productDTO, Product.class);
    }


}
