import java.sql.*;

public class test {
    private static final String STRING_CONECCION = "jdbc:sqlserver://localhost:1433;databaseName=StoreTec;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(STRING_CONECCION);
            System.out.println("Connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
