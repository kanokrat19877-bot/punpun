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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
        noBookLabel.setStyle("-fx-font-size:18px; -fx-text-fill: gray;");
        noBookLabel.setVisible(false);

        ScrollPane scrollPane = new ScrollPane(bookGrid);
        scrollPane.setFitToWidth(true);
        // ✅ ปรับกลับมาใช้สไตล์ทึบแสงเหมือนเดิม เพราะไม่ต้องมองทะลุแล้ว
        scrollPane.setStyle("-fx-background-color: #f4f4f4; -fx-background: #f4f4f4;");

        // ✅ เตรียมข้อมูลหลัก
        List<Book> booksFromFile = loadBooksFromFile();
        Inventory inventory = new Inventory();
        booksFromFile.forEach(inventory::addBook);
        BookService bookService = new BookService(inventory);

        loadBooks(bookGrid, booksFromFile, noBookLabel);

        // ✅ จัดการส่วน Center ของหน้าจอ (เหลือแค่รายการหนังสือและ Label แจ้งเตือน)
        StackPane centerPane = new StackPane();
        centerPane.getChildren().addAll(scrollPane, noBookLabel);

        BorderPane root = new BorderPane();
        root.setCenter(centerPane);

        // ✅ --- เริ่มส่วนการสร้างแถบเมนูข้าง (Sidebar) พร้อมรูปแบลกกราวอยู่ใต้เมนู ---

        VBox sidebarContainer = new VBox(15);
        sidebarContainer.setPadding(new Insets(25));
        sidebarContainer.setPrefWidth(260);
        sidebarContainer.setStyle("-fx-background-color: #f4f4f4;");
        // จัดให้เมนูอยู่ด้านบนซ้าย
        sidebarContainer.setAlignment(Pos.TOP_LEFT);

        Label mainTitle = new Label("ตั้งค่าการค้นหา");
        mainTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton allBtn = new RadioButton("ทั้งหมด");
        allBtn.setToggleGroup(categoryGroup);
        allBtn.setSelected(true);
        allBtn.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 10 0;");
        allBtn.setOnAction(e -> loadBooks(bookGrid, booksFromFile, noBookLabel));

        VBox categoryCard = new VBox(10);
        categoryCard.setPadding(new Insets(15));
        categoryCard.setStyle("-fx-background-color: #343a40; -fx-background-radius: 10;");

        Label categoryHeader = new Label("หมวดหมู่สินค้า");
        categoryHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
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

        // --- เพิ่มรูปแบลกกราวที่มุมซ้ายล่างของ Sidebar ---
        
        // 🖼️ 1. สร้าง ImageView และโหลดรูป (ใช้พาร์ทเดิมของเฮีย)
        ImageView bgImageView = new ImageView();
        try {
            File imgFile = new File("src/main/java/resources/Icon/pngegg.png");
            if (imgFile.exists()) {
                Image img = new Image(imgFile.toURI().toString());
                bgImageView.setImage(img);
            } else {
                System.out.println("❌ หาไฟล์รูปไม่เจอ ลองเช็คพาร์ทดูอีกที: " + imgFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error loading background image: " + e.getMessage());
        }

        // ปรับขนาดรูปให้พอดี (เพิ่มขนาดนิดหน่อยเพราะอยู่ใน Sidebar ที่แคบลง)
        bgImageView.setFitWidth(150); 
        bgImageView.setPreserveRatio(true);
        // รักษาความจางไว้ 30% เหมือนเดิม
        bgImageView.setOpacity(0.3);

        // 2. สร้างตัวคั่น (Spacer) เพื่อดันรูปภาพไปไว้ด้านล่างสุดของแถบ Sidebar
        // ตัว Spacer นี้จะพยายามขยายตัวในแนวตั้ง (Vgrow) ให้เต็มพื้นที่ที่เหลืออยู่
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // 3. ใส่ทุกอย่างลงใน sidebarContainer (ดันเมนูไว้บน คั่นด้วย spacer แล้วใส่รูปล่างสุด)
        sidebarContainer.getChildren().addAll(mainTitle, allBtn, categoryCard, spacer);
        
        if (bgImageView.getImage() != null) {
            sidebarContainer.getChildren().add(bgImageView);
            // ตั้งค่าระยะห่าง (Margin) ของรูปภาพภายใน VBox 
            // เว้นระยะนิดหน่อยให้ดูสมดุล
            VBox.setMargin(bgImageView, new Insets(0, 0, 10, 0)); // เว้นขอบล่าง 10px
        }

        // ✅ ใส่ Sidebar เข้าไปที่ด้านซ้ายของ Layout หลัก
        root.setLeft(sidebarContainer);

        // --- TOP BAR (NavBar & Sort) ---
        NavBar nav = new NavBar();
        PopAndPriceSort sortUI = new PopAndPriceSort(bookService);
        
        HBox sortBox = sortUI.createSortBox(sorted -> loadBooks(bookGrid, sorted, noBookLabel));
        sortBox.setAlignment(Pos.CENTER_RIGHT);
        sortBox.setPadding(new Insets(10, 20, 10, 0));

        VBox topBox = new VBox(nav.createNavBar(stage, bookService, b -> loadBooks(bookGrid, booksFromFile, noBookLabel)), sortBox);
        root.setTop(topBox);
        
        try {
            root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("⚠️ Warning: ไม่พบไฟล์ style.css");
        }
        
        return new Scene(root, 1000, 600);
    }
}