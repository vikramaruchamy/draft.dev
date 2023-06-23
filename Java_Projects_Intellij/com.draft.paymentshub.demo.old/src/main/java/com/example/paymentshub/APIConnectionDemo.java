package com.example.paymentshub;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class APIConnectionDemo {
    public static String sendApiRequest(String url, Map<String, String> parameters, String requestSchema) throws IOException {
        StringBuilder queryString = new StringBuilder();

        // Build the query string
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            queryString.append(key).append("=").append(value).append("&");
        }

        // Create the full URL with parameters
        String fullUrl = url + "?" + queryString.toString();

        // Create a HttpURLConnection object
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            // Create a URL object from the full URL
            URL requestUrl = new URL(fullUrl);

            // Open a connection to the URL
            connection = (HttpURLConnection) requestUrl.openConnection();

            // Set the request method and headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Enable output and send the request body
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(requestSchema);
            outputStream.flush();
            outputStream.close();

            // Send the request and receive the response
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } else {
                // Handle the error response
                response.append("Request failed with HTTP error code: ").append(responseCode);
            }
        } finally {
            // Clean up resources
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response.toString();
    }

    public static void main(String[] args) {
        //epii id - 9001-900300-2-6
        //epi key -8EEDC66DF02D7803E05321281FAC8C31
        String url = "https://billing.epxuap.com/subscription/";
        Map<String, String> parameters = Map.of("EPI-Id", "9001-900300-2-6", "EPI-Signature", "8EEDC66DF02D7803E05321281FAC8C31");

        HttpResponse response = null;
        String requestSchema =  "{'CustomerData':{'FirstName':'John','LastName':'Doe','Phone':'1234567890','Email':'vikram@thinksolv.com'},'PaymentMethod':{'CreditCardData':{'AccountNumber':'4000000000000002','ExpirationDate':'2512','CVV':'123','FirstName':'John','LastName':'Doe','PostalCode':'12345','StreetAddress':'123 N CENTRAL'}},'SubscriptionData':{'Amount':1.00,'Frequency':'Monthly','BillingDate':'2023-05-25','FailureOption':'Forward','NumberOfPayments':12,'Retries':3,'Description':'Subscription Description'}}";

        try {
           String respons = sendApiRequest(url, parameters, requestSchema);
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            URI uri = new URIBuilder(httpPost.getURI())
                    .addParameter("EPI-Id", "9001-900300-2-6")
                    .addParameter("EPI-Signature", "8EEDC66DF02D7803E05321281FAC8C31")
                    .build();
            httpPost.setURI(uri);
            httpPost.setHeader("Content-type", "application/json");
            try {
                StringEntity stringEntity = new StringEntity(RequestSchemaGenerator.getSampleSchema());
                httpPost.setEntity(stringEntity);

                response= httpClient.execute(httpPost);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }*/
            System.out.println("Response: " + respons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
