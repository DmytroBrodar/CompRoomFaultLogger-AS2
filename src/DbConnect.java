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
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      System.out.println("JDBC driver loaded");
    } catch (Exception err) 
    {
      System.err.println("Error loading JDBC driver");
      err.printStackTrace(System.err);
      System.exit(0);
    }
    databaseConnection = null;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
        String connectionUrl = "jdbc:sqlserver://sql.coccork.ie:8443;databaseName=DBComputerLog;encrypt=true;trustServerCertificate=true;user=dmytro.brodar@morrisonsislandcampus.com;password=10121991z";
      System.out.println(connectionUrl);
      databaseConnection = DriverManager.getConnection(connectionUrl);
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
