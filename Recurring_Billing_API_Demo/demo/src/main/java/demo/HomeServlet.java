package demo;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = -1541805457506999385L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        
        out.println("<h1>Payments Hub Subscription Demo</h1>");

        out.println("<form method=\"POST\" action=\"/subscriptionhandler?subId=0\">");
        out.println("First Name: <input type=\"text\" name=\"FirstName\" value=\"Joe\"><br><br>");
        out.println("Last Name: <input type=\"text\" name=\"LastName\" value=\"Doe\"><br><br>");
        out.println("Phone: <input type=\"text\" name=\"Phone\" value=\"1234567890\"><br><br>");
        out.println("Email: <input type=\"text\" name=\"Email\" value=\"jdoe@domanin.com\"><br><br>");
        out.println("Address: <input type=\"text\" name=\"Address\" value=\"1234 Ruby Road\"><br><br>");
        out.println("Postal Code: <input type=\"text\" name=\"PostalCode\" value=\"12346\"><br><br>");
        
        out.println("<h2>Credit Card Information</h2>");
        
        out.println("Account Number: <input type=\"text\" name=\"AccountNumber\" value =\"400000000000000002\"><br><br>");
        out.println("Expiration Date: <input type=\"text\" name=\"ExpirationDate\"  placeholder=\"YYMM\" value=\"2211\" required><br><br>");
        out.println("CVV: <input type=\"text\" name=\"CVV\" value=\"123\"><br><br>");

        
        out.println("<h2>Subscription Details</h2>");
        
        out.println("Select Plan:");
        out.println("<label><input type=\"radio\" name=\"plan\" value=\"basic\" checked> Basic ($2121.21) </label>");
        out.println("<label><input type=\"radio\" name=\"plan\" value=\"premium\"> Premium ($5000.00)</label><br><br>");

        out.println("Frequency:");
        out.println("<label><input type=\"radio\" name=\"frequency\" value=\"Weekly\"> Weekly</label>");
        out.println("<label><input type=\"radio\" name=\"frequency\" value=\"Biweekly\"> Biweekly</label>");
        out.println("<label><input type=\"radio\" name=\"frequency\" value=\"Monthly\" checked> Monthly</label><br><br>");

        out.println("Billing Date: <input type=\"date\" name=\"BillingDate\"><br><br>");
        out.println("Number of Payments: <input type=\"text\" name=\"NumberOfPayments\" value =\"5\"><br><br>");
        out.println("Description: <input type=\"text\" name=\"Description\" value=\"Subscription for Streaming service\"><br><br>");

        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");

        out.println("</body></html>");

    }
}
