package com.example.paymentshub;

import com.draft.paymentshub.model.CustomerData;
import com.draft.paymentshub.model.PaymentData;
import com.draft.paymentshub.model.RequestSchema;
import com.draft.paymentshub.model.SubscriptionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestSchemaGenerator {
    public static String generateRequestSchema(CustomerData customerData, PaymentData paymentData, SubscriptionData subscriptionData) throws JsonProcessingException {
        RequestSchema requestSchema = new RequestSchema();
        requestSchema.setCustomerData(customerData);
        requestSchema.setPaymentData(paymentData);
        requestSchema.setSubscriptionData(subscriptionData);


        // Convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestSchema);
    }

    public static void main(String[] args) {
        getSampleSchema();
    }

    public static String getSampleSchema() {
        CustomerData CustomerData = new CustomerData("John", "Doe", "1234567890", "vikram@thinksolv.com");

        PaymentData.CreditCardData CreditCardData = new PaymentData.CreditCardData("4000000000000002", "2512", "123", "John", "Doe", "12345", "123 N CENTRAL");

        PaymentData paymentData = new PaymentData();
        paymentData.setCreditCardData(CreditCardData);

        SubscriptionData subscriptionData = new SubscriptionData(1.00, "Monthly", "15th", "cancel", 12, 3, "Subscription Description");
        String requestSchema = null;
        try {
            requestSchema = generateRequestSchema(CustomerData, paymentData, subscriptionData);
            System.out.println("Request Schema:\n" + requestSchema);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestSchema;
    }
}

