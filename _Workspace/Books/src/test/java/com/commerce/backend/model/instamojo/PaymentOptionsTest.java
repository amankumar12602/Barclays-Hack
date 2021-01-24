package com.commerce.backend.model.instamojo;

import com.google.gson.Gson;
import com.commerce.backend.api.ApiContext;
import com.commerce.backend.api.PaymentOrderBuilder;
import com.commerce.backend.error.exception.ConnectionException;
import com.commerce.backend.error.exception.HTTPException;
import com.commerce.backend.util.Constants;
import com.commerce.backend.util.HttpUtils;
import com.commerce.backend.api.TestConstants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import java.util.HashMap;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class PaymentOptionsTest {

    private String PAYMENT_ORDER_ENDPOINT;
    private Map<String, String> headers = new HashMap<>();
    private Gson gson = new Gson();
    private ApiContext context;

    private PaymentOrderResponse orderResponse = new PaymentOrderResponse();

    @Before
    public void setUp() throws IOException, HTTPException, ConnectionException {

        ApiContext.ClearInstance();
        context = ApiContext.create(
            TestConstants.CLIENT_ID, TestConstants.CLIENT_SECRET,
            TestConstants.USERNAME, TestConstants.PASSWORD, ApiContext.Mode.TEST
        );

        PAYMENT_ORDER_ENDPOINT = context.getApiPath(Constants.PATH_PAYMENT_ORDER);
        PaymentOrder order = new PaymentOrderBuilder().build();

        headers.put(Constants.HEADER_AUTHORIZATION, context.getAuthorization());

        String response = HttpUtils.post(PAYMENT_ORDER_ENDPOINT, headers, gson.toJson(order));

        orderResponse = gson.fromJson(response, PaymentOrderResponse.class);

    }

    @Test
    public void testPaymentOrderModel() throws IOException {

        PaymentOptions options = orderResponse.getPaymentOptions();

        URL paymentUrl = new URL(options.getPaymentUrl());
        String expectedPaymentUrlPath = "/@" + TestConstants.USERNAME + "/" + orderResponse.getPaymentOrder().getId();

        assertEquals(expectedPaymentUrlPath, paymentUrl.getPath());

    }
}
