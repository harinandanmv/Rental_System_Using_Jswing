import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String db_url = "jdbc:oracle:thin:@localhost:1521:orcl"; 
    private static final String db_user = "system"; 
    private static final String db_password = "Hari123#"; 
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(db_url, db_user, db_password);
        }
        return connection;
    }
}
