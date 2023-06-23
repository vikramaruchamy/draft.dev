package com.example.paymentshub;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class HttpUtils {
    public static void sendAuthenticatedPostRequest(String url, String username, String password, String jsonBody) {
        try {

            StringBuilder queryString = new StringBuilder();
            Map<String, String> parameters = Map.of("EPI-Id", "9001-900300-2-6", "EPI-Signature", "8EEDC66DF02D7803E05321281FAC8C31");
            // Build the query string
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                queryString.append(key).append("=").append(value).append("&");
            }

            // Create the full URL with parameters
            String fullUrl = url + "?" + "EPI-Id=9001-900300-2-6&EPI-Signature=8EEDC66DF02D7803E05321281FAC8C31";
            System.out.println(fullUrl);
            // Create URL object with the endpoint
            URL apiUrl = new URL(fullUrl);

            // Create connection object
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");

            // Enable output and disable caching
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            // Set the content type for the request as application/json
            conn.setRequestProperty("Content-Type", "application/json");

            // Set the basic authentication header
            /*String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);*/

            // Convert the JSON body to bytes
            System.out.println(jsonBody);
            byte[] postData = jsonBody.getBytes(StandardCharsets.UTF_8);

            // Get the output stream of the connection
            try (OutputStream os = conn.getOutputStream()) {
                // Write the JSON body to the output stream
                os.write(postData);
                os.flush();
            }

            // Check the response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Close the connection
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "https://billing.epxuap.com/subscription";
        String username = "9001-900300-2-6";
        String password = "8EEDC66DF02D7803E05321281FAC8C31";
        String jsonBody = "{'CustomerData':{'FirstName':'John','LastName':'Doe','Phone':'1234567890','Email':'vikram@thinksolv.com'},'PaymentMethod':{'CreditCardData':{'AccountNumber':'4000000000000002','ExpirationDate':'2512','CVV':'123','FirstName':'John','LastName':'Doe','PostalCode':'12345','StreetAddress':'123 N CENTRAL'}},'SubscriptionData':{'Amount':1.00,'Frequency':'Monthly','BillingDate':'2023-05-23','FailureOption':'Pause','NumberOfPayments':12,'Retries':3,'Description':'Subscription Description'}}";
        sendAuthenticatedPostRequest(url, username, password, jsonBody);
    }
}

