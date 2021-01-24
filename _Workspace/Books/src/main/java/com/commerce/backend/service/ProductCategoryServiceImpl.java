package com.commerce.backend.service;

import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.response.category.ProductCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {


    @Autowired
    public ProductCategoryServiceImpl() {
    }


    @Override
    public List<ProductCategoryResponse> findAllByOrderByName() {
        List<ProductCategoryResponse> productCategories = null;
        if (productCategories.isEmpty()) {
            throw new ResourceNotFoundException("Could not find product categories");
        }
        return productCategories
                ;
    }
}
