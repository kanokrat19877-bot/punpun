package bookstore.ui;

import bookstore.model.*;
import bookstore.service.BookService;
import bookstore.service.RepoerService; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginPage {

    private List<User> users = new ArrayList<>();
    private final String USER_FILE_PATH = "src/main/java/resources/User.txt";
    private final String STOCK_FILE_PATH = "src/main/java/resources/Stock.txt";
    private final String ADMIN_CODE = "ADMIN123"; 

    public LoginPage() {
        loadUsersFromFile();
    }

    public Scene createLoginScene(Stage stage) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f4f4;");

        Label title = new Label("HORROR STORE LOGIN");
        title.setStyle("-fx-font-size:24px; -fx-font-weight:bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(250);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(250);

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Customer", "Admin");
        roleCombo.setValue("Customer");
        roleCombo.setPrefWidth(250);

        PasswordField adminCodeField = new PasswordField();
        adminCodeField.setPromptText("Enter Admin Secret Code");
        adminCodeField.setMaxWidth(250);
        adminCodeField.setVisible(false); 

        roleCombo.setOnAction(e -> adminCodeField.setVisible(roleCombo.getValue().equals("Admin")));

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Create Account");
        loginBtn.setPrefWidth(250);
        registerBtn.setPrefWidth(250);

        // ================= LOGIN LOGIC =================
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                message.setText("Please enter email and password");
                return;
            }

            users.clear(); 
            loadUsersFromFile(); 

            for (User user : users) {
                if (user != null && user.getEmail() != null && user.login(email, password)) {
                    UserSession.setUser(user);
                    
                    if (user instanceof Admin) {
                        Inventory inventory = new Inventory();
                        loadStockIntoInventory(inventory);
                        BookService bookService = new BookService(inventory);
                        RepoerService reportService = new RepoerService(new ArrayList<>()); 
                        
                        AccountPage adminPage = new AccountPage(bookService, reportService); 
                        stage.setScene(adminPage.createScene(stage));
                    } else {
                        // เปลี่ยนไปหน้า Store สำหรับ Customer
                        StorePage storePage = new StorePage();
                        stage.setScene(storePage.createStoreScene(stage));
                    }
                    return;
                }
            }
            message.setText("Invalid email or password!");
        });

        // ================= REGISTER LOGIC =================
        registerBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();
            String role = roleCombo.getValue();

            if (email.isEmpty() || password.isEmpty()) {
                message.setText("Please fill email and password");
                return;
            }

            if (users.stream().anyMatch(u -> email.equals(u.getEmail()))) {
                message.setText("Email already exists!");
                return;
            }

            User newUser;
            if (role.equals("Admin")) {
                if (!adminCodeField.getText().equals(ADMIN_CODE)) {
                    message.setText("Wrong Admin Secret Code!");
                    return;
                }
                newUser = new Admin("A" + (users.size() + 1), "Admin_" + (users.size() + 1), email, password);
            } else {
                // สร้าง Customer พร้อม List ประวัติการซื้อที่ว่างเปล่า
                newUser = new Customer("C" + (users.size() + 1), "User_" + (users.size() + 1), email, password, new Cart(), new ArrayList<>());
            }

            saveUserToFile(newUser, role.toUpperCase());
            users.add(newUser); 
            message.setStyle("-fx-text-fill: green;");
            message.setText(role + " account created!");
        });

        root.getChildren().addAll(title, emailField, passField, roleCombo, adminCodeField, loginBtn, registerBtn, message);
        return new Scene(root, 400, 500);
    }

    private void saveUserToFile(User user, String role) {
        File file = new File(USER_FILE_PATH);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.println(role + "," + user.getUserId() + "," + user.getName() + "," + user.getEmail() + "," + user.getPassword());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadUsersFromFile() {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 5) continue; 
                
                String role = data[0].trim();
                String id = data[1].trim();
                String name = data[2].trim();
                String email = data[3].trim();
                String pass = data[4].trim();

                if (role.equalsIgnoreCase("ADMIN")) {
                    users.add(new Admin(id, name, email, pass));
                } else {
                    // โหลดกลับมาเป็น Customer โดยส่งค่าให้ครบตาม Constructor
                    users.add(new Customer(id, name, email, pass, new Cart(), new ArrayList<>()));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadStockIntoInventory(Inventory inventory) {
        File file = new File(STOCK_FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lastId = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 7) {
                    lastId++;
                    // ✅ แก้ไข: เพิ่ม parameter ตัวที่ 9 (imagePath) ถ้าไม่มีให้ส่ง "null"
                    String imgPath = (data.length >= 8) ? data[7].trim() : "null";
                    inventory.addBook(new Book(
                             lastId, data[0].trim(), data[1].trim(), data[2].trim(), 
                             Double.parseDouble(data[3].trim()), Double.parseDouble(data[5].trim()), 
                             Integer.parseInt(data[4].trim()), Integer.parseInt(data[6].trim()), imgPath));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}