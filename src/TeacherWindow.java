import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TeacherWindow extends Application {

    private DbConnect dbConnect;
    private String username;
    private LogBookTableView logBookTableView;
    
    public TeacherWindow(DbConnect dbConnect, String username) {
        this.dbConnect = dbConnect;
        this.username = username;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create UI components
        Button changePasswordButton = new Button("Change Password");
        Button reportFaultButton = new Button("Report a Fault");

        Label logBookLabel = new Label("LogBook");
        Label resolveLogBookLabel = new Label("Resolve LogBook");

        // Create tables for LogBook and Resolve LogBook
        LogBookTableView logBookTableView = new LogBookTableView(dbConnect);
        TableView<LogBookEntry> logBookTable = logBookTableView.createLogBookTableView();

        ResolveLogBookTableView resolveLogBookTableView = new ResolveLogBookTableView(dbConnect);
        TableView<ResolveLogBookEntry> resolveLogBookTable = resolveLogBookTableView.createResolveLogBookTableView();

        Button backButton = new Button("Back");
        Button helpButton = new Button("Help");

        // Set actions for buttons
        changePasswordButton.setOnAction(e -> openChangePasswordWindow());
        reportFaultButton.setOnAction(e -> openReportFaultWindow());
        backButton.setOnAction(e -> primaryStage.close());
        helpButton.setOnAction(e -> displayHelp());
        
        // Apply styles
        changePasswordButton.getStyleClass().add("login-button");
        reportFaultButton.getStyleClass().add("login-button");
        backButton.getStyleClass().add("login-button");
        helpButton.getStyleClass().add("login-button");
        logBookLabel.getStyleClass().add("login-label");
        resolveLogBookLabel.getStyleClass().add("login-label");

        // Create layout
        HBox topLayout = new HBox(10, changePasswordButton, reportFaultButton);
        HBox bottomLayout = new HBox(10, backButton, helpButton);
        VBox centerLayout = new VBox(10, logBookLabel, logBookTable, resolveLogBookLabel, resolveLogBookTable);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.getStyleClass().add("login-root");
        root.setTop(topLayout);
        root.setCenter(centerLayout);
        root.setBottom(bottomLayout);
        
        // Set alignment for labels
        //logBookLabel.setAlignment(Pos.CENTER);
        //resolveLogBookLabel.setAlignment(Pos.CENTER);
        logBookLabel.setPadding(new Insets(10,0,0,0));
        bottomLayout.setPadding(new Insets(10,0,0,0));
        
        // Set up the scene
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Teacher Window");
        primaryStage.show();
    }

    private void openChangePasswordWindow() {
        ChangePasswordWindow changePasswordWindow = new ChangePasswordWindow("Teacher", dbConnect);
        changePasswordWindow.start(new Stage());
        System.out.println("Opening Change Password Window");
    }

    private void openReportFaultWindow() {
    	ReportFaultWindow reportFaultWindow = new ReportFaultWindow(dbConnect, username, logBookTableView);
        reportFaultWindow.start(new Stage());
        System.out.println("Opening Report Fault Window");
    }

    private void displayHelp() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("HELP:\n\n");

        // Help for Teacher Window 
        helpMessage.append("Teacher Window:\n");
        helpMessage.append("- This window allows teachers to perform various actions.\n");
        helpMessage.append("- Change Password: Click this button to change your password.\n");
        helpMessage.append("- Report a Fault: Click this button to report a fault with a computer.\n");
        helpMessage.append("- Back: Click this button to go back.\n");
        helpMessage.append("- Help: Click this button to view help information.\n\n");

        // Help for Report Fault Window
        helpMessage.append("Report Fault Window:\n");
        helpMessage.append("- This window allows users to report faults with computers.\n");
        helpMessage.append("- Enter the Room Number, Computer Number, and describe the Fault.\n");
        helpMessage.append("- Click Report to submit the fault report.\n");
        helpMessage.append("- Click Cancel to close the window.\n\n");

        // Create an alert dialog
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText(helpMessage.toString());
        // Set styles for the dialog pane
        alert.getDialogPane().setStyle("-fx-background-color: rgb(210, 210, 210); "); // Light grey background

        // Set styles for the content area
        alert.getDialogPane().lookup(".content").setStyle("-fx-text-fill: #000000; -fx-font-size: 13px;-fx-font-weight: bold;");

        // Set styles for the buttons
        alert.getDialogPane().lookupAll(".button").forEach(node ->
            node.setStyle("-fx-background-color: #000000; -fx-text-fill: #ffffff; -fx-font-size: 12px; ")
        );
        // Show the alert dialog
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
