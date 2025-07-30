package com.example.loginjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class DBUtils {

    /**
     * Changes the current scene to the given FXML.
     *
     * @param event    the JavaFX ActionEvent from the UI control
     * @param fxmlFile the FXML file name (e.g., "sign-up.fxml")
     * @param title    the window title to set
     * @param username optional; if not null and the controller supports it, passes username to the controller
     */
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        try {
            // Resolve as absolute path so it works regardless of caller's package
            String path = fxmlFile.startsWith("/")
                    ? fxmlFile
                    : "/com/example/loginjavafx/" + fxmlFile;

            URL resource = DBUtils.class.getResource(path);
            if (resource == null) {
                throw new IllegalArgumentException("FXML not found on classpath: " + path);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            if (username != null) {
                Object controller = loader.getController();
                if (controller instanceof DashboardController) {
                    ((DashboardController) controller).setUserInformation(username);
                }
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlFile, e);
        }
    }

    public static void signUpUser(ActionEvent event, String username, String email, String password) {
        String url = "jdbc:mysql://localhost:3306/loginsystems";
        String dbUser = "root";
        String dbPass = "";

        String checkSql = "SELECT 1 FROM users WHERE email = ?";
        String insertSql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)"; // <-- fixed

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement psCheck = connection.prepareStatement(checkSql)) {

            psCheck.setString(1, email);

            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.isBeforeFirst()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("This email is already taken.");
                    alert.show();
                    return;
                }
            }

            try (PreparedStatement psInsert = connection.prepareStatement(insertSql)) {
                psInsert.setString(1, email);
                psInsert.setString(2, username);
                psInsert.setString(3, password); // consider hashing in production
                psInsert.executeUpdate();
            }

            changeScene(event, "dashboard.fxml", "Welcome", username);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("A database error occurred. Please try again.");
            alert.show();
        }
    }

    public static void loginUser(ActionEvent event, String email, String password) {
        String url = "jdbc:mysql://localhost:3306/loginsystems";
        String dbUser = "root";
        String dbPass = "";

        String sql = "SELECT username, password FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    showError("Provided credentials are incorrect.");
                    return;
                }

                if (rs.next()) {
                    String stored = rs.getString("password");
                    String username = rs.getString("username");

                    if (stored != null && stored.equals(password)) {
                        changeScene(event, "dashboard.fxml", "Welcome", username);
                    } else {
                        showError("Provided credentials are incorrect.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("A database error occurred. Please try again.");
        }
    }

    private static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}
