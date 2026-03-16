package bookstore.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage {

    public Scene createLoginScene(Stage stage) {

        Label title = new Label("Book Store Login");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        Label message = new Label();
        loginButton.setOnAction(e -> {

            String username = usernameField.getText();
            String password = passwordField.getText();
            if(username.equals("admin") && password.equals("1234")) {
                message.setText("Login Success");
                StorePage storePage = new StorePage();
                stage.setScene(storePage.createStoreScene(stage));
            } else {
                message.setText("Invalid username or password");
            }
        });
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
                title,
                usernameField,
                passwordField,
                loginButton,
                message
        );

        return new Scene(root, 400, 300);
    }
}
