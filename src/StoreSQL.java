import java.io.*;
import java.sql.*;

public class StoreSQL {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream out = System.out;
    private static final String CONNECTION_STRING = "jdbc:sqlserver://localhost:1433;databaseName=StoreTec;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";

    public static void main(String[] args) {
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                showWelcomeMenu();
                int choice = getUserChoice();
                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        out.println("Saliendo del sistema...");
                        keepRunning = false;
                        break;
                    default:
                        out.println("Opción no válida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void showWelcomeMenu() {
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
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese número de cédula: ");
            long id = Long.parseLong(in.readLine());
            out.print("Ingrese la contraseña: ");
            String password = in.readLine();

            String query = "SELECT * FROM [User] WHERE id = ? AND password = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("Login exitoso. Bienvenido, " + rs.getString("nombre"));
                    showPostLoginMenu(id);
                } else {
                    out.println("Usuario o contraseña incorrectos.");
                    return; // Go back to the main menu
                }
            }
        } catch (Exception e) {
            out.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }

    private static void showPostLoginMenu(long userId) throws IOException {
        boolean loggedIn = true;

        while (loggedIn) {
            out.println("¿Qué desea hacer?");
            out.println("1 - Manejar usuarios");
            out.println("2 - Realizar compra (No implementado)");
            out.println("3 - Salir y cerrar sesión");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    out.println("Esta opción aún no está implementada.");
                    break;
                case 3:
                    out.println("Cerrando sesión...");
                    loggedIn = false; // Exit back to main menu
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void manageUsers() throws IOException {
        boolean managingUsers = true;

        while (managingUsers) {
            out.println("Gestión de Usuarios:");
            out.println("1 - Crear Usuario");
            out.println("2 - Listar Usuarios");
            out.println("3 - Actualizar Usuario");
            out.println("4 - Eliminar Usuario");
            out.println("5 - Regresar al menú anterior");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    listUsers();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    managingUsers = false;
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void createUser() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del nuevo usuario: ");
            long id = Long.parseLong(in.readLine());
            out.print("Ingrese el nombre: ");
            String nombre = in.readLine();
            out.print("Ingrese la contraseña: ");
            String password = in.readLine();
            out.print("Ingrese el email: ");
            String email = in.readLine();
            out.print("Ingrese el tipo de usuario (1 = Normal, 2 = Admin): ");
            long typeUserId = Long.parseLong(in.readLine());

            String query = "INSERT INTO [User] (id, nombre, password, email, typeUserId) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ps.setString(2, nombre);
                ps.setString(3, password);
                ps.setString(4, email);
                ps.setLong(5, typeUserId);
                ps.executeUpdate();
                out.println("Usuario creado con éxito.");
            }
        } catch (Exception e) {
            out.println("Error creando usuario: " + e.getMessage());
        }
    }

    private static void listUsers() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM [User]";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", Nombre: " + rs.getString("nombre") + ", Email: " + rs.getString("email") + ", Tipo de Usuario: " + rs.getLong("typeUserId"));
                }
            }
        } catch (Exception e) {
            out.println("Error listando usuarios: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del usuario a actualizar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM [User] WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("Usuario encontrado: " + rs.getString("nombre"));
                    out.print("Ingrese el nuevo nombre: ");
                    String nombre = in.readLine();
                    out.print("Ingrese la nueva contraseña: ");
                    String password = in.readLine();
                    out.print("Ingrese el nuevo email: ");
                    String email = in.readLine();
                    out.print("Ingrese el nuevo tipo de usuario (1 = Normal, 2 = Admin): ");
                    long typeUserId = Long.parseLong(in.readLine());

                    String updateQuery = "UPDATE [User] SET nombre = ?, password = ?, email = ?, typeUserId = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, nombre);
                        updateStmt.setString(2, password);
                        updateStmt.setString(3, email);
                        updateStmt.setLong(4, typeUserId);
                        updateStmt.setLong(5, id);
                        updateStmt.executeUpdate();
                        out.println("Usuario actualizado con éxito.");
                    }
                } else {
                    out.println("Usuario no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error actualizando usuario: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del usuario a eliminar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM [User] WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM [User] WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Usuario eliminado con éxito.");
                    }
                } else {
                    out.println("Usuario no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error eliminando usuario: " + e.getMessage());
        }
    }

    private static void register() {
        createUser();
    }
}
