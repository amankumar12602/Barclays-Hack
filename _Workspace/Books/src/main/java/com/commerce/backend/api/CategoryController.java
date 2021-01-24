package com.commerce.backend.api;

import com.commerce.backend.model.dto.CategoryDTO;
import com.commerce.backend.model.response.category.ProductCategoryResponse;
import com.commerce.backend.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController extends PublicApiController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public CategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping(value = "/category")
    public ResponseEntity<List<ProductCategoryResponse>> getAllCategories() {
    	ProductCategoryResponse res= new ProductCategoryResponse();
    	CategoryDTO cat= new CategoryDTO("Books");
    	res.setCategory(cat);
        List<ProductCategoryResponse> productCategories = new ArrayList<>();
        productCategories.add(res);
        return new ResponseEntity<>(productCategories, HttpStatus.OK);
    }

}
