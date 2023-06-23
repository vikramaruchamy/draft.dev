import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RapydDemoTest {
    String httpMethod;
    String rapydApiServiceHome;
    String servicePath;
    String accessKey;
    String secretKey;
    long timestamp;
    RapydDemo rapydDemo = new RapydDemo();

    @BeforeEach
    void setUp() {

        httpMethod = "get";// get|put|post|delete - must be lowercase
        rapydApiServiceHome = "https://sandboxapi.rapyd.net/";
        servicePath = "/v1/" + "payment_methods/countries/US";
        accessKey = "XXXXXXXXXXXX";
        secretKey = "XXXXXXXXXXXXXXXXXXXXXX";


        timestamp = System.currentTimeMillis() / 1000L; // Unix time (seconds).

    }


    @Test
    void generateSalt() {
        assertNotNull(rapydDemo.generateSalt());
    }

    @Test
    void createSignature() {
        String salt = rapydDemo.generateSalt();
        assertNotNull(rapydDemo.createSignature(salt, timestamp, httpMethod, servicePath, accessKey, secretKey));
    }

    @Test
    void getHttpResponseFromRapyd() throws IOException {
        String salt = rapydDemo.generateSalt();
        String signature = rapydDemo.createSignature(salt, timestamp, httpMethod, servicePath, accessKey, secretKey);
        HttpResponse response = null;
        try {
            response = rapydDemo.getHttpResponseFromRapyd(rapydApiServiceHome, servicePath, accessKey, secretKey, salt, timestamp, signature);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(EntityUtils.toString(response.getEntity()));
            JsonObject obj = je.getAsJsonObject();
            System.out.println(obj.get("status").getAsJsonObject().get("status"));

            //Success denotes that the API has returned the result successfully.
            assertEquals(obj.get("status").getAsJsonObject().get("status").getAsString(), "SUCCESS");

            JsonArray dataElement = obj.get("data").getAsJsonArray();

            System.out.println("Available payment methods are..");
            for (JsonElement pa : dataElement) {
                JsonObject paymentObj = pa.getAsJsonObject();
                String     paymentName     = paymentObj.get("name").getAsString();
                System.out.println(paymentName);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
