package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private Button signupButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sign up
        signupButton.setOnAction((ActionEvent event) ->
                DBUtils.signUpUser(event,
                        username.getText(),
                        email.getText(),
                        password.getText())
        );

        // Login
        loginButton.setOnAction((ActionEvent event) ->
                DBUtils.changeScene(event, "log-in.fxml", "Log In", null)
        );
    }
}
