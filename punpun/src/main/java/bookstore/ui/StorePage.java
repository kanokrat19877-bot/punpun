package bookstore.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    	System.out.println(font);
    	
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
        File file = new File("src/main/java/resources/Stock.txt");
        
        if (!file.exists()) {
            System.out.println("⚠️ Warning: Stock.txt not found");
            return bookList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lastId = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 8) {
                    lastId++;
                    bookList.add(new Book(lastId, data[0].trim(), data[1].trim(), data[2].trim(), 
                                 Double.parseDouble(data[3].trim()), Double.parseDouble(data[5].trim()), 
                                 Integer.parseInt(data[4].trim()), Integer.parseInt(data[6].trim()), data[7].trim()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public Scene createStoreScene(Stage stage) {
        GridPane bookGrid = new GridPane();
        bookGrid.setHgap(20); bookGrid.setVgap(20);
        bookGrid.setPadding(new Insets(20));

        Label noBookLabel = new Label("ไม่พบหนังสือในสต็อก");
        noBookLabel.setStyle("-fx-font-size:18px; "
        		+ "-fx-text-fill: gray;");
        noBookLabel.setVisible(false);

        ScrollPane scrollPane = new ScrollPane(bookGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent; "
        		+ "-fx-background: #f4f4f4;");

        StackPane centerPane = new StackPane(scrollPane, noBookLabel);
        
        List<Book> booksFromFile = loadBooksFromFile();
        Inventory inventory = new Inventory();
        booksFromFile.forEach(inventory::addBook);
        BookService bookService = new BookService(inventory);

        loadBooks(bookGrid, booksFromFile, noBookLabel);

        BorderPane root = new BorderPane();
        root.setCenter(centerPane);

        // --- SIDEBAR ---
        VBox sidebarContainer = new VBox(15);
        sidebarContainer.setPadding(new Insets(25));
        sidebarContainer.setPrefWidth(260);
        sidebarContainer.setStyle("-fx-background-color: #f4f4f4;");

        Label mainTitle = new Label("ตั้งค่าการค้นหา");
        mainTitle.setStyle("-fx-font-size: 22px; "
        		+ "-fx-font-weight: bold;");

        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton allBtn = new RadioButton("ทั้งหมด");
        allBtn.setToggleGroup(categoryGroup);
        allBtn.setSelected(true);
        allBtn.setStyle("-fx-font-weight: bold; "
        		+ "-fx-padding: 10 0 10 0;");
        allBtn.setOnAction(e -> loadBooks(bookGrid, booksFromFile, noBookLabel));

        VBox categoryCard = new VBox(10);
        categoryCard.setPadding(new Insets(15));
        categoryCard.setStyle("-fx-background-color: #343a40; "
        		+ "-fx-background-radius: 10;");

        Label categoryHeader = new Label("หมวดหมู่สินค้า");
        categoryHeader.setStyle("-fx-text-fill: white; "
        		+ "-fx-font-weight: bold;");
        categoryCard.getChildren().add(categoryHeader);

        String[] cats = {"สยองขวัญ", "จิตวิทยา", "ระทึกขวัญ", "ผี", "เลือดสาด", "ไซไฟ", "ซอมบี้"};
        for (String c : cats) {
            RadioButton rb = new RadioButton(c);
            rb.setToggleGroup(categoryGroup);
            rb.setStyle("-fx-text-fill: #adb5bd;");
            rb.setOnAction(e -> {
                List<Book> filtered = booksFromFile.stream()
                        .filter(b -> b.getCategory().equalsIgnoreCase(c))
                        .collect(Collectors.toList());
                loadBooks(bookGrid, filtered, noBookLabel);
            });
            categoryCard.getChildren().add(rb);
        }

        sidebarContainer.getChildren().addAll(mainTitle, allBtn, categoryCard);
        root.setLeft(sidebarContainer);

        // --- TOP BAR (NavBar & Sort) ---
        NavBar nav = new NavBar();
        PopAndPriceSort sortUI = new PopAndPriceSort(bookService);
        
        // ส่วนจัดเรียง (Sort)
        HBox sortBox = sortUI.createSortBox(sorted -> loadBooks(bookGrid, sorted, noBookLabel));
        sortBox.setAlignment(Pos.CENTER_RIGHT);
        sortBox.setPadding(new Insets(10, 20, 10, 0));

        // รวม NavBar เข้ากับตัวจัดเรียง
        VBox topBox = new VBox(nav.createNavBar(stage, bookService, b -> loadBooks(bookGrid, booksFromFile, noBookLabel)), sortBox);
        root.setTop(topBox);
        
        root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );
        return new Scene(root, 1000, 600);
    }
}