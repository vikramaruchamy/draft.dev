package com.example.paymentshub;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HttpPostExample {
    public static void main(String[] args) {
        try {
            String jsonPayload = jsonConstruction();

            String signature = createSignature("/subscription", jsonPayload, "8EEDC66DF02D7803E05321281FAC8C31");

            System.out.println(signature);
            // Create an instance of HttpClient
            HttpClient httpClient = HttpClient.newHttpClient();
            // Create the request URI
            URI uri = URI.create("https://billing.epxuap.com/subscription");

            // Create the POST request with the request body
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Content-Type", "application/json")
                    .header("EPI-Signature", signature)
                    .header("EPI-Id", "9001-900300-2-6")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Get the response status code
            int statusCode = response.statusCode();

            // Get the response headers
            HttpHeaders headers = response.headers();

            // Get the response body
            String responseBody = response.body();

            // Print the response
            System.out.println("Response Code: " + statusCode);
            System.out.println("Response Headers: " + headers);
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String jsonConstruction(){
        // Create a JSON object from the string
        JSONObject customerDataJson = new JSONObject();
        customerDataJson.put("FirstName", "Joe");
        customerDataJson.put("LastName", "Doe");
        customerDataJson.put("Phone", "1234567890");
        customerDataJson.put("Email", "jdoe@domanin.com");
        System.out.println(customerDataJson.toString());

        JSONObject creditCardData = new JSONObject();
        creditCardData.put("AccountNumber", "400000000000000002");
        creditCardData.put("ExpirationDate", "2211");
        creditCardData.put("CVV", "123");

        creditCardData.put("FirstName", "Joe1");
        creditCardData.put("LastName", "Doe1");
        creditCardData.put("PostalCode", "12346");
        creditCardData.put("StreetAddress", "1234 Ruby Road");
        System.out.println(creditCardData.toString());

        JSONObject paymentMethod = new JSONObject();
        paymentMethod.put("CreditCardData", creditCardData);

        JSONObject subscriptionData = new JSONObject();
        subscriptionData.put("Amount", 2121.21);
        subscriptionData.put("Frequency", "Weekly");
        subscriptionData.put("BillingDate", "2023-06-25");
        subscriptionData.put("FailureOption", "Forward");
        subscriptionData.put("NumberOfPayments", 3);
        subscriptionData.put("Retries", 5);
        subscriptionData.put("Description", "String");
        System.out.println(subscriptionData.toString());

        JSONObject mainJson = new JSONObject();
        mainJson.put("CustomerData", customerDataJson);
        mainJson.put("PaymentMethod", paymentMethod);
        mainJson.put("SubscriptionData", subscriptionData);

        // Print the nested JSON object
        System.out.println(mainJson);

        return mainJson.toString();
    }

    public static String createSignature(String endpoint, String payload, String epiKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(epiKey.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        String data = endpoint + payload;
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    // Utility function for parsing signature
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
