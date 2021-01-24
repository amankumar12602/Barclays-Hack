package com.commerce.backend.api;


import com.commerce.backend.model.request.user.PasswordResetRequest;
import com.commerce.backend.model.request.user.UpdateUserAddressRequest;
import com.commerce.backend.model.request.user.UpdateUserRequest;
import com.commerce.backend.model.response.user.UserResponse;
import com.commerce.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(UserController.class)
@AutoConfigureWebClient
@WithMockUser
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    //@Test
    void it_should_get_user() throws Exception {

        // given
        UserResponse userResponse = new UserResponse();


        given(userService.fetchUser()).willReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).fetchUser();
        then(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(userResponse));
    }

    }