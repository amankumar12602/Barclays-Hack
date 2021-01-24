package com.commerce.backend.service;

import com.commerce.backend.model.response.color.ProductColorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductColorServiceImpl implements ProductColorService {


    @Autowired
    public ProductColorServiceImpl( ) {
    }


    @Override
    public List<ProductColorResponse> findAll() {return null;}
}
