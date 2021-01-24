package com.commerce.backend.service;

import com.commerce.backend.dao.ProductRepository;
import com.commerce.backend.dao.ProductVariantRepository;
import com.commerce.backend.error.exception.InvalidArgumentException;
import com.commerce.backend.error.exception.ResourceNotFoundException;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.entity.ProductCategory;
import com.commerce.backend.model.entity.ProductVariant;
import com.commerce.backend.model.response.product.ProductDetailsResponse;
import com.commerce.backend.model.response.product.ProductResponse;
import com.commerce.backend.model.response.product.ProductVariantResponse;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

   
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

   
   
   
   
    private Faker faker;


    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

  

   

   
  

    //@Test
    void it_should_get_all_product_variants() {

        // given
        Integer page = faker.number().randomDigitNotZero();
        Integer size = faker.number().randomDigitNotZero();
        String sort = faker.bool().bool() ? "lowest" : "highest";
        String category = faker.lorem().word();
        Float minPrice = (float) faker.number().randomNumber();
        Float maxPrice = minPrice + (float) faker.number().randomNumber();
        String color = faker.color().name();

        ProductVariant productVariant = new ProductVariant();
        List<ProductVariant> productVariantList = new ArrayList<>();
        productVariantList.add(productVariant);

        Page<ProductVariant> productVariantPage = new PageImpl<>(productVariantList);

        ProductVariantResponse productVariantResponseExpected = new ProductVariantResponse();

        given(productVariantRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(productVariantPage);
      //  given(productVariantResponseConverter.apply(any(ProductVariant.class))).willReturn(productVariantResponseExpected);

        // when
        List<ProductVariantResponse> productVariantResponseList = productService.getAll(page, size, sort, category, minPrice, maxPrice);

        // then
        then(productVariantResponseList.size()).isEqualTo(productVariantList.size());
        productVariantResponseList.forEach(productVariantResponse -> then(productVariantResponse).isEqualTo(productVariantResponseExpected));
    }

    //@Test
    void it_should_get_all_product_variants_with_no_sort() {

        // given
        Integer page = faker.number().randomDigitNotZero();
        Integer size = faker.number().randomDigitNotZero();
        String sort = null;
        String category = faker.lorem().word();
        Float minPrice = (float) faker.number().randomNumber();
        Float maxPrice = minPrice + (float) faker.number().randomNumber();
        String color = faker.color().name();

        ProductVariant productVariant = new ProductVariant();
        List<ProductVariant> productVariantList = new ArrayList<>();
        productVariantList.add(productVariant);

        Page<ProductVariant> productVariantPage = new PageImpl<>(productVariantList);

        ProductVariantResponse productVariantResponseExpected = new ProductVariantResponse();

        given(productVariantRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(productVariantPage);

        // when
        List<ProductVariantResponse> productVariantResponseList = productService.getAll(page, size, sort, category, minPrice, maxPrice);

        // then
        then(productVariantResponseList.size()).isEqualTo(productVariantList.size());
        productVariantResponseList.forEach(productVariantResponse -> then(productVariantResponse).isEqualTo(productVariantResponseExpected));
    }

    //@Test
    void it_should_throw_exception_when_invalid_sort() {

        // given
        Integer page = faker.number().randomDigitNotZero();
        Integer size = faker.number().randomDigitNotZero();
        String sort = faker.random().hex();
        String category = faker.lorem().word();
        Float minPrice = (float) faker.number().randomNumber();
        Float maxPrice = minPrice + (float) faker.number().randomNumber();
        String color = faker.color().name();


        // when, then
        assertThatThrownBy(() -> productService.getAll(page, size, sort, category, minPrice, maxPrice))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Invalid sort parameter");
    }

  


   

    //@Test
    void it_should_get_searched_products() {

        // given
        String keyword = faker.lorem().word();
        Integer page = faker.number().randomDigitNotZero();
        Integer size = faker.number().randomDigitNotZero();

        List<Product> productList = Stream.generate(Product::new)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toList());

        given(productRepository.findAllByNameContainingIgnoreCase(keyword, PageRequest.of(page, size))).willReturn(productList);

        // when
        List<ProductResponse> productResponseList = productService.searchProductDisplay(keyword, page, size);

        // then
        then(productResponseList.size()).isEqualTo(productList.size());

    }

    //@Test
    void it_should_throw_exception_when_searched_products_with_null_page_and_null_size() {

        //given
        String keyword = "harry";

        // when, then
        assertThatThrownBy(() -> productService.searchProductDisplay(keyword, null, faker.number().randomDigitNotZero()))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Page and size are required");

        assertThatThrownBy(() -> productService.searchProductDisplay(keyword, faker.number().randomDigitNotZero(), null))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Page and size are required");

    }


}