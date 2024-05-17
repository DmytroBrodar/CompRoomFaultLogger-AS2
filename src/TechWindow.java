import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class TechWindow extends Application {
    private Connection connection;
    private TableView<FaultEntry> logBookTable;
    private TableView<FaultEntry> resolvedTable;
    private TableView<FaultEntry> brokenComputersTable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Connect to the database
        DbConnect dbConnect = new DbConnect();
        dbConnect.connect();
        connection = dbConnect.getDatabaseConnection();

        // Create tables
        logBookTable = new TableView<>();
        createFaultEntryTable(logBookTable, "LogBook");

        resolvedTable = new TableView<>();
        createFaultEntryTable(resolvedTable, "Resolved");

        brokenComputersTable = new TableView<>();
        createFaultEntryTable(brokenComputersTable, "BrokenComputers");

        // Buttons
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("login-button");
        backButton.setOnAction(e -> primaryStage.close());
        Button viewFaultsByRoomBtn = new Button("View Faults by Room");
        viewFaultsByRoomBtn.getStyleClass().add("login-button");
        viewFaultsByRoomBtn.setOnAction(e -> viewFaultsByRoom());
        Button viewIndividualFaultBtn = new Button("View Individual Fault");
        viewIndividualFaultBtn.getStyleClass().add("login-button");
        viewIndividualFaultBtn.setOnAction(e -> viewIndividualFault());
        Button indicateRepairStatusBtn = new Button("Indicate Repair Status");
        indicateRepairStatusBtn.getStyleClass().add("login-button");
        indicateRepairStatusBtn.setOnAction(e -> indicateRepairStatus());
        Button markRepairCompletedBtn = new Button("Mark Repair Completed");
        markRepairCompletedBtn.getStyleClass().add("login-button");
        markRepairCompletedBtn.setOnAction(e -> markRepairCompleted());
        Button generateReportBtn = new Button("Generate Report");
        generateReportBtn.getStyleClass().add("login-button");
        generateReportBtn.setOnAction(e -> generateReport());

        HBox buttonsBox = new HBox(10,
        		backButton,
                viewFaultsByRoomBtn,
                viewIndividualFaultBtn,
                indicateRepairStatusBtn,
                markRepairCompletedBtn,
                generateReportBtn);
        buttonsBox.getStyleClass().add("login-root");
        buttonsBox.setPadding(new Insets(10));

        VBox tablesBox = new VBox(10,new Label("LogBook"), logBookTable, new Label("Resolved"), 
        		resolvedTable, new Label("Broken Computers more than 5 days"), brokenComputersTable);
        tablesBox.getStyleClass().add("login-root");
        tablesBox.setPadding(new Insets(10));

        root.setCenter(tablesBox);
        root.setBottom(buttonsBox);

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Technician Window");
        primaryStage.show();
    }

    @SuppressWarnings("unchecked")
    private void createFaultEntryTable(TableView<FaultEntry> table, String tableName) {
        TableColumn<FaultEntry, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<FaultEntry, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<FaultEntry, Integer> roomNumColumn = new TableColumn<>("Room Number");
        roomNumColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumProperty().asObject());

        TableColumn<FaultEntry, Integer> computerNumColumn = new TableColumn<>("Computer Number");
        computerNumColumn.setCellValueFactory(cellData -> cellData.getValue().computerNumProperty().asObject());

        TableColumn<FaultEntry, String> faultColumn = new TableColumn<>("Fault");
        faultColumn.setCellValueFactory(cellData -> cellData.getValue().faultProperty());

        TableColumn<FaultEntry, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        TableColumn<FaultEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        table.getColumns().addAll(idColumn, dateColumn, roomNumColumn, computerNumColumn, faultColumn, statusColumn, nameColumn);

        switch (tableName) {
            case "LogBook":
                loadFaultEntries(table, "DBLogbook");
                break;
            case "Resolved":
                loadFaultEntries(table, "DBResolved");
                break;
            case "BrokenComputers":
                loadBrokenComputers(table);
                break;
        }
    }


    private void loadFaultEntries(TableView<FaultEntry> table, String tableName) {
        ObservableList<FaultEntry> data = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date;
                if (tableName.equals("DBResolved")) {
                    date = resultSet.getDate("dateFixed");
                } else { // Assuming the default case is "DBLogbook"
                    date = resultSet.getDate("dateLogged");
                }
                int roomNum = resultSet.getInt("roomNum");
                int computerNum = resultSet.getInt("computerNum");
                String fault = resultSet.getString("fault");
                String status = resultSet.getString("status");
                String name = resultSet.getString("name");

                FaultEntry faultEntry = new FaultEntry(id, date, roomNum, computerNum, fault, status, name);
                data.add(faultEntry);
            }

            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadBrokenComputers(TableView<FaultEntry> table) {
        ObservableList<FaultEntry> data = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DBLogbook WHERE dateLogged < DATEADD(day, -5, GETDATE())");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("dateLogged");
                int roomNum = resultSet.getInt("roomNum");
                int computerNum = resultSet.getInt("computerNum");
                String fault = resultSet.getString("fault");
                String status = resultSet.getString("status");
                String name = resultSet.getString("name");

                FaultEntry faultEntry = new FaultEntry(id, date, roomNum, computerNum, fault, status, name);
                data.add(faultEntry);
            }

            table.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewFaultsByRoom() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Faults by Room");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Room Number:");
        
        dialog.getDialogPane().getStyleClass().add("login-root");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(roomNumber -> {
            try {
                int roomNum = Integer.parseInt(roomNumber);
                
                // Check if the room number exists in the database
                boolean roomExists = checkRoomExists(roomNum);
                
                if (!roomExists) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Room " + roomNum + " does not exist.");
                    return;
                }
                
                ObservableList<FaultEntry> data = FXCollections.observableArrayList();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM DBLogbook WHERE roomNum = ?");
                statement.setInt(1, roomNum);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Date date = resultSet.getDate("dateLogged");
                    int computerNum = resultSet.getInt("computerNum");
                    String fault = resultSet.getString("fault");
                    String status = resultSet.getString("status");
                    String name = resultSet.getString("name");

                    FaultEntry faultEntry = new FaultEntry(id, date, roomNum, computerNum, fault, status, name);
                    data.add(faultEntry);
                }

                TableView<FaultEntry> tableView = new TableView<>(data);
                createFaultEntryTable(tableView, "Room " + roomNum);
                tableView.setPrefWidth(500);
                tableView.setPrefHeight(400);
                tableView.getStyleClass().add("login-root");
                Stage stage = new Stage();
                stage.setScene(new Scene(tableView));
                stage.setTitle("Faults in Room " + roomNum);
                stage.show();
                
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid room number. Please enter a valid room number.");
            }
        });
    }

    private void viewIndividualFault() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("View Individual Fault");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Room Number:");
        
        dialog.getDialogPane().getStyleClass().add("login-root");

        Optional<String> roomResult = dialog.showAndWait();

        roomResult.ifPresent(roomNumber -> {
            try {
                int roomNum = Integer.parseInt(roomNumber);

                // Check if the room number exists in the database
                boolean roomExists = checkRoomExists(roomNum);

                if (!roomExists) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid room number.");
                    return;
                }

                // Prompt user to enter computer number
                TextInputDialog computerDialog = new TextInputDialog();
                computerDialog.setTitle("View Individual Fault");
                computerDialog.setHeaderText(null);
                computerDialog.setContentText("Enter Computer Number:");
                
                computerDialog.getDialogPane().getStyleClass().add("login-root");

                Optional<String> computerResult = computerDialog.showAndWait();

                computerResult.ifPresent(computerNumber -> {
                    try {
                        int compNum = Integer.parseInt(computerNumber);

                        // Check if the computer number exists in the database
                        boolean computerExists = checkComputerExists(roomNum, compNum);

                        if (!computerExists) {
                            showAlert(Alert.AlertType.ERROR, "Error", "Invalid computer number.");
                            return;
                        }

                        // Proceed with viewing individual fault for the specified room and computer
                        displayIndividualFault(roomNum, compNum);
                        // Implement logic to retrieve and display individual fault
                    } catch (NumberFormatException e) {
                        // Handle invalid input (non-integer computer number)
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid computer number. Please enter a valid number.");
                    }
                });
            } catch (NumberFormatException e) {
                // Handle invalid input (non-integer room number)
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid room number. Please enter a valid number.");
            }
        });
    }

    private boolean checkComputerExists(int roomNumber, int computerNumber) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT pcno FROM DBLogComputer WHERE roomNo = ? AND pcno = ?");
            statement.setInt(1, roomNumber);
            statement.setInt(2, computerNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if computer number exists in the specified room, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any exception
        }
    }


    private boolean checkRoomExists(int roomNumber) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT roomNo FROM DBLogRooms WHERE roomNo = ?");
            statement.setInt(1, roomNumber);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if room number exists, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of any exception
        }
    }

    private void displayIndividualFault(int roomNumber, int computerNumber) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DBLogbook WHERE roomNum = ? AND computerNum = ?");
            statement.setInt(1, roomNumber);
            statement.setInt(2, computerNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("dateLogged");
                String roomNum = resultSet.getString("roomNum");
                String computerNum = resultSet.getString("computerNum");
                String fault = resultSet.getString("fault");
                String status = resultSet.getString("status");
                String name = resultSet.getString("name");

                // Display the individual fault information
                showAlert(Alert.AlertType.INFORMATION, "Individual Fault Details", "ID: " + id + "\n" +
                        "Room Number: " + roomNum + "\n" + 
                		"Computer Number: " + computerNum + "\n" +
                		"Date: " + date.toString() + "\n" +
                        "Fault: " + fault + "\n" +
                        "Status: " + status + "\n" +
                        "Logged by: " + name);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No fault found for room " + roomNumber + ", computer " + computerNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void indicateRepairStatus() {
        // Get selected fault entry
        FaultEntry selectedFault = logBookTable.getSelectionModel().getSelectedItem();
        if (selectedFault == null) {
            // No fault selected
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a fault entry to indicate repair status.");
            return;
        }

        // Prompt the technician to input the repair status
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Indicate Repair Status");
        dialog.setHeaderText("Enter the repair status for the selected fault entry:");
        dialog.setContentText("Repair Status for Computer " + selectedFault.getComputerNum() + " in Room " + selectedFault.getRoomNum() + ":");

        dialog.getDialogPane().getStyleClass().add("login-root");
        
        // Show dialog and wait for response
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String repairStatus = result.get();
            // Update the repair status in the database
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE DBLogbook SET status = ? WHERE id = ?");
                statement.setString(1, repairStatus);
                statement.setInt(2, selectedFault.getId());
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    // Update successful, refresh the log book table
                    loadFaultEntries(logBookTable, "DBLogbook");
                } else {
                    // Update failed
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update repair status.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Database error
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating repair status.");
            }
        }
    }



    private void markRepairCompleted() {
        // Get selected fault entry
        FaultEntry selectedFault = logBookTable.getSelectionModel().getSelectedItem();
        if (selectedFault == null) {
            // No fault selected
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a fault entry to mark repair completed.");
            return;
        }

        // Confirm with the technician before marking the repair as completed
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Mark Repair Completed");
        confirmationAlert.setHeaderText("Mark Repair Completed");
        confirmationAlert.setContentText("Are you sure you want to mark this repair as completed for " +
                "Computer: " + selectedFault.getComputerNum() + " in " +
                "Room: " + selectedFault.getRoomNum());
        confirmationAlert.getDialogPane().getStyleClass().add("login-root");
        confirmationAlert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background
        confirmationAlert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 12px;-fx-font-weight: bold;");
        confirmationAlert.getDialogPane().lookupAll(".button").forEach(node ->
            node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
        );
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Move the fault entry from log book table to resolved table
                PreparedStatement moveStatement = connection.prepareStatement(
                        "INSERT INTO DBResolved (dateFixed, roomNum, computerNum, fault, status, name) " +
                                "VALUES (?, ?, ?, ?, ?, ?)");
                moveStatement.setDate(1, new Date(System.currentTimeMillis()));
                moveStatement.setInt(2, selectedFault.getRoomNum());
                moveStatement.setInt(3, selectedFault.getComputerNum());
                moveStatement.setString(4, selectedFault.getFault());
                moveStatement.setString(5, "resolved");
                moveStatement.setString(6, selectedFault.getName());
                moveStatement.executeUpdate();

                // Delete the fault entry from log book table
                PreparedStatement deleteStatement = connection.prepareStatement(
                        "DELETE FROM DBLogbook WHERE id = ?");
                deleteStatement.setInt(1, selectedFault.getId());
                deleteStatement.executeUpdate();

                // Refresh log book and resolved tables
                loadFaultEntries(logBookTable, "DBLogbook");
                loadFaultEntries(resolvedTable, "DBResolved");
            } catch (SQLException e) {
                e.printStackTrace();
                // Database error
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while marking repair completed.");
            }
        }
    }

    private void generateReport() {
        try {
            // Query to get the count of faults per room from both Logbook and Resolved tables
            String query = "SELECT roomNum, SUM(faultCount) AS totalFaults " +
                           "FROM ( " +
                           "    SELECT roomNum, COUNT(*) AS faultCount " +
                           "    FROM DBLogbook " +
                           "    GROUP BY roomNum " +
                           "    UNION ALL " +
                           "    SELECT roomNum, COUNT(*) AS faultCount " +
                           "    FROM DBResolved " +
                           "    GROUP BY roomNum " +
                           ") AS CombinedFaults " +
                           "GROUP BY roomNum";

            // Execute the query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            int maxFaultCount = -1;
            int roomWithMaxFaults = -1;

            // Iterate through the result set to find the room with the most faults
            while (resultSet.next()) {
                int roomNum = resultSet.getInt("roomNum");
                int totalFaults = resultSet.getInt("totalFaults");

                if (totalFaults > maxFaultCount) {
                    maxFaultCount = totalFaults;
                    roomWithMaxFaults = roomNum;
                }
            }

            if (roomWithMaxFaults != -1) {
                // Display the report
                showAlert(Alert.AlertType.INFORMATION, "Report on the room with the most faults", "The room with the most faults per computer is Room " + roomWithMaxFaults +
                        " with " + maxFaultCount + " faults.");
            } else {
                // No faults logged
                showAlert(Alert.AlertType.INFORMATION, "Report", "No Faults Logged");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Database error
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while generating the report.");
        }
    }
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
    public static class FaultEntry {
        private final Integer id;
        private final Date date;
        private final Integer roomNum;
        private final Integer computerNum;
        private final String fault;
        private final String status;
        private final String name;

        public FaultEntry(Integer id, Date date, Integer roomNum, Integer computerNum, String fault, String status, String name) {
            this.id = id;
            this.date = date;
            this.roomNum = roomNum;
            this.computerNum = computerNum;
            this.fault = fault;
            this.status = status;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public Date getDate() {
            return date;
        }

        public Integer getRoomNum() {
            return roomNum;
        }

        public Integer getComputerNum() {
            return computerNum;
        }

        public String getFault() {
            return fault;
        }

        public String getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public IntegerProperty idProperty() {
            return new SimpleIntegerProperty(id);
        }

        public ObjectProperty<Date> dateProperty() {
            return new SimpleObjectProperty<>(date);
        }

        public IntegerProperty roomNumProperty() {
            return new SimpleIntegerProperty(roomNum);
        }

        public IntegerProperty computerNumProperty() {
            return new SimpleIntegerProperty(computerNum);
        }

        public StringProperty faultProperty() {
            return new SimpleStringProperty(fault);
        }

        public StringProperty statusProperty() {
            return new SimpleStringProperty(status);
        }

        public StringProperty nameProperty() {
            return new SimpleStringProperty(name);
        }
    }
}
