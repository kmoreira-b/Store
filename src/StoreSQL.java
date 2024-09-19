import java.io.*;
import java.sql.*;
//test commit
public class StoreSQL {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = System.out;
    private static final String STRING_CONECCION = "jdbc:sqlserver://localhost:1433;databaseName=StoreTec;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public static void main(String[] args) {
        try {
            welcomeMessage();
            int choice = getUserChoice();
            if (choice == 1) {
                login();
            } else if (choice == 2) {
                register();
            } else {
                out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void welcomeMessage() {
        out.println("Bienvenido al sistema");
        out.println("Seleccione una opción:");
        out.println("1 - Iniciar sesión");
        out.println("2 - Registrarse");
    }

    private static int getUserChoice() throws IOException {
        return Integer.parseInt(in.readLine());
    }

    private static void login() {
        out.println("Funcionalidad de inicio de sesión no implementada.");
    }

    private static void register() {
        try {
            out.print("Ingrese número de cédula: ");
            long id = Long.parseLong(in.readLine()); // Use Long for BIGINT
            out.print("Ingrese nombre: ");
            String nombre = in.readLine();
            out.print("Ingrese contraseña (número): ");
            long password = Long.parseLong(in.readLine()); // Use Long for BIGINT
            out.print("Ingrese correo electrónico: ");
            String email = in.readLine();

            // TypeUser ID for admin
            long typeUserId = 1L;

            String query = "INSERT INTO [User] (id, nombre, password, email, typeUserId) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(STRING_CONECCION);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setLong(1, id);
                pstmt.setString(2, nombre);
                pstmt.setLong(3, password);
                pstmt.setString(4, email);
                pstmt.setLong(5, typeUserId);

                pstmt.executeUpdate();
                out.println("Usuario registrado con éxito.");
            }
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }


}
