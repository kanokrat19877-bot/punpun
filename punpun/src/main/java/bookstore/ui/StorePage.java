package bookstore.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import bookstore.model.Book;
import bookstore.model.Inventory;
import bookstore.service.BookService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StorePage {

    private void loadBooks(GridPane grid, List<Book> books, Label noBookLabel) {
    	
    	Font font = Font.loadFont(
                getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
                20
            );
    	
        grid.getChildren().clear();
        if (books == null || books.isEmpty()) {
            noBookLabel.setVisible(true);
            return;
        }
        noBookLabel.setVisible(false);

        int col = 0;
        int row = 0;
        for (Book b : books) {
            CardBook card = new CardBook(b);
            grid.add(card, col, row);
            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    private List<Book> loadBooksFromFile() {
        List<Book> bookList = new ArrayList<>();
        // อ้างอิง Path ตามรูปโครงสร้างโปรเจกต์: src/main/resources/Stock.txt
        File file = new File("src/main/java/resources/Stock.txt");
        
        if (!file.exists()) {
            System.out.println("⚠️ Warning: Stock.txt not found at " + file.getAbsolutePath());
            return bookList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lastId = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 7) {
                    lastId++;
                    bookList.add(new Book(lastId, data[0].trim(), data[1].trim(), data[2].trim(), 
                                 Double.parseDouble(data[3].trim()), Double.parseDouble(data[5].trim()), 
                                 Integer.parseInt(data[4].trim()), Integer.parseInt(data[6].trim())));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
        return bookList;
    }

    public Scene createStoreScene(Stage stage) {
        GridPane bookGrid = new GridPane();
        bookGrid.setHgap(20); bookGrid.setVgap(20);
        bookGrid.setPadding(new Insets(20));

        Label noBookLabel = new Label("ไม่พบหนังสือใน stock");
        noBookLabel.setStyle("-fx-font-size:18px;");
        noBookLabel.setVisible(false);

        ScrollPane scrollPane = new ScrollPane(bookGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent; -fx-background: #f4f4f4;");

        StackPane centerPane = new StackPane(scrollPane, noBookLabel);
        
        List<Book> booksFromFile = loadBooksFromFile();
        Inventory inventory = new Inventory();
        booksFromFile.forEach(inventory::addBook);
        BookService bookService = new BookService(inventory);

        loadBooks(bookGrid, booksFromFile, noBookLabel);

        BorderPane root = new BorderPane();
        root.setCenter(centerPane);

        // --- SIDEBAR CUSTOM UI (ตามรูปภาพ) ---
        VBox sidebarContainer = new VBox(15);
        sidebarContainer.setPadding(new Insets(25));
        sidebarContainer.setPrefWidth(260);
        sidebarContainer.setStyle("-fx-background-color: #f4f4f4;"); // พื้นหลังข้างนอก

        Label mainTitle = new Label("ตั้งค่าการค้นหา");
        mainTitle.setStyle("-fx-font-size: 24px; "
        		+ "-fx-font-weight: bold; "
        		+ "-fx-text-fill: #1a1a1a;");

        // สร้างปุ่ม "ทั้งหมด" แยกออกมา (อยู่ระหว่างหัวข้อ)
        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton allBtn = new RadioButton("ทั้งหมด");
        allBtn.setToggleGroup(categoryGroup);
        allBtn.setSelected(true);
        allBtn.setStyle("-fx-text-fill: #1a1a1a; "
        		+ "-fx-font-size: 14px; "
        		+ "-fx-font-weight: bold;");
        allBtn.setPadding(new Insets(20));
        allBtn.setOnAction(e -> loadBooks(bookGrid, booksFromFile, noBookLabel));

        // กล่องเมนูหมวดหมู่ (สีเข้มโค้งมนตามรูป)
        VBox categoryCard = new VBox(12);
        categoryCard.setPadding(new Insets(20));
        categoryCard.setStyle("-fx-background-color: #343a40; "
        		+ "-fx-background-radius: 15;");

        Label categoryHeader = new Label("หมวดหมู่สินค้า");
        categoryHeader.setStyle("-fx-text-fill: white; -fx-font-size: 14px; "
        						+ "-fx-font-weight: bold;");
        
        // ขีดเส้นใต้หัวข้อเล็กน้อย
        VBox headerBox = new VBox(8, categoryHeader);
        headerBox.setPadding(new Insets(0, 0, 10, 0));
        headerBox.setStyle("-fx-border-color: #495057; "
        				+ "-fx-border-width: 0 0 1 0;");

        categoryCard.getChildren().add(headerBox);

        String[] cats = {"เหนือธรรมชาติ", "จิตวิทยา", "ระทึกขวัญ", "ผี", "เลือดสาด", "ไซไฟ", "ซอมบี้"};
        for (String c : cats) {
            RadioButton rb = new RadioButton(c);
            rb.setToggleGroup(categoryGroup);
            rb.setStyle("-fx-text-fill: #adb5bd; "
            		+ "-fx-font-size: 14px; "
            		+ "-fx-cursor: hand;");
            
            // เอฟเฟกต์เมื่อเลือก
            rb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) rb.setStyle("-fx-text-fill: white; "
                		+ "-fx-font-size: 14px; "
                		+ "-fx-font-weight: bold;");
                
                else rb.setStyle("-fx-text-fill: #adb5bd; "
                		+ "-fx-font-size: 14px;");
            });

            rb.setOnAction(e -> {
                List<Book> filtered = booksFromFile.stream()
                        .filter(b -> b.getCategory().equals(c))
                        .toList();
                loadBooks(bookGrid, filtered, noBookLabel);
            });
            categoryCard.getChildren().add(rb);
        }

        sidebarContainer.getChildren().addAll(mainTitle, allBtn, categoryCard);
        root.setLeft(sidebarContainer);

        // --- TOP BAR ---
        PopAndPriceSort sortUI = new PopAndPriceSort(bookService);
        HBox sortBox = sortUI.createSortBox(sorted -> loadBooks(bookGrid, sorted, noBookLabel));
        sortBox.setPadding(new Insets(10, 20, 10, 0));
        sortBox.setAlignment(Pos.CENTER_RIGHT);

        NavBar nav = new NavBar();
        VBox topBox = new VBox(nav.createNavBar(stage, bookService, b -> loadBooks(bookGrid, booksFromFile, noBookLabel)), sortBox);
        root.setTop(topBox);
        
        return new Scene(root, 1000, 600);
    }
}