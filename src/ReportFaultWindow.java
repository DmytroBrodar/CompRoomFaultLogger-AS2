import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReportFaultWindow extends Application {
	
	private DbConnect dbConnect;
	private String username;
	
	
	public ReportFaultWindow(DbConnect dbConnect, String username, LogBookTableView logBookTableView) {
		this.dbConnect = dbConnect;
		this.username = username;
	
	} 
	
	@Override
	public void start(Stage primaryStage) {
		// Create UI components
		Label titleLabel = new Label("Report a Fault");
		Label roomNumberLabel = new Label("Room Number: ");
		Label computerNumberLabel = new Label("Computer Number: ");
		Label faultLabel =new Label("Fault: ");
		TextField roomNumberField = new TextField();
		TextField computerNumberField = new TextField();
		TextField faultField = new TextField();
		Button reportButton = new Button("Report");
		Button cancelButton = new Button("Cancel");
		
		// Set action for report button
		reportButton.setOnAction(e -> {
		    String roomNumber = roomNumberField.getText();
		    String computerNumber = computerNumberField.getText();
		    String fault = faultField.getText();

		    if (!isRoomExists(roomNumber)) {
		        showAlertAndWait(Alert.AlertType.ERROR, "Room does not exist!");
		    } else if (!isComputerExists(roomNumber, computerNumber)) {
		        showAlertAndWait(Alert.AlertType.ERROR, "Computer does not exist in the specified room!");
		    } else if (isComputerReported(roomNumber, computerNumber)) {
		        showAlertAndWait(Alert.AlertType.ERROR, "Computer has already been reported!");
		    } else {
		        reportFault(username, roomNumber, computerNumber, fault);
		        primaryStage.close();
		    }
		});
		
		
		
		// Set action for cancel button
		cancelButton.setOnAction(e -> primaryStage.close());
		
	    // Apply CSS styles to UI components
        titleLabel.getStyleClass().add("login-title");
        roomNumberLabel.getStyleClass().add("login-label");
        computerNumberLabel.getStyleClass().add("login-label");
        faultLabel.getStyleClass().add("login-label");
        roomNumberField.getStyleClass().add("login-text-field");
        computerNumberField.getStyleClass().add("login-text-field");
        faultField.getStyleClass().add("login-text-field");
        reportButton.getStyleClass().add("login-button");
        cancelButton.getStyleClass().add("login-button");
		
		// Create layout
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(titleLabel, 0, 0, 2, 1);
		gridPane.add(roomNumberLabel, 0, 1);
		gridPane.add(roomNumberField, 1, 1);
		gridPane.add(computerNumberLabel, 0, 2);
		gridPane.add(computerNumberField, 1, 2);
		gridPane.add(faultLabel, 0, 3);
		gridPane.add(faultField, 1, 3);
		
		HBox buttonBox = new HBox(10, reportButton, cancelButton);
		VBox root = new VBox(10, gridPane, buttonBox);
        root.getStyleClass().add("login-root");

		
		// Set up the scene
		Scene scene = new Scene(root, 420, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Report Fault");
		primaryStage.show();
	}
	


	private boolean isRoomExists(String roomNumber) {
	    try {
	        if (roomNumber.isEmpty()) {
	            return false; // Return false if room number is empty
	        }
	        Connection connection = dbConnect.getDatabaseConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DBLogRooms WHERE roomNo = ?");
	        preparedStatement.setInt(1, Integer.parseInt(roomNumber));
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSet.next();
	    } catch (SQLException | NumberFormatException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	private boolean isComputerExists(String roomNumber, String computerNumber) {
	    try {
	        if (roomNumber.isEmpty() || computerNumber.isEmpty()) {
	            return false; // Return false if room number or computer number is empty
	        }
	        Connection connection = dbConnect.getDatabaseConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DBLogComputer WHERE roomNo = ? AND pcno = ?");
	        preparedStatement.setInt(1, Integer.parseInt(roomNumber));
	        preparedStatement.setInt(2, Integer.parseInt(computerNumber));
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSet.next();
	    } catch (SQLException | NumberFormatException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	private boolean isComputerReported(String roomNumber, String computerNumber) {
	    try {
	        if (roomNumber.isEmpty() || computerNumber.isEmpty()) {
	            return false; // Return false if room number or computer number is empty
	        }
	        Connection connection = dbConnect.getDatabaseConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DBLogbook WHERE roomNum = ? AND computerNum = ?");
	        preparedStatement.setInt(1, Integer.parseInt(roomNumber));
	        preparedStatement.setInt(2, Integer.parseInt(computerNumber));
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSet.next();
	    } catch (SQLException | NumberFormatException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	

	
	private void reportFault(String username, String roomNumber, String computerNumber, String fault) {
	    try {
	        // Get the current date
	        LocalDate currentDate = LocalDate.now();
	        
	        // Insert the fault report into the database
	        String query = "INSERT INTO DBLogbook (dateLogged, roomNum, computerNum, fault, status, name) VALUES (?, ?, ?, ?, 'broken', ?)";
	        Connection connection = dbConnect.getDatabaseConnection();
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setDate(1, Date.valueOf(currentDate));
	        preparedStatement.setInt(2, Integer.parseInt(roomNumber));
	        preparedStatement.setInt(3, Integer.parseInt(computerNumber));
	        preparedStatement.setString(4, fault);
	        preparedStatement.setString(5, username);
	        preparedStatement.executeUpdate();
	        
	        // Show success message
	        showAlertAndWait(Alert.AlertType.INFORMATION, "Fault reported successfully!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Show error message
	        showAlertAndWait(Alert.AlertType.ERROR, "Error reporting fault!");
	    }
	}
	private void showAlertAndWait(Alert.AlertType type, String message) {
	    Alert alert = new Alert(type, message, ButtonType.OK);
	    setAlertStyles(alert);
	    alert.showAndWait();
	}

	private void setAlertStyles(Alert alert) {
	    // Set styles for the dialog pane
	    alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background

	    // Set styles for the content area
	    alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");

	    // Set styles for the buttons
	    alert.getDialogPane().lookupAll(".button").forEach(node ->
	        node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
	    );
	}
}
