import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        DbConnect dbConnect = new DbConnect();
        dbConnect.connect(); // Call the correct method name
        LoginController loginController = new LoginController(dbConnect.getDatabaseConnection());
        loginController.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
} 


