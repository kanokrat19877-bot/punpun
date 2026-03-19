package bookstore.ui;

import bookstore.model.*;
import bookstore.service.BookService;
import bookstore.service.RepoerService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NavBar {

    public HBox createNavBar(Stage stage, BookService bookService, Consumer<List<Book>> onSearch) {

        HBox navBar = new HBox(20);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color:#1f2733;");

        Label logo = new Label("PUNPUN HORROR BOOK STORE");
        logo.setStyle("-fx-text-fill:white; -fx-font-size:16px; -fx-font-weight:bold;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search book...");
        searchBar.setPrefWidth(350);

        Button searchBtn = new Button("Search");
        searchBar.setOnAction(e -> searchBtn.fire());

        searchBtn.setOnAction(e -> {
            String keyword = searchBar.getText();
            List<Book> result = bookService.searchByTitle(keyword);
            onSearch.accept(result);
        });

        Button cartBtn = new Button("Cart");
        cartBtn.setOnAction(e -> {
            Chart chart = new Chart(); // ตรวจสอบว่าคลาสคุณสะกด Chart หรือ Cart นะครับ
            stage.setScene(chart.createScene(stage));
        });

        Button accountBtn = new Button("Account"); // เปลี่ยนชื่อจาก loginBtn เป็น accountBtn ให้สื่อความหมาย
        accountBtn.setStyle("-fx-background-color:#d8cbb2;");
        
        // ✅ ปรับปรุงปุ่ม Account ให้แยกหน้าตามประเภท User
        accountBtn.setOnAction(e -> {
            User currentUser = UserSession.getUser();

            if (currentUser instanceof Admin) {
                // ถ้าเป็น Admin ไปหน้าจัดการ Dashboard
                List<Order> orders = new ArrayList<>(); 
                RepoerService reportService = new RepoerService(orders);
                AccountPage adminPage = new AccountPage(bookService, reportService);
                stage.setScene(adminPage.createScene(stage));
                
            } else if (currentUser instanceof Customer) {
                // ✅ ถ้าเป็น Customer ให้ไปหน้าโปรไฟล์เฉพาะของลูกค้า
                CustomerAccountPage customerPage = new CustomerAccountPage();
                stage.setScene(customerPage.createSceneAccCustomer(stage));
            }
        });
        
        HBox rightMenu = new HBox(10, cartBtn, accountBtn);
        rightMenu.setAlignment(Pos.CENTER_RIGHT);

        navBar.getChildren().addAll(logo, searchBar, searchBtn, rightMenu);
        return navBar;
    }
}