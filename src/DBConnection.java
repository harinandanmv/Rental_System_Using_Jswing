import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    db_url=jdbc:oracle:thin:@localhost:1521:orcl
    db_user=system
    db_password=your_password_here
 
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(db_url, db_user, db_password);
        }
        return connection;
    }
}
