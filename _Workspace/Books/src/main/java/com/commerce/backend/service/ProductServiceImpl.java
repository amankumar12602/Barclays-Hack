package com.commerce.backend.service;

import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.entity.ProductVariant;
import com.commerce.backend.model.response.product.ProductDetailsResponse;
import com.commerce.backend.model.response.product.ProductResponse;
import com.commerce.backend.model.response.product.ProductVariantResponse;
import com.commerce.backend.model.specs.ProductVariantSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    public ProductServiceImpl() {
    }

    @Override
    public ProductDetailsResponse findById(String Id) {
       Product product = new Product();
       ProductDetailsResponse response=new ProductDetailsResponse();
        if (Objects.isNull(product)) {
            throw new ResourceNotFoundException(String.format("Product not found with the url %s", Id));
        }
        List<ProductDetailsResponse> products=new ArrayList<ProductDetailsResponse>();
        ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<ProductDetailsResponse>> typeReference = new TypeReference<List<ProductDetailsResponse>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/books.json");
		try {
			 products = mapper.readValue(inputStream,typeReference);
			 int i=Integer.parseInt(Id); 
				response.setTitle(products.get(i).getTitle());
				response.setAuthors(products.get(i).getAuthors());
				 response.setBookID(products.get(i).getBookID());
				 response.setIsbn( products.get(i).getIsbn());
				 response.setPrice(products.get(i).getPrice());
				 response.setLanguage_code(products.get(i).getLanguage_code());
				 response.setAverage_rating(products.get(i).getAverage_rating());
				 response.setRatings_count(products.get(i).getRatings_count());
				
				
				 
				 
			 
			
		} catch (IOException e){
			System.out.println( e.getMessage());
		} 
        return response;
    }


    @Override
    public ProductVariant findProductVariantById(Long id) {
        ProductVariant productVariant = null;
        if (Objects.isNull(productVariant)) {
            throw new ResourceNotFoundException(String.format("Could not find any product variant with the id %d", id));
        }
        return productVariant;
    }

    @Override
    public List<ProductVariantResponse> getAll(Integer page, Integer size, String sort, String category, Float minPrice, Float maxPrice) {
        PageRequest pageRequest;
        if (Objects.nonNull(sort) && !sort.isBlank()) {
            Sort sortRequest = getSort(sort);
            if (Objects.isNull(sortRequest)) {
                throw new InvalidArgumentException("Invalid sort parameter");
            }
            pageRequest = PageRequest.of(page, size, sortRequest);
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        Specification<ProductVariant> combinations =
                Objects.requireNonNull(Specification.where(ProductVariantSpecs.withCategory(category)))
                        .and(ProductVariantSpecs.withCategory(category))
                        .and(ProductVariantSpecs.minPrice(minPrice))
                        .and(ProductVariantSpecs.maxPrice(maxPrice));
        List<ProductVariantResponse> products=new ArrayList<ProductVariantResponse>();
        ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<ProductVariantResponse>> typeReference = new TypeReference<List<ProductVariantResponse>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/books.json");
		try {
			 products = mapper.readValue(inputStream,typeReference);
			 for(int i=0;i<products.size();i++) {
				 products.get(i).getTitle();
				 products.get(i).getAuthors();
				 products.get(i).getBookID();
				 products.get(i).getIsbn();
				 products.get(i).getPrice();
				 products.get(i).getLanguage_code();
				 products.get(i).getAverage_rating();
				 products.get(i).getRatings_count();
				
				 
				 
			 }
			
		} catch (IOException e){
			System.out.println( e.getMessage());
		} 
        return products;
    }

    @Override
    public Long getAllCount(String category, Float minPrice, Float maxPrice, String color) {
        Specification<ProductVariant> combinations =
                Objects.requireNonNull(Specification.where(ProductVariantSpecs.withCategory(category)))
                        .and(ProductVariantSpecs.withCategory(category))
                        .and(ProductVariantSpecs.minPrice(minPrice))
                        .and(ProductVariantSpecs.maxPrice(maxPrice));
        List<ProductVariantResponse> products=new ArrayList<ProductVariantResponse>();

        ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<ProductVariantResponse>> typeReference = new TypeReference<List<ProductVariantResponse>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/books.json");
		try {
			 products = mapper.readValue(inputStream,typeReference);
		} catch (IOException e){
			System.out.println( e.getMessage());
		}  
        return (long) products.size();
    }

    @Override
    public List<ProductResponse> getRelatedProducts(String url) {return null;}

    @Override
    public List<ProductResponse> getNewlyAddedProducts() {return null;}

    @Override
    public List<ProductVariantResponse> getMostSelling() {return null;}

    @Override
    public List<ProductResponse> getInterested() {
    	return null;
    }

    @Override
    public List<ProductResponse> searchProductDisplay(String keyword, Integer page, Integer size) {
        if (Objects.isNull(page) || Objects.isNull(size)) {
            throw new InvalidArgumentException("Page and size are required");
        }
        PageRequest pageRequest = PageRequest.of(page, size);

        List<ProductResponse> products=new ArrayList<ProductResponse>();
        ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<ProductResponse>> typeReference = new TypeReference<List<ProductResponse>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/books.json");
		try {
			 products = mapper.readValue(inputStream,typeReference);
			 for(int i=0;i<products.size();i++) {
				 if( products.get(i).getTitle().contains(keyword)) {
					 products.get(i).getTitle();
					 products.get(i).getAuthors();
					 products.get(i).getBookID();
					 products.get(i).getIsbn();
					 products.get(i).getPrice();
					 products.get(i).getLanguage_code();
					 products.get(i).getAverage_rating();
					 products.get(i).getRatings_count();
				 }
				}
			
		} catch (IOException e){
			System.out.println( e.getMessage());
		} 
		
		
        
       return products;
    }


    private Sort getSort(String sort) {
    	
        switch (sort) {
            case "lowest":
                return Sort.by(Sort.Direction.ASC, "price");
            case "highest":
                return Sort.by(Sort.Direction.DESC, "price");
            default:
                return null;
        }
    }

}
