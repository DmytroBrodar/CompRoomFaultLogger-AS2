import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

public class AdminWindow extends Application {

    private DbConnect dbConnect;
    
    public AdminWindow(DbConnect dbConnect) { 
    	 this.dbConnect = dbConnect;
    }


	public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	Connection connection = dbConnect.getDatabaseConnection();

    	primaryStage.setTitle("Admin Panel");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        // Labels
        Label titleLabel = new Label("Administrator Panel");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        
        // Tables
        TableView<ObservableList<String>> roomsTable = new TableView<>();
        TableView<ObservableList<String>> computersTable = new TableView<>();
        TableView<ObservableList<String>> usersTable = new TableView<>();

        // Load data into tables
        loadTableData(roomsTable, "DBLogRooms", connection);
        loadTableData(computersTable, "DBLogComputer", connection);
        loadTableData(usersTable, "DBLogUsers", connection);
        
        // Table Labels
        Label roomsLabel = new Label("Computer Rooms");
        Label computersLabel = new Label("Computers");
        Label usersLabel = new Label("Users");

        // Buttons
        Button addRoomButton = new Button("Add Room");
        Button modifyRoomButton = new Button("Modify Room");
        Button removeRoomButton = new Button("Remove Room");
        Button addComputerButton = new Button("Add Computer");
        Button modifyComputerButton = new Button("Modify Computer");
        Button removeComputerButton = new Button("Remove Computer");
        Button addUserButton = new Button("Add User");
        Button removeUserButton = new Button("Remove User");
        Button backButton = new Button("Back");
        Button helpButton = new Button("Help");

        // Button HBox
        HBox roomsButtonBox = new HBox(10, addRoomButton, modifyRoomButton, removeRoomButton);
        HBox computersButtonBox = new HBox(10, addComputerButton, modifyComputerButton, removeComputerButton);
        HBox usersButtonBox = new HBox(10, addUserButton, removeUserButton);
        HBox bottomButtonBox = new HBox(10, backButton, helpButton);
        bottomButtonBox.setAlignment(Pos.CENTER); 
        
        // Apply CSS styles to UI components
        roomsLabel.getStyleClass().add("login-label");
        computersLabel.getStyleClass().add("login-label");
        usersLabel.getStyleClass().add("login-label");
        addRoomButton.getStyleClass().add("login-button");
        modifyRoomButton.getStyleClass().add("login-button");
        removeRoomButton.getStyleClass().add("login-button");
        addComputerButton.getStyleClass().add("login-button");
        modifyComputerButton.getStyleClass().add("login-button");
        removeComputerButton.getStyleClass().add("login-button");
        addUserButton.getStyleClass().add("login-button");
        removeUserButton.getStyleClass().add("login-button");
        backButton.getStyleClass().add("login-button");
        helpButton.getStyleClass().add("login-button");

        // Layout for each table
        VBox roomsBox = new VBox(10, roomsLabel, roomsTable, roomsButtonBox);
        VBox computersBox = new VBox(10, computersLabel, computersTable, computersButtonBox);
        VBox usersBox = new VBox(10, usersLabel, usersTable, usersButtonBox);

        // Set alignment for labels
        roomsLabel.setAlignment(Pos.CENTER);
        computersLabel.setAlignment(Pos.CENTER);
        usersLabel.setAlignment(Pos.CENTER);
        
        // Layout for tables
        HBox tablesBox = new HBox(25, roomsBox, computersBox, usersBox);
        tablesBox.setAlignment(Pos.CENTER);
        VBox.setMargin(tablesBox, new Insets(20, 0, 20, 0));

        // Add components to border pane
        borderPane.setTop(titleLabel);
        borderPane.setCenter(tablesBox);
        borderPane.setBottom(bottomButtonBox);
        borderPane.getStyleClass().add("login-root");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        addRoomButton.setOnAction(e -> addRoom(primaryStage, connection, roomsTable));
        modifyRoomButton.setOnAction(e -> modifyRoom(primaryStage, connection, roomsTable));
        removeRoomButton.setOnAction(e -> removeRoom(connection, roomsTable));
        addComputerButton.setOnAction(e -> addComputer(primaryStage, connection, computersTable));
        modifyComputerButton.setOnAction(e -> modifyComputer(primaryStage, connection, computersTable));
        removeComputerButton.setOnAction(e -> removeComputer(connection, computersTable));
        addUserButton.setOnAction(e -> addUser(primaryStage, connection, usersTable));
        removeUserButton.setOnAction(e -> removeUser(connection, usersTable));
        backButton.setOnAction(e -> primaryStage.close());
        helpButton.setOnAction(e -> help());
        
