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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class PaymentOrderTest {

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

        PaymentOrder order = orderResponse.getPaymentOrder();

        assertNotNull(order.getTransactionId());
        assertNotNull(order.getCreatedAt());
        assertEquals(order.getWebhookUrl(), PaymentOrderBuilder.PAYMENT_ORDER_WEBHOOK_URL);
        assertEquals(order.getRedirectUrl(), PaymentOrderBuilder.PAYMENT_ORDER_REDIRECT_URL);

        URL resourceUri = new URL(order.getResourceUri());
        String expectedResourceUri = "/v2/gateway/orders/id:" + order.getId() + "/";

        assertEquals(expectedResourceUri, resourceUri.getPath());

    }

}
