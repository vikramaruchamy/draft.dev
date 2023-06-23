package com.example.paymentshub;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;

public class PaymentsDemo {

    public HttpResponse getHttpResponseFromHub(String endpoint, String signature, String requestSchema) throws IOException {


        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(endpoint);
            URI uri = new URIBuilder(httpPost.getURI())
                    .addParameter("EPI-Id", "9001-900300-2-6")
                    .addParameter("EPI-Signature", signature)
                    .build();
            httpPost.setURI(uri);
            httpPost.setHeader("Content-type", "application/json");
            try {
                StringEntity stringEntity = new StringEntity(requestSchema);
                httpPost.setEntity(stringEntity);

                response= httpClient.execute(httpPost);
                System.out.println("Response: " + response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public static void main(String[] args) {
        String apiServiceHome = "https://billing.epxuap.com/";
        String servicePath = "subscription";
        String endpoint = apiServiceHome + servicePath;
        System.out.println(endpoint);
        String jsonPayload = jsonConstruction();
        String secretKey = "8EEDC66DF02D7803E05321281FAC8C31";

        String signature = HmacUtils.generateHmac("/subscription", jsonPayload, secretKey);
        System.out.println("HMAC: " + signature);
        PaymentsDemo demo = new PaymentsDemo();
        try {
            demo.getHttpResponseFromHub(endpoint, signature, jsonPayload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String jsonConstruction(){
        //String jsonString = "{\"CustomerData\":{\"John\",\"Doe\",\"1234567890\",\"vikram@thinksolv.com\"},\"PaymentMethod\":{\"CreditCardData\":{\"4000000000000002\",\"2512\",\"123\",\"John\",\"Doe\",\"12345\",\"123 N CENTRAL\"}},\"SubscriptionData\":{\"1.00\",\"Frequency\":\"Monthly\",\"BillingDate\":\"2023-05-23\",\"FailureOption\":\"Pause\",\"NumberOfPayments\":12,\"Retries\":3,\"Description\":\"Subscription Description\"}}";
        //String jsonPayload = "{\"CustomerData\":{\"FirstName\":\"John\",\"LastName\":\"Doe\",\"Phone\":\"1234567890\",\"Email\":\"vikram@thinksolv.com\"},\"PaymentMethod\":{
        // \"CreditCardData\":{\"AccountNumber\":\"4000000000000002\",\"ExpirationDate\":\"2512\",\"CVV\":\"123\",\"FirstName\":\"John\",\"LastName\":\"Doe\",\"PostalCode\":\"12345\",\"StreetAddress\":\"123 N CENTRAL\"}},\"SubscriptionData\":{\"Amount\":1.00,\"Frequency\":\"Monthly\",\"BillingDate\":\"2023-05-23\",\"FailureOption\":\"Pause\",\"NumberOfPayments\":12,\"Retries\":3,\"Description\":\"Subscription Description\"}}";

        // Create a JSON object from the string
        JSONObject customerDataJson = new JSONObject();
        customerDataJson.put("FirstName", "John");
        customerDataJson.put("LastName", "Doe");
        customerDataJson.put("Phone", "1234567890");
        customerDataJson.put("Email", "vikram@thinksolv.com");
        System.out.println(customerDataJson.toString());

        JSONObject creditCardData = new JSONObject();
        creditCardData.put("AccountNumber", "4000000000000002");
        creditCardData.put("ExpirationDate", "2512");
        creditCardData.put("CVV", "123");

        System.out.println(creditCardData.toString());
        JSONObject paymentMethod = new JSONObject();
        paymentMethod.put("CreditCardData", creditCardData);

        JSONObject subscriptionData = new JSONObject();
        subscriptionData.put("Amount", 1.00);
        subscriptionData.put("Frequency", "Monthly");
        subscriptionData.put("BillingDate", "2023-05-23");
        System.out.println(subscriptionData.toString());

        JSONObject mainJson = new JSONObject();
        mainJson.put("CustomerData", customerDataJson);
        mainJson.put("PaymentMethod", paymentMethod);
        mainJson.put("SubscriptionData", subscriptionData);

        // Print the nested JSON object
        System.out.println(mainJson.toString());
        return mainJson.toString();
    }
}
