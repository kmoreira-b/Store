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
            out.println("2 - Manejar productos");
            out.println("3 - Manejar órdenes");
            out.println("4 - Manejar proveedores");
            out.println("5 - Salir y cerrar sesión");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    manageProducts();
                    break;
                case 3:
                    manageOrders();
                    break;
                case 4:
                    manageProviders();
                    break;
                case 5:
                    out.println("Cerrando sesión...");
                    loggedIn = false;
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void manageProviders() throws IOException {
        boolean managingProviders = true;

        while (managingProviders) {
            out.println("Gestión de Proveedores:");
            out.println("1 - Crear Proveedor");
            out.println("2 - Listar Proveedores");
            out.println("3 - Actualizar Proveedor por Vendor ID");
            out.println("4 - Borrar Proveedor por Vendor ID");
            out.println("5 - Regresar al menú anterior");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createProvider();
                    break;
                case 2:
                    listProviders();
                    break;
                case 3:
                    updateProviderById();
                    break;
                case 4:
                    deleteProviderById();
                    break;
                case 5:
                    managingProviders = false; 
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void createProvider() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el nombre del proveedor: ");
            String name = in.readLine();
            out.print("Ingrese el teléfono del proveedor: ");
            String tel = in.readLine();
            out.print("Ingrese el email del proveedor: ");
            String email = in.readLine();
            out.print("Ingrese la dirección del proveedor: ");
            String address = in.readLine();

            String query = "INSERT INTO Supply (name, tel, email, address) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, tel);
                ps.setString(3, email);
                ps.setString(4, address);
                ps.executeUpdate();
                out.println("Proveedor creado con éxito.");
            }
        } catch (Exception e) {
            out.println("Error al crear el proveedor: " + e.getMessage());
        }
    }

    private static void listProviders() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM Supply";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                out.println("Proveedores registrados:");
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", Nombre: " + rs.getString("name") +
                            ", Teléfono: " + rs.getString("tel") + ", Email: " + rs.getString("email") +
                            ", Dirección: " + rs.getString("address"));
                }
            }
        } catch (Exception e) {
            out.println("Error al listar los proveedores: " + e.getMessage());
        }
    }


    private static void updateProviderById() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del proveedor a actualizar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM Supply WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("Proveedor encontrado: " + rs.getString("name"));
                    out.print("Ingrese el nuevo nombre: ");
                    String name = in.readLine();
                    out.print("Ingrese el nuevo teléfono: ");
                    String tel = in.readLine();
                    out.print("Ingrese el nuevo email: ");
                    String email = in.readLine();
                    out.print("Ingrese la nueva dirección: ");
                    String address = in.readLine();

                    String updateQuery = "UPDATE Supply SET name = ?, tel = ?, email = ?, address = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, name);
                        updateStmt.setString(2, tel);
                        updateStmt.setString(3, email);
                        updateStmt.setString(4, address);
                        updateStmt.setLong(5, id);
                        updateStmt.executeUpdate();
                        out.println("Proveedor actualizado con éxito.");
                    }
                } else {
                    out.println("Proveedor no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error al actualizar el proveedor: " + e.getMessage());
        }
    }

    private static void deleteProviderById() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del proveedor a borrar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM Supply WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM Supply WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Proveedor borrado con éxito.");
                    }
                } else {
                    out.println("Proveedor no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error al borrar el proveedor: " + e.getMessage());
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

    private static void manageProducts() throws IOException {
        boolean managingProducts = true;

        while (managingProducts) {
            out.println("Gestión de Productos:");
            out.println("1 - Crear Producto");
            out.println("2 - Listar Productos");
            out.println("3 - Actualizar Producto");
            out.println("4 - Eliminar Producto");
            out.println("5 - Agregar productos a favoritos");
            out.println("6 - Listar productos favoritos");
            out.println("7 - Eliminar producto de favoritos");
            out.println("8 - Manejar Categorías");
            out.println("9 - Regresar al menú anterior");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createProducts();
                    break;
                case 2:
                    listProducts();
                    break;
                case 3:
                    updateProducts();
                    break;
                case 4:
                    deleteProducts();
                    break;
                case 5:
                    wishlist();
                    break;
                case 6:
                    listarWishlist();
                    break;
                case 7:
                    deleteWishlist();
                    break;
                case 8:
                    manageCategories();
                    break;
                case 9:
                    managingProducts = false;
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void manageCategories() throws IOException {
        boolean managingCategories = true;

        while (managingCategories) {
            out.println("Gestión de Categorías:");
            out.println("1 - Crear Categoría");
            out.println("2 - Listar Categorías");
            out.println("3 - Actualizar Categoría por ID");
            out.println("4 - Borrar Categoría por ID");
            out.println("5 - Regresar al menú anterior");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    listCategories();
                    break;
                case 3:
                    updateCategoryById();
                    break;
                case 4:
                    deleteCategoryById();
                    break;
                case 5:
                    managingCategories = false;
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void createCategory() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el nombre de la categoría: ");
            String name = in.readLine();
            out.print("Ingrese la descripción de la categoría: ");
            String description = in.readLine();

            String query = "INSERT INTO categoryProduct (name, description) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, description);
                ps.executeUpdate();
                out.println("Categoría creada con éxito.");
            }
        } catch (Exception e) {
            out.println("Error al crear la categoría: " + e.getMessage());
        }
    }

    private static void listCategories() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM categoryProduct";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                out.println("Categorías registradas:");
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", Nombre: " + rs.getString("name") +
                            ", Descripción: " + rs.getString("description"));
                }
            }
        } catch (Exception e) {
            out.println("Error al listar las categorías: " + e.getMessage());
        }
    }

    private static void updateCategoryById() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID de la categoría a actualizar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM categoryProduct WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("Categoría encontrada: " + rs.getString("name"));
                    out.print("Ingrese el nuevo nombre: ");
                    String name = in.readLine();
                    out.print("Ingrese la nueva descripción: ");
                    String description = in.readLine();

                    String updateQuery = "UPDATE categoryProduct SET name = ?, description = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, name);
                        updateStmt.setString(2, description);
                        updateStmt.setLong(3, id);
                        updateStmt.executeUpdate();
                        out.println("Categoría actualizada con éxito.");
                    }
                } else {
                    out.println("Categoría no encontrada.");
                }
            }
        } catch (Exception e) {
            out.println("Error al actualizar la categoría: " + e.getMessage());
        }
    }


    private static void deleteCategoryById() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID de la categoría a borrar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM categoryProduct WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM categoryProduct WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Categoría borrada con éxito.");
                    }
                } else {
                    out.println("Categoría no encontrada.");
                }
            }
        } catch (Exception e) {
            out.println("Error al borrar la categoría: " + e.getMessage());
        }
    }


    private static void createProducts() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el nombre: ");
            String name = in.readLine();
            out.print("Ingrese una descripcion: ");
            String description = in.readLine();
            out.print("Ingrese el precio del producto: ");
            double price = Double.parseDouble(in.readLine());
            out.print("Ingrese el numero del color del producto: ");
            long color = Long.parseLong(in.readLine());
            out.print("Ingrese el numero de talla del producto: ");
            long size = Long.parseLong(in.readLine());
            out.print("Ingrese el material del producto: ");
            String material = in.readLine();
            out.print("Ingrese el numero de categoria del producto: ");
            long category = Long.parseLong(in.readLine());
            out.print("Ingrese la cantidad deseada de productos: ");
            long quantity = Long.parseLong(in.readLine());
            out.print("Ingrese el Supply del producto: ");
            long supplierid = Long.parseLong(in.readLine());

            String storedProcedure = "{call CreateProductInventory(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {

                callableStatement.setString(1, name);
                callableStatement.setString(2, description);
                callableStatement.setBigDecimal(3, new java.math.BigDecimal(price));
                callableStatement.setLong(4, color);
                callableStatement.setLong(5, size);
                callableStatement.setString(6, material);
                callableStatement.setLong(7, category);
                callableStatement.setLong(8, quantity);
                callableStatement.setLong(9, supplierid);

                boolean hasResults = callableStatement.execute();

                if (hasResults == false) {
                    out.println("El producto se creo y se agrego al inventario!!!");
                }
            }
        } catch (Exception e) {
            out.println("Error creando producto: " + e.getMessage());
        }
    }

    private static void listProducts() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM [Product]";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", Nombre: " + rs.getString("name") + ", Descripcion: " + rs.getString("descripcion") + ", Precio: " + rs.getDouble("price") + ", Color: " + rs.getLong("color") + ", Size: " + rs.getLong("size") + ", Material: " + rs.getString("material") + ", Caantidad: " + rs.getLong("quantity"));
                }
            }
        } catch (Exception e) {
            out.println("Error listando productos: " + e.getMessage());
        }
    }

    private static void updateProducts() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del producto: ");
            long id = Long.parseLong(in.readLine());
            out.print("Ingrese el nombre: ");
            String name = in.readLine();
            out.print("Ingrese una descripcion: ");
            String description = in.readLine();
            out.print("Ingrese el precio del producto: ");
            double price = Double.parseDouble(in.readLine());
            out.print("Ingrese el numero del color del producto: ");
            long color = Long.parseLong(in.readLine());
            out.print("Ingrese el numero de talla del producto: ");
            long size = Long.parseLong(in.readLine());
            out.print("Ingrese el material del producto: ");
            String material = in.readLine();
            out.print("Ingrese el numero de categoria del producto: ");
            long category = Long.parseLong(in.readLine());
            out.print("Ingrese la cantidad deseada de productos: ");
            long quantity = Long.parseLong(in.readLine());
            out.print("Ingrese el Supply del producto: ");
            long supplierid = Long.parseLong(in.readLine());

            String storedProcedure = "{call UpdateProduct(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {

                callableStatement.setLong(1, id);
                callableStatement.setString(2, name);
                callableStatement.setString(3, description);
                callableStatement.setBigDecimal(4, new java.math.BigDecimal(price));
                callableStatement.setLong(5, color);
                callableStatement.setLong(6, size);
                callableStatement.setString(7, material);
                callableStatement.setLong(8, category);
                callableStatement.setLong(9, quantity);
                callableStatement.setLong(10, supplierid);

                boolean hasResults = callableStatement.execute();

                if (hasResults == false) {
                    out.println("El producto se ha actualizado correctamente!!!");
                }
            }
        } catch (Exception e) {
            out.println("Error actualizando el producto: " + e.getMessage());
        }
    }

    private static void deleteProducts() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del usuario a eliminar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM [Product] WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM [Product] WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Producto eliminado con éxito.");
                    }
                } else {
                    out.println("Producto no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error eliminando producto: " + e.getMessage());
        }
    }

    private static void wishlist(){
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del usuario: ");
            long userid = Long.parseLong(in.readLine());
            out.print("Ingrese el ID del producto: ");
            long productid = Long.parseLong(in.readLine());


            String storedProcedure = "{call CreateWishProduct(?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {

                callableStatement.setLong(1, userid);
                callableStatement.setLong(2, productid);


                boolean hasResults = callableStatement.execute();

                if (hasResults == false) {
                    out.println("El producto se agrego a su WishList!!!");
                }
            }
        } catch (Exception e) {
            out.println("Error creando Wishlist: " + e.getMessage());
        }
    }

    private static void listarWishlist() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM [Wishlist]";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", ID del usuario: " + rs.getLong("userid") + ", ID del producto: " + rs.getLong("productid") + ", Fecha: " + rs.getDate("dateAdded"));
                }
            }
        } catch (Exception e) {
            out.println("Error listando productos: " + e.getMessage());
        }
    }

    private static void deleteWishlist() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del producto a eliminar de favoritos: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM [Wishlist] WHERE productid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM [Wishlist] WHERE productid = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Producto eliminado con éxito.");
                    }
                } else {
                    out.println("Producto no encontrado.");
                }
            }
        } catch (Exception e) {
            out.println("Error eliminando producto: " + e.getMessage());
        }
    }

    private static void manageOrders() throws IOException {
        boolean managingOrders = true;

        while (managingOrders) {
            out.println("Gestión de Ordenes:");
            out.println("1 - Crear Orden");
            out.println("2 - Listar Ordenes");
            out.println("3 - Actualizar Orden");
            out.println("4 - Eliminar Orden");
            out.println("5 - Regresar al menú anterior");

            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    listOrders();
                    break;
                case 3:
                    updateOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    managingOrders = false;
                    break;
                default:
                    out.println("Opción no válida.");
                    break;
            }
        }
    }

    private static void createOrder() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID del usuario: ");
            long userid = Long.parseLong(in.readLine());
            out.print("Ingrese el ID del producto: ");
            long productid = Long.parseLong(in.readLine());
            out.print("Ingrese la cantidad a pedir: ");
            long amount = Long.parseLong(in.readLine());
            out.print("Ingrese el precio unitario del producto: ");
            long unitprice = Long.parseLong(in.readLine());
            out.print("Ingrese el estado de la orden: (pending, sent, delivered): ");
            String state = in.readLine();

            String storedProcedure = "{call CreateOrder(?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {

                callableStatement.setLong(1, userid);
                callableStatement.setLong(2, amount);
                callableStatement.setString(3, state);
                callableStatement.setLong(4, productid);
                callableStatement.setLong(5, unitprice);


                boolean hasResults = callableStatement.execute();

                if (hasResults == false) {
                    out.println("La orden se creo y se agrego a detalles de orden!!!");
                }
            }
        } catch (Exception e) {
            out.println("Error creando orden: " + e.getMessage());
        }
    }

    private static void listOrders() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            String query = "SELECT * FROM [Orders]";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    out.println("ID: " + rs.getLong("id") + ", ID Usuario: " + rs.getLong("userId") + ", Fecha: " + rs.getDate("date") + ", Cantidad: " + rs.getInt("amount") + ", Estado: " + rs.getString("state"));
                }
            }
        } catch (Exception e) {
            out.println("Error listando ordenes: " + e.getMessage());
        }
    }

    private static void updateOrder() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID de la orden: ");
            long id = Long.parseLong(in.readLine());
            out.print("Ingrese el ID del usuario: ");
            long userid = Long.parseLong(in.readLine());
            out.print("Ingrese la cantidad de productos: ");
            long amount = Long.parseLong(in.readLine());
            out.print("Ingrese el estado de la orden: (pending, sent, delivered): ");
            String state = in.readLine();
            out.print("Ingrese el ID del producto: ");
            long productid = Long.parseLong(in.readLine());
            out.print("Ingrese el precio unitario del producto: ");
            long unitprice = Long.parseLong(in.readLine());

            String storedProcedure = "{call UpdateOrder(?, ?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(storedProcedure)) {

                callableStatement.setLong(1, id);
                callableStatement.setLong(2, userid);
                callableStatement.setLong(3, amount);
                callableStatement.setString(4, state);
                callableStatement.setLong(5, productid);
                callableStatement.setLong(6, unitprice);

                boolean hasResults = callableStatement.execute();

                if (hasResults == false) {
                    out.println("La orden se ha actualizado correctamente!!!");
                }
            }
        } catch (Exception e) {
            out.println("Error actualizando la orden: " + e.getMessage());
        }
    }

    private static void deleteOrder() {

        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING)) {
            out.print("Ingrese el ID de la orden a eliminar: ");
            long id = Long.parseLong(in.readLine());

            String query = "SELECT * FROM [Orders] WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String deleteQuery = "DELETE FROM [Orders] WHERE id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setLong(1, id);
                        deleteStmt.executeUpdate();
                        out.println("Orden eliminada con éxito.");
                    }
                } else {
                    out.println("Orden no encontrada.");
                }
            }
        } catch (Exception e) {
            out.println("Error eliminando orden: " + e.getMessage());
        }
    }
}