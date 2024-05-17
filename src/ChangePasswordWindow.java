import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordWindow extends Application {
 
    private String role;
    private DbConnect dbConnect;

    public ChangePasswordWindow(String role, DbConnect dbConnect) {
        this.role = role;
        this.dbConnect = dbConnect;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        Label titleLabel = new Label("Change Password");
        Label usernameLabel = new Label("Username: ");
        Label oldPasswordLabel = new Label("Current Password:");
        Label newPasswordLabel = new Label("New Password:");
        Label confirmPasswordLabel = new Label("Confirm New Password:");
        TextField usernameField = new TextField();
        PasswordField oldPasswordField = new PasswordField();
        PasswordField newPasswordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Label statusLabel = new Label();
        Button changePasswordButton = new Button("Change Password");

        // Set action for change password button
        changePasswordButton.setOnAction(e -> {
            String username = usernameField.getText();
        	String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            changePassword(primaryStage, username, oldPassword, newPassword, confirmPassword);
        });
        
        // Apply CSS styles to UI components
        titleLabel.getStyleClass().add("login-title");
        usernameLabel.getStyleClass().add("login-label");
        oldPasswordLabel.getStyleClass().add("login-label");
        newPasswordLabel.getStyleClass().add("login-label");
        confirmPasswordLabel.getStyleClass().add("login-label");
        usernameField.getStyleClass().add("login-text-field");
        oldPasswordField.getStyleClass().add("login-text-field");
        newPasswordField.getStyleClass().add("login-text-field");
        confirmPasswordField.getStyleClass().add("login-text-field");
        changePasswordButton.getStyleClass().add("login-button");
        statusLabel.getStyleClass().add("login-error-label");

        // Create layout
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("login-root");
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(titleLabel, 0, 0, 2, 1);
        gridPane.add(usernameLabel, 0, 1); 
        gridPane.add(usernameField, 1, 1); 
        gridPane.add(oldPasswordLabel, 0, 2);
        gridPane.add(oldPasswordField, 1, 2);
        gridPane.add(newPasswordLabel, 0, 3);
        gridPane.add(newPasswordField, 1, 3);
        gridPane.add(confirmPasswordLabel, 0, 4);
        gridPane.add(confirmPasswordField, 1, 4);
        gridPane.add(changePasswordButton, 1, 5);
        gridPane.add(statusLabel, 0, 6, 2, 1);

        // Set scene
        Scene scene = new Scene(gridPane, 500, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }

    private void changePassword(Stage stage, String username, String oldPassword, String newPassword, String confirmPassword) {
        if (username.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
        	showAlert(Alert.AlertType.ERROR, "Error", "New passwords do not match.");
            return;
        }

        try {
            // Check if old password matches the one stored in the database
            String query = "SELECT * FROM DBLogUsers WHERE role = ? AND username = ? AND password = ?";
            Connection connection = dbConnect.getDatabaseConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, oldPassword);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or old password.");
                return;
            }

            // Change password in the database
            String updateQuery = "UPDATE DBLogUsers SET password = ? WHERE role = ? AND username = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, newPassword);
            updateStatement.setString(2, role);
            updateStatement.setString(3, username);
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while changing password.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Set styles for the dialog pane
        alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background

        // Set styles for the content area
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");

        // Set styles for the buttons
        alert.getDialogPane().lookupAll(".button").forEach(node ->
            node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
        );
        alert.showAndWait();
    }

}