        // Set scene
        Scene scene = new Scene(borderPane, 1250, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadTableData(TableView<ObservableList<String>> tableView, String tableName, Connection connection) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Clear previous data
            tableView.getColumns().clear();
            tableView.getItems().clear();

            // Add columns
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                final int index = i - 1;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(i));
                column.setCellValueFactory(data -> data.getValue().get(index) != null
                        ? new SimpleStringProperty(data.getValue().get(index)) : null);
                tableView.getColumns().add(column);
            }

            // Add data rows
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                tableView.getItems().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addRoom(Stage primaryStage, Connection connection, TableView<ObservableList<String>> roomsTable) {
        // Create form components
        TextField roomNoField = new TextField();
        TextField totalNumOfPCField = new TextField();
        Button addButton = new Button("Add");
        
        // Apply CSS styles to UI components
        roomNoField.getStyleClass().add("login-text-field");
        totalNumOfPCField.getStyleClass().add("login-text-field");
        addButton.getStyleClass().add("login-button");

        // Form layout
        VBox addRoomLayout = new VBox(10, new Label("Room Number:"), roomNoField, new Label("Total Number of PCs:"), totalNumOfPCField, addButton);
        addRoomLayout.setAlignment(Pos.CENTER);
        addRoomLayout.getStyleClass().add("login-root");

        // Add room button action
        addButton.setOnAction(e -> {
            String roomNo = roomNoField.getText().trim();
            String totalNumOfPC = totalNumOfPCField.getText().trim();
            if (!roomNo.isEmpty() && !totalNumOfPC.isEmpty()) {
                try {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO DBLogRooms (roomNo, totalnumofpc) VALUES (?, ?)");
                    statement.setString(1, roomNo);
                    statement.setString(2, totalNumOfPC);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Refresh table
                        loadTableData(roomsTable, "DBLogRooms", connection);
                        // Clear fields
                        roomNoField.clear();
                        totalNumOfPCField.clear();
                        // Show success alert
                        showAlert(Alert.AlertType.INFORMATION, "Room Added", "Room " + roomNo + " has been successfully added.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Create a new stage for adding room
        Stage addRoomStage = new Stage();
        addRoomStage.setTitle("Add Room");
        Scene scene = new Scene(addRoomLayout, 300, 200);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Set stylesheet
        addRoomStage.setScene(scene);
        addRoomStage.show();
    }

    private void modifyRoom(Stage primaryStage, Connection connection, TableView<ObservableList<String>> roomsTable) {
        // Get selected room
        ObservableList<String> selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String roomId = selectedRoom.get(0); // Assuming the first column is the room number

            // Create form components
            TextField roomNoField = new TextField(selectedRoom.get(0)); // Pre-fill room number field
            TextField totalNumOfPCField = new TextField(selectedRoom.get(1)); // Pre-fill total number of PCs field
            Button modifyButton = new Button("Modify");

            // Apply CSS styles to UI components
            roomNoField.getStyleClass().add("login-text-field");
            totalNumOfPCField.getStyleClass().add("login-text-field");
            modifyButton.getStyleClass().add("login-button");

            // Form layout
            VBox modifyRoomLayout = new VBox(10, new Label("Room Number:"), roomNoField, new Label("Total Number of PCs:"), totalNumOfPCField, modifyButton);
            modifyRoomLayout.setAlignment(Pos.CENTER);
            modifyRoomLayout.getStyleClass().add("login-root");

            // Modify room button action
            modifyButton.setOnAction(e -> {
                String newRoomNo = roomNoField.getText().trim();
                String newTotalNumOfPC = totalNumOfPCField.getText().trim();
                if (!newRoomNo.isEmpty() && !newTotalNumOfPC.isEmpty()) {
                    // Ask for confirmation
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation Dialog");
                    confirmationAlert.setHeaderText("Modify Room");
                    confirmationAlert.setContentText("Are you sure you want to modify the room?");
                    confirmationAlert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
                    confirmationAlert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
                    confirmationAlert.getDialogPane().lookupAll(".button").forEach(node ->
                        node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
                    );
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            PreparedStatement statement = connection.prepareStatement("UPDATE DBLogRooms SET roomNo = ?, totalnumofpc = ? WHERE roomNo = ?");
                            statement.setString(1, newRoomNo);
                            statement.setString(2, newTotalNumOfPC);
                            statement.setString(3, roomId);
                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                // Refresh table
                                loadTableData(roomsTable, "DBLogRooms", connection);
                                // Show success alert
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Room modified successfully.");
                                // Clear fields
                                roomNoField.clear();
                                totalNumOfPCField.clear();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    // Display an error message if any field is empty
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                }
            });

            // Create a new stage for modifying room
            Stage modifyRoomStage = new Stage();
            modifyRoomStage.setTitle("Modify Room");
            Scene scene = new Scene(modifyRoomLayout, 300, 200);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Set stylesheet
            modifyRoomStage.setScene(scene);
            modifyRoomStage.show();
        } else {
            // Inform user to select a room first
            showAlert(Alert.AlertType.INFORMATION, "Information", "Please select a room to modify.");
        }
    }



    private void removeRoom(Connection connection, TableView<ObservableList<String>> roomsTable) {
        // Get selected room
        ObservableList<String> selectedRoom = roomsTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            String roomNo = selectedRoom.get(0); 
            
            // Ask for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Remove Room");
            alert.setContentText("Are you sure you want to remove room " + roomNo + "?");
            alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
            alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
            alert.getDialogPane().lookupAll(".button").forEach(node ->
                node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
            );
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                	/*
                	// Remove computers from the deleted room
                    PreparedStatement deleteComputersStatement = connection.prepareStatement("DELETE FROM DBLogComputer WHERE roomNo = ?");
                    deleteComputersStatement.setString(1, roomNo);
                    deleteComputersStatement.executeUpdate();
                    */

                    // Remove the room itself
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM DBLogRooms WHERE roomNo = ?");
                    statement.setString(1, roomNo);
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Refresh table
                        loadTableData(roomsTable, "DBLogRooms", connection);
                     // Show success alert
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Room " + roomNo + " removed successfully.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Inform user to select a room first
            showAlert(Alert.AlertType.INFORMATION, "Information", "Please select a room to delete.");
        }
    }

    private void addComputer(Stage primaryStage, Connection connection, TableView<ObservableList<String>> computersTable) {
    	// Create form 
    	TextField roomNoField = new TextField();
    	TextField pcNoField = new TextField();
    	TextField serialNoField = new TextField();
    	TextField modelField = new TextField();
    	TextField manufacturerField = new TextField();
    	Button addButton = new Button("Add");
    	
        roomNoField.getStyleClass().add("login-text-field");
        pcNoField.getStyleClass().add("login-text-field");
        serialNoField.getStyleClass().add("login-text-field");
        modelField.getStyleClass().add("login-text-field");
        manufacturerField.getStyleClass().add("login-text-field");
        addButton.getStyleClass().add("login-button");
    	
    	//Form Layout
    	VBox addComputerLayout = new VBox(10, new Label ("Room Number:"), roomNoField, new Label("PC Number:"), pcNoField, new Label ("Serial Number:"), serialNoField, 
    			new Label ("Model:"), modelField, new Label ("Manufacturer:"), manufacturerField, addButton);
    	addComputerLayout.setAlignment(Pos.CENTER);
    	addComputerLayout.getStyleClass().add("login-root");
    	
    	// Add a button action
    	addButton.setOnAction(e -> {
    		String roomNo = roomNoField.getText().trim();
    		String pcNo = pcNoField.getText().trim();
    		String serialNo = serialNoField.getText().trim();
    		String model = modelField.getText().trim();
    		String manufacturer = manufacturerField.getText().trim();
    		if (!roomNo.isEmpty() && !pcNo.isEmpty() && !serialNo.isEmpty() && !model.isEmpty() && !manufacturer.isEmpty()) {
    			try {
    				// Check if roomNo exists
    				PreparedStatement checkRoomStatement = connection.prepareStatement("SELECT COUNT(*) FROM DBLogRooms WHERE roomNo = ?");
    				checkRoomStatement.setString(1, roomNo);
    				ResultSet resultSet = checkRoomStatement.executeQuery();
    				resultSet.next();
    				int roomCount = resultSet.getInt(1);
    				if (roomCount > 0) {
    					PreparedStatement checkPcNoStatement = connection.prepareStatement("SELECT COUNT(*) FROM DBLogComputer WHERE pcNo = ?");
    					checkPcNoStatement.setString(1, pcNo);
    					resultSet = checkPcNoStatement.executeQuery();
    					resultSet.next();
    					int pcNoCount = resultSet.getInt(1);
    					if (pcNoCount == 0) {
    	    				PreparedStatement statement = connection.prepareStatement("INSERT INTO DBLogComputer (roomNo, pcNo, serialNo, model, manufacturer) VALUES (?,?,?,?,?)");
    	                    statement.setString(1, roomNo);
    	                    statement.setString(2, pcNo); 
    	                    statement.setString(3, serialNo);
    	                    statement.setString(4, model);
    	                    statement.setString(5, manufacturer);
    	    				int rowsAffected = statement.executeUpdate();
    	    				if (rowsAffected > 0) {
    	    					// Refresh table
    	    					loadTableData(computersTable, "DBLogComputer", connection);
    	    					// Clear fields
    	    					roomNoField.clear();
    	    					pcNoField.clear();
    	    					serialNoField.clear();
    	    					modelField.clear();
    	    					manufacturerField.clear();
    	    					// Show success alert
                                showAlert(Alert.AlertType.INFORMATION, "Success", "Computer added successfully.");
    					}

	    				} else {
	                        // pcNo already exists, display error message
	                        showAlert(Alert.AlertType.ERROR, "Error", "The PC Number already exists. Please use a different PC Number.");
	                    }
    				} else {
    					// Room doesn't exist, error message
    					showAlert(Alert.AlertType.ERROR, "Error", "Room doesn't exist.Please create the room first.");
    				}
    			}
    				catch (SQLException ex) {
    				ex.printStackTrace();
    			}
    		}  else {
                // Display an error message if any field is empty
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
    		}
    	});
    	
    	// Create stage for adding computer
    	Stage addComputerStage = new Stage();
    	addComputerStage.setTitle("Add Computer");
        Scene scene = new Scene(addComputerLayout, 300, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Set stylesheet
        addComputerStage.setScene(scene);
        addComputerStage.show();
    }

    private void modifyComputer(Stage primaryStage, Connection connection, TableView<ObservableList<String>> computersTable) {
        // Get selected computer
        ObservableList<String> selectedComputer = computersTable.getSelectionModel().getSelectedItem();
        if (selectedComputer != null) {
            String computerNo = selectedComputer.get(2); 

            // Create form components
            TextField roomNoField = new TextField(selectedComputer.get(1));
            TextField serialNoField = new TextField(selectedComputer.get(2));
            TextField modelField = new TextField(selectedComputer.get(3));
            TextField manufacturerField = new TextField(selectedComputer.get(4));
            Button modifyButton = new Button("Modify");

            // Apply CSS styles to UI components
            roomNoField.getStyleClass().add("login-text-field");
            serialNoField.getStyleClass().add("login-text-field");
            modelField.getStyleClass().add("login-text-field");
            manufacturerField.getStyleClass().add("login-text-field");
            modifyButton.getStyleClass().add("login-button");
            
            // Form layout
            VBox modifyComputerLayout = new VBox(10, new Label ("Room Number:"), roomNoField, new Label ("Serial Number:"), serialNoField, 
                    new Label ("Model:"), modelField, new Label ("Manufacturer:"), manufacturerField, modifyButton);
            modifyComputerLayout.setAlignment(Pos.CENTER);
            modifyComputerLayout.getStyleClass().add("login-root");

            // Modify button action
            modifyButton.setOnAction(e -> {
                String newRoomNo = roomNoField.getText().trim();
                String newSerialNo = serialNoField.getText().trim();
                String newModel = modelField.getText().trim();
                String newManufacturer = manufacturerField.getText().trim();
                if (!newRoomNo.isEmpty() && !newSerialNo.isEmpty() && !newModel.isEmpty() && !newManufacturer.isEmpty()) {
                	// Confirmation alert before modifying computer
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText("Modify Computer");
                    confirmationAlert.setContentText("Are you sure you want to modify this computer?");
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
	                	try {
	                        PreparedStatement statement = connection.prepareStatement("UPDATE DBLogComputer SET roomNo = ?, serialNo = ?, model = ?, manufacturer = ? WHERE serialNo = ?");
	                        statement.setString(1, newRoomNo);
	                        statement.setString(2, newSerialNo);
	                        statement.setString(3, newModel);
	                        statement.setString(4, newManufacturer);
	                        statement.setString(5, computerNo);
	
	                        int rowsAffected = statement.executeUpdate();
	                        if (rowsAffected > 0) {
	                            // Refresh table
	                            loadTableData(computersTable, "DBLogComputer", connection);
	                            // Clear fields
	                            roomNoField.clear();
	                            serialNoField.clear();
	                            modelField.clear();
	                            manufacturerField.clear();
	                            // Success alert for modifying computer
	                            showAlert(Alert.AlertType.INFORMATION, "Success", "Computer modified successfully.");
	                        }
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                    
	                    }
	                	}   
                } else {
                    // Display an error message if any field is empty
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                }
            });

            // Create stage for modifying computer
            Stage modifyComputerStage = new Stage();
            modifyComputerStage.setTitle("Modify Computer");
            Scene scene = new Scene(modifyComputerLayout, 300, 400);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Set stylesheet
            modifyComputerStage.setScene(scene);
            modifyComputerStage.show();
        } else {
            // Display an error message if no computer is selected
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a computer to modify.");
        }
    }


    private void removeComputer(Connection connection, TableView<ObservableList<String>> computersTable) {
        // Get selected computer
    	ObservableList<String> selectedComputer = computersTable.getSelectionModel().getSelectedItem();
    	if (selectedComputer != null) {
    		String computerSerialNo = selectedComputer.get(2);
    		
    		// Ask user confirmation
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation");
    		alert.setHeaderText("Remove Computer");
    		alert.setContentText("Are you shure you want to remove computer " + computerSerialNo + "?");
            alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
            alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
            alert.getDialogPane().lookupAll(".button").forEach(node ->
                node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
            );
    		// Get user's response
    		Optional<ButtonType> result = alert.showAndWait();
    		
            // Proceed with deletion if user confirms
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				// Remove computer from the room
    				PreparedStatement statement = connection.prepareStatement("DELETE FROM DBLogComputer WHERE serialNo = ?");
    				statement.setString(1, computerSerialNo);
    				int rowsAffected = statement.executeUpdate();
    				if (rowsAffected > 0) {
    					// Refresh table
    					loadTableData(computersTable, "DBLogComputer", connection);
    					
                        // Provide feedback to the user
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Computer with Serial No. " + computerSerialNo + " removed successfully.");
    				}
    			} catch (SQLException e) {
                    // Error handling for SQL exception
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while removing the computer. Please try again.");
                    e.printStackTrace();
    			}
    		}
    	} else {
    		// Inform user to select a computer first
    		showAlert(Alert.AlertType.INFORMATION, "Information", "Please select a computer to delete.");
    	}
    }

    private void addUser(Stage primaryStage, Connection connection, TableView<ObservableList<String>> usersTable) {
        // Create form
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        ComboBox<String> roleComboBox = new ComboBox<>(); // ComboBox for user roles
        roleComboBox.getItems().addAll("Teacher", "Tech", "Admin"); // Add roles to the ComboBox
        Button addButton = new Button("Add");

        // Apply CSS styles to UI components
        usernameField.getStyleClass().add("login-text-field");
        passwordField.getStyleClass().add("login-text-field");
        firstNameField.getStyleClass().add("login-text-field");
        lastNameField.getStyleClass().add("login-text-field");
        roleComboBox.getStyleClass().add("login-text-field");
        addButton.getStyleClass().add("login-button");

        
        // Form layout
        VBox addUserLayout = new VBox(10, new Label("Username:"), usernameField, new Label("Password:"), passwordField,
                new Label("First Name:"), firstNameField, new Label("Last Name:"), lastNameField, new Label("Role:"), roleComboBox, addButton);
        addUserLayout.setAlignment(Pos.CENTER);
        addUserLayout.getStyleClass().add("login-root");

        // Add a button action
        addButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String role = roleComboBox.getValue();
            if (!username.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && role != null) {
                try {
                    // Check if user already exists
                    PreparedStatement checkUserStatement = connection.prepareStatement("SELECT COUNT(*) FROM DBLogUsers WHERE username = ?");
                    checkUserStatement.setString(1, username);
                    ResultSet resultSet = checkUserStatement.executeQuery();
                    resultSet.next();
                    int userCount = resultSet.getInt(1);
                    if (userCount == 0) {
                        // Insert new user into the database
                        PreparedStatement insertUserStatement = connection.prepareStatement("INSERT INTO DBLogUsers (username, password, fname, sname, role) VALUES (?, ?, ?, ?, ?)");
                        insertUserStatement.setString(1, username);
                        insertUserStatement.setString(2, password);
                        insertUserStatement.setString(3, firstName);
                        insertUserStatement.setString(4, lastName);
                        insertUserStatement.setString(5, role);
                        int rowsAffected = insertUserStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            // Refresh table
                            loadTableData(usersTable, "DBLogUsers", connection);

                            // Clear fields
                            usernameField.clear();
                            passwordField.clear();
                            firstNameField.clear();
                            lastNameField.clear();
                            roleComboBox.setValue(null);

                            // Provide feedback to the user
                             showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully.");
                        }
                    } else {
                        // Inform user that the username already exists
                    	showAlert(Alert.AlertType.WARNING, "Username Exists", "User with the given username already exists.");
                    }
                } catch (SQLException ex) {
                    // Error handling for SQL exception
                	showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the user. Please try again.");
                    ex.printStackTrace();
                }
            } else {
                // Inform user to fill in all fields
            	showAlert(Alert.AlertType.WARNING, "Empty Fields", "Please fill in all fields.");
            }
        });

        // Create stage for adding user
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add User");
        Scene scene = new Scene(addUserLayout, 300, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Set stylesheet
        addUserStage.setScene(scene);
        addUserStage.show();
    }


    private void removeUser(Connection connection, TableView<ObservableList<String>> usersTable) {
        // Get selected user
    	ObservableList<String> selectedUser = usersTable.getSelectionModel().getSelectedItem();
    	if (selectedUser !=null) {
    		String username = selectedUser.get(0);
    		
    		// Ask user confirmation
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation");
    		alert.setHeaderText("Remove User");
    		alert.setContentText("Are you shure to delete user " + username  + "?");
            alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
            alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
            alert.getDialogPane().lookupAll(".button").forEach(node ->
                node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
            );
    		// Get user's responce
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		// Procced with deletion if user confirms
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				// Remove user from the table
    				PreparedStatement statement = connection.prepareStatement("DELETE FROM DBLogUsers WHERE username = ?");
    				statement.setString(1, username);
    				int rowsAffected = statement.executeUpdate();
    				if (rowsAffected > 0) {
    					//Refresh table
    					loadTableData(usersTable, "DBLogUsers", connection);
    					
    					// Provide feedback to user
    					showAlert(Alert.AlertType.INFORMATION, "Success", "User " + username + " removed successfully.");
    					
    				}
    			} catch (SQLException e) {
    				// Error handling for SQL excepition
    				showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while removing the user. Please try again.");
                    e.printStackTrace();
    			}
    		} 
    	} else {
    		// Inform user to select a computer first
    		showAlert(Alert.AlertType.INFORMATION,"Information", "Please select a user to delete.");
    	}
    }
    	

/*    private void back() {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();
   }*/
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        // Apply custom styles to the alert dialog pane and buttons
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: rgb(210, 210, 210);"); // Light grey background
        dialogPane.lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
        dialogPane.lookupAll(".button").forEach(node ->
            node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px;")
        );
        
        alert.showAndWait();
    }
    
    private void help() {
        // Help information or dialog content
        String helpText = "Welcome to the Admin Panel!\n\n" +
                          "This panel allows administrators to manage computer rooms, computers, and users.\n\n" +
                          "Here are some basic functionalities:\n" +
                          "- Add, modify, or remove computer rooms\n" +
                          "- Add, modify, or remove computers\n" +
                          "- Add or remove users\n\n" +
                          "For more information, please refer to the user manual or contact support.";

        // Create a help dialog
        Alert helpDialog = new Alert(Alert.AlertType.INFORMATION);
        helpDialog.setTitle("Help");
        helpDialog.setHeaderText("Admin Panel Help");
        helpDialog.setContentText(helpText);
        helpDialog.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
        helpDialog.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");
        helpDialog.getDialogPane().lookupAll(".button").forEach(node ->
            node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
        );
        helpDialog.showAndWait();
    }
    
}
