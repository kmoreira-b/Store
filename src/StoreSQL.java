import java.io.*;
import java.sql.*;

public class StoreSQL {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = System.out;
    private static final String STRING_CONECCION = "jdbc:sqlserver://localhost:1433;databaseName=StoreTec;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public static void main(String[] args) {
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                welcomeMessage();
                int choice = getUserChoice();
                if (choice == 1) {
                    login();
                } else if (choice == 2) {
                    register();
                } else if (choice == 3) {
                    out.println("Saliendo del sistema...");
                    keepRunning = false;
                } else {
                    out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void welcomeMessage() {
        out.println("Bienvenido al sistema");
        out.println("Seleccione una opción:");
        out.println("1 - Iniciar sesión");
        out.println("2 - Registrarse");
        out.println("3 - Salir");
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
            long id = Long.parseLong(in.readLine());
            out.print("Ingrese nombre: ");
            String nombre = in.readLine();
            out.print("Ingrese contraseña: ");
            String password = in.readLine();
            out.print("Ingrese correo electrónico: ");
            String email = in.readLine();


            long typeUserId = 1L;

            String query = "INSERT INTO [User] (id, nombre, password, email, typeUserId) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(STRING_CONECCION);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setLong(1, id);
                pstmt.setString(2, nombre);
                pstmt.setString(3, password); // Insert password as string
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
