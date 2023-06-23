package com.example.paymentshub;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HttpPostExampleNew {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        //Request Payload
        String jsonPayload = constructJson();

        String signature = createSignature("/subscription", jsonPayload, "8EEDC66DF02D7803E05321281FAC8C31");
        System.out.println(signature);
        // URL for the POST request
        String url = "https://billing.epxuap.com/subscription";

        // Create a URL object
        URL obj = new URL(url);

        // Open a connection
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Set the request method
        connection.setRequestMethod("POST");

        // Set the request headers
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("epi-signature", signature);
        connection.setRequestProperty("epi-Id", "9001-900300-2-6");

        // Enable input/output streams
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // Write the request body
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(jsonPayload);
        outputStream.flush();
        outputStream.close();

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Print the response
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Body: " + response.toString());

        // Close the connection
        connection.disconnect();
    }

    public static String constructJson(){

        // Create a JSON object from the string
        JSONObject customerDataJson = new JSONObject();
        customerDataJson.put("FirstName", "John");
        customerDataJson.put("LastName", "Doe");
        customerDataJson.put("Phone", "1234567890");
        customerDataJson.put("Email", "vikram@thinksolv.com");
        //System.out.println(customerDataJson.toString());

        JSONObject creditCardData = new JSONObject();
        creditCardData.put("AccountNumber", "4000000000000002");
        creditCardData.put("ExpirationDate", "2512");
        creditCardData.put("CVV", "123");

        //System.out.println(creditCardData.toString());

        JSONObject paymentMethod = new JSONObject();
        paymentMethod.put("CreditCardData", creditCardData);

        JSONObject subscriptionData = new JSONObject();
        subscriptionData.put("Amount", 1.00);
        subscriptionData.put("Frequency", "Monthly");
        subscriptionData.put("BillingDate", "2023-05-23");
        subscriptionData.put("FailureOption", "Forward");
        subscriptionData.put("Description", "Test Description");
        //System.out.println(subscriptionData.toString());

        JSONObject mainJson = new JSONObject();
        mainJson.put("CustomerData", customerDataJson);
        mainJson.put("PaymentMethod", paymentMethod);
        mainJson.put("SubscriptionData", subscriptionData);

        // Print the nested JSON object
        System.out.println(mainJson.toString());
        return mainJson.toString();
    }

    // Creating signature using the Hmac SHA 256 algorithm from endpoint + payload and epiKey
    public static String createSignature(String endpoint, String payload, String epiKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(epiKey.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        String data = endpoint + payload;
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    // Utility function for parsing signature
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
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

