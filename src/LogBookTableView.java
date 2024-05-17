import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogBookTableView {

    private DbConnect dbConnect;
    

    public LogBookTableView(DbConnect dbConnect) {
        this.dbConnect = dbConnect;
        
    }

    @SuppressWarnings("unchecked")
	public TableView<LogBookEntry> createLogBookTableView() {
        TableView<LogBookEntry> logBookTable = new TableView<>();

        // Define columns
        TableColumn<LogBookEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateLogged"));

        TableColumn<LogBookEntry, Integer> roomNumColumn = new TableColumn<>("Room Number");
        roomNumColumn.setCellValueFactory(new PropertyValueFactory<>("roomNum"));

        TableColumn<LogBookEntry, Integer> computerNumColumn = new TableColumn<>("Computer Number");
        computerNumColumn.setCellValueFactory(new PropertyValueFactory<>("computerNum"));

        TableColumn<LogBookEntry, String> faultColumn = new TableColumn<>("Fault");
        faultColumn.setCellValueFactory(new PropertyValueFactory<>("fault"));

        TableColumn<LogBookEntry, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<LogBookEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Add columns to table
        logBookTable.getColumns().addAll(dateColumn, roomNumColumn, computerNumColumn, faultColumn, statusColumn, nameColumn);

        // Populate table with data
        List<LogBookEntry> logBookEntries = fetchLogBookEntries();
        logBookTable.getItems().addAll(logBookEntries);

        return logBookTable;
    }

    private List<LogBookEntry> fetchLogBookEntries() {
        List<LogBookEntry> logBookEntries = new ArrayList<>();
        String query = "SELECT * FROM DBLogbook";

        try (PreparedStatement preparedStatement = dbConnect.databaseConnection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LogBookEntry entry = new LogBookEntry(
                        resultSet.getDate("dateLogged"),
                        resultSet.getInt("roomNum"),
                        resultSet.getInt("computerNum"),
                        resultSet.getString("fault"),
                        resultSet.getString("status"),
                        resultSet.getString("name")
                );
                logBookEntries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logBookEntries;
    }
    
}