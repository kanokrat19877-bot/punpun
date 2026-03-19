package bookstore.ui;

import bookstore.model.Book;
import bookstore.model.User;
import bookstore.model.UserSession;
import bookstore.service.BookService;
import bookstore.service.RepoerService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class AccountPage {
    
    private BookService bookService;
    private RepoerService reportService;
    private GridPane bookGrid; 

    // กำหนด Path เป็นค่าคงที่เพื่อป้องกันการพิมพ์ผิดหลายจุด
    // หากรันใน IDE (IntelliJ/Eclipse) ให้ใช้ path นี้
    private final String STOCK_FILE_PATH = "src/main/java/resources/Stock.txt";

    public AccountPage() {
    	
    }
    
    public AccountPage(BookService bookService, RepoerService reportService){
        this.bookService = bookService;
        this.reportService = reportService;
        this.bookGrid = new GridPane();
    }

	private void loadBooks(GridPane grid, List<Book> books) {
        grid.getChildren().clear();
        if (books == null || books.isEmpty()) return;
        int col = 0; int row = 0;
        for (Book b : books) {
            CardBook card = new CardBook(b);
            grid.add(card, col, row);
            col++;
            if (col == 3) { col = 0; row++; }
        }
    }

    public Scene createScene(Stage stage){
    	Font font = Font.loadFont(
                getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
                20
            );
    	System.out.println(font);
    	
        User user = UserSession.getUser();
        BorderPane root = new BorderPane();

        HBox navBar = new HBox(20);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color:#1f2733;");
        Label logo = new Label("PUNPUN HORROR BOOK STORE");
        logo.setStyle("-fx-text-fill:white; -fx-font-size:16px; -fx-font-weight:bold;");
        navBar.getChildren().add(logo);
        root.setTop(navBar);

        VBox mainContainer = new VBox(25);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color:#eaeaea;");

        Button backBtn = new Button("< Back to Store");
        backBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            StorePage store = new StorePage();
            stage.setScene(store.createStoreScene(stage));
        });
        mainContainer.getChildren().add(backBtn);

        HBox bodyLayout = new HBox(30);
        VBox leftSide = new VBox(20);
        leftSide.setMinWidth(250);
        leftSide.setAlignment(Pos.TOP_CENTER);
        
        CardProflie profile = new CardProflie(user);
        
        Button editStockBtn = new Button("Edit Stock");
        editStockBtn.setMaxWidth(Double.MAX_VALUE);
        editStockBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        editStockBtn.setOnAction(e -> openAndRefreshStock());

        Button refreshBtn = new Button("🔄 Refresh Data");
        refreshBtn.setMaxWidth(Double.MAX_VALUE);
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshBtn.setOnAction(e -> reloadDataFromTxt());

        Button logoutBtn = new Button("🚪 Log out");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> stage.close());

        VBox menuBox = new VBox(15, editStockBtn, refreshBtn, logoutBtn);
        menuBox.setPadding(new Insets(10, 0, 0, 0));
        leftSide.getChildren().addAll(profile, menuBox);

        VBox rightSide = new VBox(30);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        CardDashboard dashboard = new CardDashboard(bookService, reportService);
        Label title = new Label("📦 รายการหนังสือปัจจุบัน");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        bookGrid.setHgap(20); bookGrid.setVgap(20);
        loadBooks(bookGrid, bookService.searchByTitle(""));

        rightSide.getChildren().addAll(dashboard, title, bookGrid);
        bodyLayout.getChildren().addAll(leftSide, rightSide);
        mainContainer.getChildren().add(bodyLayout);

        ScrollPane scroll = new ScrollPane(mainContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:transparent; -fx-background: #eaeaea;");
        root.setCenter(scroll);
        root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );
        
        return new Scene(root, 1000, 600);
    }

    private void openAndRefreshStock() {
        try {
            File file = new File(STOCK_FILE_PATH);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("เปิดไฟล์สำเร็จ! กรุณาแก้ไขข้อมูล บันทึกไฟล์ และกดปุ่ม Refresh \n"
                		+ "ให้กรอก ชื่อเรื่อง, ผู้แต่ง, หมวดหมู่, ราคา, สต็อก, เรตติ้ง, จำนวนที่ขายได้");
                alert.showAndWait();
            } else {
                // ถ้าหาไม่เจอ ให้แจ้งเตือนว่าหาที่ไหนไม่เจอ
                showErrorAlert("ไม่พบไฟล์!", "ไม่พบไฟล์ที่: " + file.getAbsolutePath() + "\nกรุณาตรวจสอบว่ามีไฟล์ Stock.txt อยู่ในโฟลเดอร์ resources หรือไม่");
            }
        } catch (Exception e) {
            showErrorAlert("Error", "ไม่สามารถเปิดไฟล์ได้: " + e.getMessage());
        }
    }

    private void reloadDataFromTxt() {
        File file = new File(STOCK_FILE_PATH);
        if (!file.exists()) {
            showErrorAlert("Error", "หาไฟล์ไม่เจอ: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            
            // bookService.clearBooks(); 

            int lastId = 0; 
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 8) {
                    lastId++;
                    String title = data[0].trim();
                    String author = data[1].trim();
                    String category = data[2].trim();
                    double price = Double.parseDouble(data[3].trim());
                    int stock = Integer.parseInt(data[4].trim());
                    double rating = Double.parseDouble(data[5].trim());
                    int soldOut = Integer.parseInt(data[6].trim());
                    String imgPath = data[7].trim();

                    Book newBook = new Book(lastId, title, author, category, price, rating, stock, soldOut, imgPath);
                    bookService.addBook(newBook);
                    count++;
                }
            }
            loadBooks(bookGrid, bookService.searchByTitle(""));
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Sync Complete");
            alert.setContentText("อัปเดตข้อมูลสำเร็จ " + count + " เล่ม");
            alert.showAndWait();

        } catch (Exception e) {
            showErrorAlert("Error", "ข้อมูลในไฟล์อาจจะผิดรูปแบบ: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}