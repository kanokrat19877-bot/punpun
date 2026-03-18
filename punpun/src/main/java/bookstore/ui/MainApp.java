package bookstore.ui;

import bookstore.model.Book;
import bookstore.model.Inventory;
import bookstore.service.BookService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) {
        
        // โหลดฟอนต์ (ต้องอยู่ใน resources/fonts/)
        Font font = Font.loadFont(
            getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
            20
        );

        System.out.println(font); // debug ว่าโหลดได้มั้ย

        LoginPage loginPage = new LoginPage();
        Inventory inventory = new Inventory();
        

        BookService bookService = new BookService(inventory);
        
        // ✅ ต้องสร้าง Scene ก่อน
        Scene scene = loginPage.createLoginScene(stage);

        // ✅ ค่อยใส่ CSS
        scene.getStylesheets().add(
            getClass().getResource("/resources/style.css").toExternalForm()
        );

        stage.setTitle("Book Store");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}