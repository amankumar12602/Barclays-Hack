package com.commerce.backend.api;

import com.commerce.backend.error.exception.ConnectionException;
import com.commerce.backend.error.exception.HTTPException;
import com.commerce.backend.model.instamojo.PaymentOrder;
import com.commerce.backend.model.instamojo.PaymentOrderResponse;
import com.commerce.backend.model.instamojo.Refund;
import com.commerce.backend.model.response.instamojo.ApiListResponse;



public class InstamojoExample {

    public static void main(String args[]) {

        
        ApiContext context = ApiContext.create("test_WC4skaFtjTDKVukMukURg0aILwcIu1vAzj9", "test_JiTvxOmmuds57QJt3BTqqEIGCTaREN0hCnP0z3WO9B6yu0ghAGTYBWhuSrvOVuuVjRly0BPpIj08WVkuIxV3NMvheto8lu9sCa3sbwLhpUCENmOxINxAwPLCwwG", ApiContext.Mode.TEST);
        Instamojo api = new InstamojoImpl(context);

       
        PaymentOrder order = new PaymentOrder();
        order.setName("Aman Kumar");
        order.setEmail("amankumarsingh2803@gmail.com");
        order.setPhone("12345678790");
        order.setCurrency("INR");
        order.setAmount(9D);
        order.setDescription("This is a test Instamojo.");
        order.setRedirectUrl("http://www.someexample.com");
        order.setWebhookUrl("http://localhost/");
        order.setTransactionId("dxg234");

        try {
            PaymentOrderResponse paymentOrderResponse = api.createPaymentOrder(order);
            System.out.println(paymentOrderResponse.getPaymentOrder().getStatus());

        } catch (HTTPException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());
            System.out.println(e.getJsonPayload());

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }

       
        try {
            PaymentOrder paymentOrder = api.getPaymentOrder("[PAYMENT_ORDER_ID]");
            System.out.println(paymentOrder.getId());
            System.out.println(paymentOrder.getStatus());

        } catch (HTTPException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());
            System.out.println(e.getJsonPayload());

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }

        /*
         * Get details of payment order when transaction id is given
         */
        try {
            PaymentOrder paymentOrder = api.getPaymentOrderByTransactionId("[TRANSACTION_ID]");
            System.out.println(paymentOrder.getId());
            System.out.println(paymentOrder.getStatus());

        } catch (HTTPException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());
            System.out.println(e.getJsonPayload());

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }

        /*
         * Get list of all payment orders
         */
        try {
            ApiListResponse<PaymentOrder> paymentOrders = api.getPaymentOrders(0, 10);

           
            for (PaymentOrder paymentOrder : paymentOrders.getResults()) {
                System.out.println("Result = " + paymentOrder.getStatus());
            }
            System.out.println(paymentOrders);

        } catch (HTTPException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());
            System.out.println(e.getJsonPayload());

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }


        
        Refund refund = new Refund();
        refund.setPaymentId("[PaymentId]");
        refund.setStatus("refunded");
        refund.setType("RFD");
        refund.setBody("This is a test refund.");
        refund.setRefundAmount(9D);
        refund.setTotalAmount(10D);

        try {
            Refund createdRefund = api.createRefund(refund);
            System.out.println(createdRefund.getId());
            System.out.println(createdRefund.getStatus());

        } catch (HTTPException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getMessage());
            System.out.println(e.getJsonPayload());

        } catch (ConnectionException e) {
            System.out.println(e.getMessage());
        }
    }
}
