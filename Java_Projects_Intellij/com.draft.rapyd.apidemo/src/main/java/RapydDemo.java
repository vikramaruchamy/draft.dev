import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;


public class RapydDemo {

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

    public HttpResponse getHttpResponseFromRapyd(String rapydApiServiceHome, String servicePath, String accessKey, String secretKey, String salt, long timestamp, String signature) throws IOException {


        HttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(rapydApiServiceHome + servicePath);

            httpget.addHeader("Content-Type", "application/json");
            httpget.addHeader("access_key", accessKey);
            httpget.addHeader("salt", salt);
            httpget.addHeader("timestamp", Long.toString(timestamp));
            httpget.addHeader("signature", signature);
            response = httpclient.execute(httpget);

        } catch (IOException e) {
            System.err.println("Error while processing Get request : " + e.getMessage());
        }

        return response;
    }

    public void prettyPrintHttpResponse(HttpResponse response) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(EntityUtils.toString(response.getEntity()));
        JsonObject obj = je.getAsJsonObject();

        System.out.println(obj.get("status").getAsJsonObject().get("status"));

        JsonArray dataElement = obj.get("data").getAsJsonArray();

        System.out.println("Available payment methods are..");
        for (JsonElement pa : dataElement) {
            JsonObject paymentObj = pa.getAsJsonObject();
            String     paymentName     = paymentObj.get("name").getAsString();
            System.out.println(paymentName);

        }
    }
    public static void main(String[] args) throws Exception {
        RapydDemo rapydDemo = new RapydDemo();
        try {

            String httpMethod = "get";// get|put|post|delete - must be lowercase
            String rapydApiServiceHome = "https://sandboxapi.rapyd.net/";
            String servicePath = "/v1/" + "payment_methods/countries/US";
            String accessKey = "XXXXXXXX";//Generated from Rapyd dashboard
            String secretKey = "XXXXXXXXXXXXXXXXXXXXXX";//Generated from Rapyd dashboard


            long timestamp = System.currentTimeMillis() / 1000L; // Unix time (seconds).

            String salt = rapydDemo.generateSalt();

            String signature = rapydDemo.createSignature(salt, timestamp, httpMethod, servicePath, accessKey, secretKey);

            HttpResponse response = rapydDemo.getHttpResponseFromRapyd(rapydApiServiceHome, servicePath, accessKey, secretKey, salt, timestamp, signature);

            rapydDemo.prettyPrintHttpResponse(response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
