import java.io.*;
import java.sql.*;
//test commit
public class StoreSQL {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = System.out;
    private static final String STRING_CONECCION = "jdbc:sqlserver://localhost:1433;databaseName=StoreTec;user=sa;password=YourPassword;encrypt=false";

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
            String identificacion = in.readLine();
            out.print("Ingrese nombre: ");
            String nombre = in.readLine();
            out.print("Ingrese contraseña: ");
            String password = in.readLine();
            out.print("Ingrese correo electrónico: ");
            String email = in.readLine();

            // TypeUser ID for admin
            int typeUserId = 1;

            Connection conn = DriverManager.getConnection(STRING_CONECCION);
            String query = "INSERT INTO PERSONA (ID, NOMBRE, PASSWORD, EMAIL, TYPEUSERID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, identificacion);
            pstmt.setString(2, nombre);
            pstmt.setString(3, password);
            pstmt.setString(4, email);
            pstmt.setInt(5, typeUserId);

            pstmt.executeUpdate();
            out.println("Usuario registrado con éxito.");
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
