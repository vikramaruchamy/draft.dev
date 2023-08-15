package handlers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import utils.Utils;

@WebServlet("/subscriptionhandler")
public class SubscriptionHandler extends HttpServlet {

	private static final String EPI_KEY = "8EEDC66DF02D7803E05321281FAC8C31";

	private static final String EPI_ID = "9001-900300-2-6";

	private static final String API_URL = "https://billing.epxuap.com";

	private static final long serialVersionUID = 7901708808237295496L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Retrieve form data from the HttpServletRequest
		String firstName = request.getParameter("FirstName");
		String lastName = request.getParameter("LastName");
		String phone = request.getParameter("Phone");
		String email = request.getParameter("Email");
		String accountNumber = request.getParameter("AccountNumber");
		String expirationDate = request.getParameter("ExpirationDate");
		String cvv = request.getParameter("CVV");
		String postalCode = request.getParameter("PostalCode");
		String streetAddress = request.getParameter("StreetAddress");
		// String amount = request.getParameter("Amount");
		String frequency = request.getParameter("frequency");

		String billingDate = request.getParameter("BillingDate");
		System.out.println("date:" + billingDate);

		String numberOfPayments = request.getParameter("NumberOfPayments");

		String description = request.getParameter("Description");
		String plan = request.getParameter("plan");

		String subscriptionId = request.getParameter("subId");

		String changeSubscription = request.getParameter("changeSub");

