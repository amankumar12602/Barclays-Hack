package com.commerce.backend.api;

import com.commerce.backend.model.entity.User;
import com.commerce.backend.model.request.user.*;
import com.commerce.backend.service.TokenService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@WebMvcTest(PublicUserController.class)
@AutoConfigureWebClient
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class PublicUserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private TokenService tokenService;
    @Autowired
    private MockMvc mockMvc;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    //@Test
    void it_should_register_user() throws Exception {

        // given
        String email = String.format("%a@%a.com", faker.lorem().characters(1, 10), faker.lorem().characters(1, 10));
        String password = faker.lorem().characters(6, 52);
        String passwordRepeat = password + "";

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(password);
        registerUserRequest.setPasswordRepeat(passwordRepeat);

        User user = new User();


        given(userService.register(registerUserRequest)).willReturn(user);

        // when
        mockMvc.perform(post("/api/public/account/registration")
                .content(objectMapper.writeValueAsString(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(userService, times(1)).register(registerUserRequest);
        verify(tokenService, times(1)).createEmailConfirmToken(user);
    }

    //@Test
    void it_should_not_register_user_if_invalid_request() throws Exception {

        // given
        String email = String.valueOf(faker.number().randomDigitNotZero());
        String password = faker.lorem().characters(6, 52);
        String passwordRepeat = password + "";

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail(email);
        registerUserRequest.setPassword(password);
        registerUserRequest.setPasswordRepeat(passwordRepeat);

        User user = new User();


        given(userService.register(registerUserRequest)).willReturn(user);

        // when
        MvcResult result = mockMvc.perform(post("/api/public/account/registration")
                .content(objectMapper.writeValueAsString(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();


        // then
        verify(userService, times(0)).register(registerUserRequest);
        verify(tokenService, times(0)).createEmailConfirmToken(user);
        then(result.getResponse().getContentAsString()).contains("Invalid email");
    }

    //@Test
    void it_should_validate_email() throws Exception {

        // given
        String token = faker.lorem().word();
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setToken(token);

        // when
        mockMvc.perform(post("/api/public/account/registration/validate")
                .content(objectMapper.writeValueAsString(validateEmailRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        // then
        verify(tokenService, times(1)).validateEmail(token);
    }

 

   
  

  

    




}