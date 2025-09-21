import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect{
  public Connection databaseConnection;
  public void connect()
  {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("JDBC driver loaded");
    } catch (Exception err) 
    {
      System.err.println("Error loading JDBC driver");
      err.printStackTrace(System.err);
      System.exit(0);
    }
    databaseConnection = null;
    try {
        String connectionUrl = "jdbc:mysql://localhost:3306/computer_fault_logger";
            String username = "root";
            String password = "your_password";
      System.out.println(connectionUrl);
      databaseConnection = DriverManager.getConnection(connectionUrl, username, password);
      System.out.println("Connected to the database");
            // Register a shutdown hook to close the connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (databaseConnection != null) {
                    try {
                      databaseConnection.close();
                      System.out.println("Connection has been closed. (Using ShutdownHook)");
                    } catch (SQLException e) {
                        e.printStackTrace(); // Log or handle the exception appropriately
                    }
                }
            })); 
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public Connection getDatabaseConnection() {
	  return databaseConnection;
  }
}

