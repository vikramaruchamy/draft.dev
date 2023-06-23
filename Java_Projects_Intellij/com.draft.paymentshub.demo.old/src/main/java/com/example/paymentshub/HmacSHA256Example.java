package com.example.paymentshub;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HmacSHA256Example {
    public static String generateHMAC(String endPoint, String epiKey) {

        String hexHash="";
        try {
            // Create an HMAC-SHA256 Mac instance and initialize it with the secret key
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(epiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKeySpec);

            // Calculate the HMAC-SHA256 hash
            byte[] hash = hmacSHA256.doFinal(endPoint.getBytes(StandardCharsets.UTF_8));

            // Convert the hash to a hexadecimal string
            hexHash = bytesToHex(hash);

            // Print the hexadecimal hash
            System.out.println("HMAC-SHA256 hash: " + hexHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return hexHash;
    }

    // Helper method to convert bytes to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}

