import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private Connection databaseConnection;

    public User(Connection connection) {
        this.databaseConnection = connection;
    }

    public String login(String username, String password) {
        String role = null;
        try {
            String query = "SELECT role FROM DBLogUsers WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
            resultSet.close(); 
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
