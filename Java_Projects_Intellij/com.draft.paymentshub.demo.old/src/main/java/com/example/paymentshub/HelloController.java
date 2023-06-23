package com.example.paymentshub;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void subscribe(ActionEvent actionEvent) {
    }

    public void pause(ActionEvent actionEvent) {
    }

    public void resume(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
    }
}