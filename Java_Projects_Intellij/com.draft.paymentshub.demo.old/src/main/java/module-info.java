module com.example.paymentshub {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires java.net.http;
    requires org.json;



    opens com.example.paymentshub to javafx.fxml;
    exports com.example.paymentshub;
    exports com.draft.paymentshub.model;

}