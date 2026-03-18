package bookstore.ui;

import bookstore.model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LoginPage {

    private List<User> users = new ArrayList<>();

    public LoginPage() {

        Inventory inventory = new Inventory();

        users.add(new Admin(
                "1",
                "Admin",
                "admin@store.com",
                "1234",
                inventory
        ));

        users.add(new Customer(
                "2",
                "User",
                "user@store.com",
                "1234",
                new Cart(),
                new ArrayList<>()
        ));
    }

    public Scene createLoginScene(Stage stage){

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label title = new Label("BOOK STORE LOGIN");
        title.setStyle("-fx-font-size:20px; -fx-font-weight:bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Label message = new Label();

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Create Customer Account");

        loginBtn.setPrefWidth(200);
        registerBtn.setPrefWidth(200);

        // ================= LOGIN =================
        loginBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if(email.isEmpty() || password.isEmpty()){
                message.setText("Please fill all fields");
                return;
            }

            for(User user : users){

                if(user.login(email, password)){

                    // ✅ เก็บ session
                    UserSession.setUser(user);

                    
                    StorePage storePage = new StorePage();
                    stage.setScene(storePage.createStoreScene(stage));

                    return;
                }
            }

            message.setText("Login failed");
        });

        // ================= REGISTER (CUSTOMER ONLY) =================
        registerBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if(email.isEmpty() || password.isEmpty()){
                message.setText("Please fill email and password");
                return;
            }

            // ❌ กัน email ซ้ำ
            for(User u : users){
                if(u.getEmail().equals(email)){
                    message.setText("Email already exists");
                    return;
                }
            }

            Customer newCustomer = new Customer(
                    users.size() + "1",
                    "Customer" + users.size(),
                    email,
                    password,
                    new Cart(),
                    new ArrayList<>()
            );

            users.add(newCustomer);

            message.setText("Customer account created ✔");
        });

        root.getChildren().addAll(
                title,
                emailField,
                passField,
                loginBtn,
                registerBtn,
                message
        );

        return new Scene(root,400,350);
    }
}