package com.example.loginjavafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;      // JavaFX ActionEvent
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button buttonLogout;

    @FXML
    private Label labelWelcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Navigate back to the login screen on logout
        buttonLogout.setOnAction((ActionEvent event) ->
                DBUtils.changeScene(event, "log-in.fxml", "Log In", null)
        );
    }

    public void setUserInformation(String username) {
        labelWelcome.setText("Welcome " + username + "!");
    }
}
