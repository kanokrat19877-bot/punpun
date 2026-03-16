package bookstore.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        LoginPage loginPage = new LoginPage();

        stage.setTitle("Book Store");
        stage.setScene(loginPage.createLoginScene(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}