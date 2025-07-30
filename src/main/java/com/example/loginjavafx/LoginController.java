package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
// import javafx.scene.control.PasswordField; // if you switch the password field type

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    private TextField email;

    @FXML
    private TextField password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction((ActionEvent event) ->
                DBUtils.loginUser(event, email.getText(), password.getText())
        );

        signupButton.setOnAction((ActionEvent event) ->
                DBUtils.changeScene(event, "sign-up.fxml", "Sign up!", null)
        );
    }
}
