package com.commerce.backend.api;

import com.commerce.backend.filter.PaymentRequestFilter;
import com.commerce.backend.filter.PayoutFilter;
import com.commerce.backend.error.exception.*;
import com.commerce.backend.model.instamojo.Invoice;
import com.commerce.backend.model.instamojo.PaymentOrder;
import com.commerce.backend.model.instamojo.PaymentOrderResponse;
import com.commerce.backend.model.instamojo.PaymentRequest;
import com.commerce.backend.model.instamojo.Payout;
import com.commerce.backend.model.instamojo.Refund;
import com.commerce.backend.model.response.instamojo.*;

import java.util.Map;


public interface Instamojo {

   
    PaymentOrderResponse createPaymentOrder(PaymentOrder paymentOrder) throws ConnectionException, HTTPException;

  
    PaymentOrder getPaymentOrder(String id) throws ConnectionException, HTTPException;

    
    PaymentOrder getPaymentOrderByTransactionId(String transactionId) throws ConnectionException, HTTPException;

   
    ApiListResponse<PaymentOrder> getPaymentOrders(int page, int limit) throws ConnectionException, HTTPException;

   
    Refund createRefund(Refund refund) throws ConnectionException, HTTPException;

   
    String generateWebhookSignature(Map<String, String> data, String salt);

   
    ApiListResponse<Invoice> getInvoices(int page, int limit) throws ConnectionException, HTTPException;

   
    ApiListResponse<Payout> getPayouts(int page, int limit) throws ConnectionException, HTTPException;

   
    ApiListResponse<Payout> getPayouts(Map<PayoutFilter, String> filter, int page, int limit) throws ConnectionException, HTTPException;

    Payout getPayout(String id) throws ConnectionException, HTTPException;

   
    PaymentRequest createPaymentRequest(PaymentRequest paymentRequest) throws ConnectionException, HTTPException;

   
    ApiListResponse<PaymentRequest> getPaymentRequests(int page, int limit) throws ConnectionException, HTTPException;

   
    ApiListResponse<PaymentRequest> getPaymentRequests(Map<PaymentRequestFilter, String> filter, int page, int limit) throws ConnectionException, HTTPException;

    
    PaymentRequest getPaymentRequest(String id) throws ConnectionException, HTTPException;

   
    Boolean enablePaymentRequest(String id) throws ConnectionException, HTTPException;

   
    Boolean disablePaymentRequest(String id) throws ConnectionException, HTTPException;
}
