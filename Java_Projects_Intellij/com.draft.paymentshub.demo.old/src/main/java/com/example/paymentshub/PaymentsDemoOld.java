package com.example.paymentshub;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


public class PaymentsDemoOld {

    public String getHmacHashValue(String msg, String secretKey, String algorithm) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((secretKey).getBytes("ASCII"), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
            System.out.println("hmacDigest UnsupportedEncodingException");
        } catch (InvalidKeyException e) {
            System.out.println("hmacDigest InvalidKeyException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("hmacDigest NoSuchAlgorithmException");
        }
        return digest;
    }

    public String generateSalt() {
        String generatedString = null;
        int leftLimit = 97;   // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        generatedString = buffer.toString();

        return (generatedString);
    }



    public String createSignature(String salt, long timestamp, String httpMethod, String servicePath, String accessKey, String secretKey) {
        String signature = null;

        String strToCreateSignature = httpMethod + servicePath + salt + Long.toString(timestamp) + accessKey + secretKey;

        String strHashCode = getHmacHashValue(strToCreateSignature, secretKey, "HmacSHA256");

        signature = Base64.getEncoder().encodeToString(strHashCode.getBytes());

        return signature;
    }

    public HttpResponse getHttpResponseFromHub(String apiServiceHome, String servicePath, String accessKey, String secretKey, String salt, long timestamp, String signature) throws IOException {


        HttpClient httpClient = HttpClients.createDefault();

        HttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(apiServiceHome + servicePath);
            URI uri = new URIBuilder(httpPost.getURI())
                    .addParameter("EPI-Id", "9001-900300-2-6")
                    .addParameter("EPI-Signature", signature)
                    .build();
            httpPost.setURI(uri);
            httpPost.setHeader("Content-type", "application/json");
            try {
                StringEntity stringEntity = new StringEntity(RequestSchemaGenerator.getSampleSchema());
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


    public static void main(String[] args) throws Exception {
        PaymentsDemoOld paymentsDemo = new PaymentsDemoOld();
        try {

            String httpMethod = "post";// get|put|post|delete - must be lowercase
            String paymentServiceHome = "https://billing.epxuap.com";
            String servicePath = "/subscription";
            String accessKey = "9001-900300-2-6";
            String secretKey = "8EEDC66DF02D7803E05321281FAC8C31";


            long timestamp = System.currentTimeMillis() / 1000L; // Unix time (seconds).

            String salt = paymentsDemo.generateSalt();

            //Generating the EPI Signature
            String signature = paymentsDemo.createSignature(salt, timestamp, httpMethod, servicePath, accessKey, secretKey);

            HttpResponse response = paymentsDemo.getHttpResponseFromHub(paymentServiceHome, servicePath, accessKey, secretKey, salt, timestamp, signature);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
