import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
	private TextField usernameField;
	private PasswordField passwordField;
	private Label statusLabel;
	private Connection databaseConnection;
	private String username;

	public LoginController(Connection connection) {
		this.databaseConnection = connection;
	}
	public void show(Stage stage) {
		// Create UI components
		usernameField = new TextField();
		passwordField = new PasswordField();
		statusLabel = new Label();
		
		// Create login form
		GridPane grid = new GridPane();
		grid.getStyleClass().add("login-root");
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);
		grid.add(new Label("Username"), 0, 0);
		grid.add(usernameField, 1, 0);
		grid.add(new Label("Password"), 0, 1);
		grid.add(passwordField, 1, 1);
		Button loginButton = new Button("Login");
		loginButton.getStyleClass().add("login-button");
		loginButton.setOnAction(e -> handleLogin(e));
		grid.add(loginButton, 1, 2);
		grid.add(statusLabel, 0, 3, 2, 1);
		
		// Create main layout
		VBox root = new VBox();
		root.getStyleClass().add("login-root");
		root.getChildren().addAll(grid);
		
		// Ser up the scene
		Scene scene = new Scene(root, 400, 250);
		Stage stage1 = new Stage();
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); 
		stage1.setTitle("Login");
		stage1.setScene(scene);
		stage1.show();
	}
	
	private void handleLogin(ActionEvent event) {
		String username = usernameField.getText();
		String password = passwordField.getText();
		this.username = username;
		
		User user = new User(databaseConnection);
		String role = user.login(username, password);
		if (role != null) {
			showAlert(Alert.AlertType.INFORMATION,"Login successful", "Welcome " + role + " " + username);
			statusLabel.setText("Login successful as " + role);
			openMainWindow(role);
			System.out.println("Opening window for " + role);
		} else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
		}
	}
	

	private void openMainWindow(String role) {
		switch (role) {
			case "Teacher":
				openTeacherWindow();
				break;
			case "Tech":
				openTechWindow();
				break;
			case "Admin":
				openAdminWindow();
                break;
            default:
                // Handle unknown roles
                break;
		}
				
	}

	void openTeacherWindow() {
	    DbConnect dbConnect = new DbConnect(); // Initialize DbConnect instance
	    dbConnect.connect(); // Connect to the database
	    TeacherWindow teacherWindow = new TeacherWindow(dbConnect, username); // Pass DbConnect instance to TeacherWindow
	    Stage stage = new Stage();
	    try {
	        teacherWindow.start(stage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

	private void openTechWindow() {
		TechWindow techWindow = new TechWindow(); 
	    techWindow.start(new Stage());
	}	
	
	private void openAdminWindow() {
		DbConnect dbConnect = new DbConnect(); // Initialize DbConnect instance
		dbConnect.connect(); 
	    AdminWindow adminWindow = new AdminWindow(dbConnect); 
	    adminWindow.start(new Stage());
	}
	
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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