		String responseCode = "";
		String subscriptionMessage = "";
		if (subscriptionId != null && subscriptionId.equals("0")) {
			JsonObject inputJSON = generateJSONFromInput(firstName, lastName, phone, email, accountNumber,
					expirationDate, cvv, postalCode, streetAddress, frequency, billingDate, numberOfPayments,
					description, plan);
			responseCode = addSubscription(inputJSON);
			if (responseCode.equals("00")) {
				subscriptionMessage = "Subscription created succesfully. ";
				redirectToProfilePage(response, email, subscriptionMessage);
			} else {
				printErrorMessage(response, responseCode);
			}

		} else {
			JsonObject jsonObject = new JsonObject();

			BigDecimal decimalValue = new BigDecimal(subscriptionId);
			int subscirptionIdInt = decimalValue.intValue();
			jsonObject.addProperty("SubscriptionID", subscirptionIdInt);

			int httpResponseCode = 0;

			if (changeSubscription != null && changeSubscription.equals("pause")) {

				jsonObject.addProperty("Paused", true);

				httpResponseCode = pauseResumeSubscription(email, jsonObject);
				subscriptionMessage = "Successfully paused.";
			} else if (changeSubscription != null && changeSubscription.equals("resume")) {

				jsonObject.addProperty("Paused", false);

				httpResponseCode = pauseResumeSubscription(email, jsonObject);
				subscriptionMessage = "Successfully resumed.";
			} else if (changeSubscription != null && changeSubscription.equals("cancel")) {

				httpResponseCode = cancelSubscription(email, jsonObject);
				subscriptionMessage = "Successfully cancelled.";
			}

			if (httpResponseCode == 200) {

				redirectToProfilePage(response, email, subscriptionMessage);
			} else {
				printErrorMessage(response, responseCode);
			}
		}
		response.setContentType("text/html");

	}

	private void printErrorMessage(HttpServletResponse response, String responseCode) throws IOException {
		response.getWriter().println("<h1>Error while creating subscription with the following response code!</h1>");
		response.getWriter().println("<h2> " + responseCode + "!</h2>");
	}

	private void redirectToProfilePage(HttpServletResponse response, String email, String subscriptionMessage)
			throws IOException {
		response.getWriter().println("<h1>Processed successfully!</h1>");

		String redirectURL = "/profile?mailID=" + email + "&subscriptionMessage=" + subscriptionMessage;
		response.sendRedirect(redirectURL);
	}

	private JsonObject generateJSONFromInput(String firstName, String lastName, String phone, String email,
			String accountNumber, String expirationDate, String cvv, String postalCode, String streetAddress,
			String frequency, String billingDate, String numberOfPayments, String description, String plan) {
		JsonObject customerDataJson = new JsonObject();
		customerDataJson.addProperty("FirstName", firstName);
		customerDataJson.addProperty("LastName", lastName);
		customerDataJson.addProperty("Phone", phone);
		customerDataJson.addProperty("Email", email);

		JsonObject creditCardData = new JsonObject();
		creditCardData.addProperty("AccountNumber", accountNumber);
		creditCardData.addProperty("ExpirationDate", expirationDate);
		creditCardData.addProperty("CVV", cvv);

		creditCardData.addProperty("FirstName", firstName);
		creditCardData.addProperty("LastName", lastName);
		creditCardData.addProperty("PostalCode", postalCode);
		creditCardData.addProperty("StreetAddress", streetAddress);
		// System.out.println(creditCardData.toString());

		JsonObject paymentMethod = new JsonObject();

		paymentMethod.add("CreditCardData", creditCardData);

		double amount = 0;

		if (plan.equals("basic")) {
			amount = 2121.21;
		} else if (plan.equals("premium")) {
			amount = 5000.00;
		}

		JsonObject subscriptionData = new JsonObject();

		subscriptionData.addProperty("Amount", amount);

		subscriptionData.addProperty("Frequency", frequency);

		subscriptionData.addProperty("BillingDate", billingDate);

		subscriptionData.addProperty("FailureOption", "Forward");

		subscriptionData.addProperty("NumberOfPayments", Integer.parseInt(numberOfPayments));
		subscriptionData.addProperty("Retries", 5);
		subscriptionData.addProperty("Description", description);

		JsonObject mainJson = new JsonObject();
		mainJson.add("CustomerData", customerDataJson);
		mainJson.add("PaymentMethod", paymentMethod);
		mainJson.add("SubscriptionData", subscriptionData);

		addJSONTOFireStore(email, "CustomerData", customerDataJson);
		addJSONTOFireStore(email, "CreditCardData", paymentMethod.getAsJsonObject("CreditCardData"));
		addJSONTOFireStore(email, "SubscriptionData", subscriptionData);

		return mainJson;
	}

	private void addJSONTOFireStore(String collectionName, String documentID, JsonObject jsonString) {
		TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
		};
		Map<String, Object> data = new Gson().fromJson(jsonString, typeToken.getType());
		try {
			FireStoreHandler.createDocument(collectionName, documentID, data);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private String addSubscription(JsonObject jsonPayload) {

		String endPoint = "/subscription";

		String requestURI = API_URL + endPoint;

		HttpResponse<String> response = executeHttpRequest(jsonPayload, endPoint, requestURI);

		String emailID = jsonPayload.getAsJsonObject("CustomerData").get("Email").getAsString();

		Gson gson = new Gson();

		JsonObject responseString = gson.fromJson(response.body(), JsonObject.class);

		JsonElement subscriptionResult = responseString.get("VerifyResult");
		String responseCode = subscriptionResult.getAsJsonObject().get("Code").getAsString();

		System.out.println(responseCode);

		if (responseCode.equals("00")) {

			addJSONTOFireStore(emailID, "SubscriptionData", responseString);

		}
		return responseCode;
	}

	private int pauseResumeSubscription(String emailId, JsonObject jsonPayload) {

		String endPoint = "/subscription/pause";

		String requestURI = API_URL + endPoint;

		System.out.println(jsonPayload);

		HttpResponse<String> response = executeHttpRequest(jsonPayload, endPoint, requestURI);

		Gson gson = new Gson();

		JsonObject responseString = gson.fromJson(response.body(), JsonObject.class);
		System.out.println(responseString);
		System.out.println(emailId);
		addJSONTOFireStore(emailId, "SubscriptionResponse", responseString);

		return response.statusCode();

	}

	private int cancelSubscription(String emailId, JsonObject jsonPayload) {

		String endPoint = "/subscription/cancel";

		String requestURI = API_URL + endPoint;

		HttpResponse<String> response = executeHttpRequest(jsonPayload, endPoint, requestURI);

		Gson gson = new Gson();

		JsonObject responseString = gson.fromJson(response.body(), JsonObject.class);

		addJSONTOFireStore(emailId, "SubscriptionResponse", responseString);

		return response.statusCode();
	}

	private HttpResponse<String> executeHttpRequest(JsonObject jsonPayload, String endPoint, String requesURI) {

		Gson gson = new Gson();

		String jsonString = gson.toJson(jsonPayload);

		String signature = null;

		HttpClient httpClient = HttpClient.newHttpClient();

		URI uri = URI.create(requesURI);

		HttpResponse<String> response;

		try {

			signature = Utils.createSignature(endPoint, jsonString, EPI_KEY);

			HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "application/json")
					.header("EPI-Signature", signature).header("EPI-Id", EPI_ID)
					.POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();

			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			int statusCode = response.statusCode();

			HttpHeaders headers = response.headers();

			String responseBody = response.body();

			System.out.println("Response Code: " + statusCode);

			System.out.println("Response Headers: " + headers);

			System.out.println("Response Body: " + responseBody);

			return response;

		} catch (NoSuchAlgorithmException | InvalidKeyException | IOException | InterruptedException e) {

			e.printStackTrace();
		}
		return null;

	}
}
