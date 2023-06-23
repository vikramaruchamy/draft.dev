package demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handlers.FireStoreHandler;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>Payments Hub Profile Page</h1>");

		String emailId = request.getParameter("mailID");
		
		Map<String, Map<String, Object>> documents = getUserDocumentsFromFireStore(emailId);
		
		
		
		String firstName = documents.get("CustomerData").get("FirstName").toString();
		
		out.println("<h2>Welcome " + firstName + "! </h2><br><br>");
		out.println("<form method=\"POST\" action=\"/your-action-url\">");

		out.println("First Name: <input type=\"text\" name=\"FirstName\" value = "+ firstName + "><br><br>");
		out.println("Last Name: <input type=\"text\" name=\"LastName\" value = " + documents.get("CustomerData").get("LastName") + "><br><br>");
		out.println("Phone: <input type=\"text\" name=\"Phone\" value = " + documents.get("CustomerData").get("Phone") + "><br><br>");
		out.println("Email: <input type=\"text\" name=\"Email\" value = " + documents.get("CustomerData").get("Email") + "><br><br>");

		out.println("<h2>Credit Card Information</h2>");
		out.println("Account Number: <input type=\"text\" name=\"AccountNumber\"  value = " + documents.get("CreditCardData").get("AccountNumber") + "><br><br>");
		out.println("Expiration Date: <input type=\"text\" name=\"ExpirationDate\"  value = " + documents.get("CreditCardData").get("ExpirationDate") + "><br><br>");
		out.println("CVV: <input type=\"text\" name=\"CVV\" value = " + documents.get("CreditCardData").get("CVV") + "><br><br>");

		out.println("<h2>Subscription Details</h2>");
		out.println("Amount: <input type=\"text\" name=\"Amount\"  value = " + documents.get("SubscriptionData").get("Amount") + "><br><br>");
		out.println("Frequency: <input type=\"text\" name=\"Frequency\"  value = " + documents.get("SubscriptionData").get("Frequency") + "><br><br>");
		out.println("Billing Date: <input type=\"text\" name=\"BillingDate\"  value = " + documents.get("SubscriptionData").get("BillingDate") + "><br><br>");
		out.println("Failure Option: <input type=\"text\" name=\"FailureOption\"  value = " + documents.get("SubscriptionData").get("FailureOption") + "><br><br>");
		out.println("Number of Payments: <input type=\"text\" name=\"NumberOfPayments\"  value = " + documents.get("SubscriptionData").get("NumberOfPayments") + "><br><br>");
		out.println("Retries: <input type=\"text\" name=\"Retries\"  value = " + documents.get("SubscriptionData").get("Retries") + "><br><br>");
		out.println("Description: <input type=\"text\" name=\"Description\"  value = " + documents.get("SubscriptionData").get("Description").toString() + "><br><br>");

		String subscriptionId = documents.get("SubscriptionData").get("id").toString();
		
		out.println("<input type=\"hidden\" name=\"subscriptionid\" value=\"" + subscriptionId + "\">");
		
		
		out.println("<input type=\"button\" value=\"pause\" onclick=\"submitForm(this.value);\">");
		out.println("<input type=\"button\" value=\"resume\" onclick=\"submitForm(this.value);\">");
		out.println("<input type=\"button\" value=\"cancel\" onclick=\"submitForm(this.value);\">");
		out.println("</form>");
		out.println("<script>");
		out.println("function submitForm(buttonValue) {");
		out.println("    var form = document.createElement('form');");
		out.println("    form.setAttribute('method', 'post');");
		out.println("    form.setAttribute('action', '/subscriptionhandler');");

		out.println("    var hiddenField = document.createElement('input');");
		out.println("    hiddenField.setAttribute('type', 'hidden');");
		out.println("    hiddenField.setAttribute('name', 'mailID');");
		out.println("    hiddenField.setAttribute('value', '" + emailId + "');");
		out.println("    form.appendChild(hiddenField);");

		out.println("    var changeSubField = document.createElement('input');");
		out.println("    changeSubField.setAttribute('type', 'hidden');");
		out.println("    changeSubField.setAttribute('name', 'changeSub');");
		out.println("    changeSubField.setAttribute('value', buttonValue);"); 
		out.println("    form.appendChild(changeSubField);");
		
		out.println("    var subscriptionIdField = document.createElement('input');");
		out.println("    subscriptionIdField.setAttribute('type', 'hidden');");
		out.println("    subscriptionIdField.setAttribute('name', 'subId');");
		out.println("    subscriptionIdField.setAttribute('value', '" + subscriptionId + "');"); 
		out.println("    form.appendChild(subscriptionIdField);");

		out.println("    document.body.appendChild(form);");
		out.println("    form.submit();");
		out.println("}");
		out.println("</script>");

		
		out.println("</body></html>");

	}

	private Map<String, Map<String, Object>> getUserDocumentsFromFireStore(String emailId) {
		Map<String, Map<String, Object>> documents = null;
		try {
			documents = FireStoreHandler.getAllDocuments(emailId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return documents;
	}
}
