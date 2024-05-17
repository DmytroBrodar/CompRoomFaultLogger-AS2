import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResolveLogBookTableView {

    private DbConnect dbConnect;

    public ResolveLogBookTableView(DbConnect dbConnect) {
        this.dbConnect = dbConnect;
    }

    @SuppressWarnings("unchecked")
	public TableView<ResolveLogBookEntry> createResolveLogBookTableView() {
        TableView<ResolveLogBookEntry> resolveLogBookTable = new TableView<>();

        // Define columns
        TableColumn<ResolveLogBookEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFixed"));

        TableColumn<ResolveLogBookEntry, Integer> roomNumColumn = new TableColumn<>("Room Number");
        roomNumColumn.setCellValueFactory(new PropertyValueFactory<>("roomNum"));

        TableColumn<ResolveLogBookEntry, Integer> computerNumColumn = new TableColumn<>("Computer Number");
        computerNumColumn.setCellValueFactory(new PropertyValueFactory<>("computerNum"));

        TableColumn<ResolveLogBookEntry, String> faultColumn = new TableColumn<>("Fault");
        faultColumn.setCellValueFactory(new PropertyValueFactory<>("fault"));

        TableColumn<ResolveLogBookEntry, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<ResolveLogBookEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Add columns to table
        resolveLogBookTable.getColumns().addAll(dateColumn, roomNumColumn, computerNumColumn, faultColumn, statusColumn, nameColumn);

        // Populate table with data
        List<ResolveLogBookEntry> resolveLogBookEntries = fetchResolveLogBookEntries();
        resolveLogBookTable.getItems().addAll(resolveLogBookEntries);

        return resolveLogBookTable;
    }

    private List<ResolveLogBookEntry> fetchResolveLogBookEntries() {
        List<ResolveLogBookEntry> resolveLogBookEntries = new ArrayList<>();
        String query = "SELECT * FROM DBResolved";

        try (PreparedStatement preparedStatement = dbConnect.databaseConnection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ResolveLogBookEntry entry = new ResolveLogBookEntry(
                        resultSet.getDate("dateFixed"),
                        resultSet.getInt("roomNum"),
                        resultSet.getInt("computerNum"),
                        resultSet.getString("fault"),
                        resultSet.getString("status"),
                        resultSet.getString("name")
                );
                resolveLogBookEntries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resolveLogBookEntries;
    }

}
