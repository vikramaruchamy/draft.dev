package com.example.paymentshub;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HmacUtils {
    public static String generateHmac(String endpoint, String payload, String secretKey) {
        try {
            // Create a SecretKeySpec with the secret key and HMAC algorithm
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

            // Create a Mac instance with the HMAC algorithm
            Mac mac = Mac.getInstance("HmacSHA256");

            // Initialize the Mac instance with the secret key
            mac.init(secretKeySpec);

            // Concatenate the endpoint and payload
            String data = endpoint + payload;

            // Compute the HMAC of the data
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Encode the HMAC bytes as a Base64 string
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String endpoint = "/subscription";
        //String payload = "{\"CustomerData\":{\"FirstName\":\"John\",\"LastName\":\"Doe\",\"Phone\":\"1234567890\",\"Email\":\"vikram@thinksolv.com\"},\"PaymentMethod\":{\"CreditCardData\":{\"AccountNumber\":\"4000000000000002\",\"ExpirationDate\":\"2512\",\"CVV\":\"123\",\"FirstName\":\"John\",\"LastName\":\"Doe\",\"PostalCode\":\"12345\",\"StreetAddress\":\"123 N CENTRAL\"}},\"SubscriptionData\":{\"Amount\":1.00,\"Frequency\":\"Monthly\",\"BillingDate\":\"2023-05-23\",\"FailureOption\":\"Pause\",\"NumberOfPayments\":12,\"Retries\":3,\"Description\":\"Subscription Description\"}}";
        //String payload = "{\"CustomerData\":{\"John\",\"Doe\",\"1234567890\",\"vikram@thinksolv.com\"},\"PaymentMethod\":{\"CreditCardData\":{\"4000000000000002\",\"2512\",\"123\",\"John\",\"Doe\",\"12345\",\"123 N CENTRAL\"}},\"SubscriptionData\":{1.00,\"Monthly\",\"2023-05-23\",\"Pause\",12,3,\"Subscription Description\"}}";
        String jsonPayload = "{\"CustomerData\":{\"John\",\"Doe\",\"1234567890\",\"vikram@thinksolv.com\"},\"PaymentMethod\":{\"CreditCardData\":{\"4000000000000002\",\"2512\",\"123\",\"John\",\"Doe\",\"12345\",\"123 N CENTRAL\"}},\"SubscriptionData\":{\"1.00\",\"Frequency\":\"Monthly\",\"BillingDate\":\"2023-05-23\",\"FailureOption\":\"Pause\",\"NumberOfPayments\":12,\"Retries\":3,\"Description\":\"Subscription Description\"}}";

        String secretKey = "8EEDC66DF02D7803E05321281FAC8C31";

        String hmac = generateHmac(endpoint, jsonPayload, secretKey);
        System.out.println("HMAC: " + hmac);
    }
}
