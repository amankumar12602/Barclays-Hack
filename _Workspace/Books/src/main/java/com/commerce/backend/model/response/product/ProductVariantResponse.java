package com.commerce.backend.model.response.product;

import com.commerce.backend.model.dto.ProductVariantDTO;
import lombok.Data;


@Data
public class ProductVariantResponse {
    private Long id;
    private String name;
    private String url;
    private ProductVariantDTO productVariant;
    private Long bookID;
    private Float price;
    private String title;
    private String authors;
    private String language_code;
    private String isbn;
    private String average_rating;
    private String ratings_count;
}
