package bookstore.ui;

import bookstore.model.Book;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CardBook extends VBox {

    public CardBook(Book book) {
        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(160); // ปรับกว้างขึ้นนิดหน่อยให้ดูสมดุล
        setPadding(new javafx.geometry.Insets(8));

        setStyle("""
                -fx-background-color: white;
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6,0,0,2);
                """);

        // 🖼️ ส่วนแสดงรูปภาพ
        ImageView coverBook = new ImageView();
        coverBook.setFitWidth(140);
        coverBook.setFitHeight(180);
        coverBook.setPreserveRatio(true);

        try {
            String path = book.getImagePath();
            // ตรวจสอบว่า path ไม่เป็น null และไม่ว่าง
            if (path != null && !path.isEmpty() && !path.equalsIgnoreCase("null")) {
                // เติม /resources/ นำหน้าชื่อไฟล์เพื่อให้ Java หาในโฟลเดอร์ resources เจอ
                String fullPath = path;
                var is = getClass().getResourceAsStream(fullPath);
                
                if (is != null) {
                    coverBook.setImage(new Image(is));
                } else {
                    System.out.println("❌ หาไฟล์รูปไม่เจอ: " + fullPath);
                    setDefaultBackground(coverBook);
                }
            } else {
                setDefaultBackground(coverBook);
            }
        } catch (Exception e) {
            setDefaultBackground(coverBook);
        }

        // 📖 ข้อมูลหนังสือ
        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        title.setWrapText(true);
        title.setPrefHeight(40);

        Label author = new Label(book.getAuthorName());
        author.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        Label category = new Label(book.getCategory());
        category.setStyle("-fx-text-fill: white; -fx-background-color: #555; -fx-padding: 2 6; -fx-background-radius: 5; -fx-font-size: 10px;");

        Label price = new Label(String.format("%.2f ฿", book.getPrice()));
        price.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 15px;");

        Label rating = new Label("⭐ " + book.getRating());
        rating.setStyle("-fx-font-size: 12px;");

        Label stock = new Label("คลัง: " + book.getStock());
        stock.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");

        HBox bottom = new HBox(10, price, rating);
        bottom.setAlignment(Pos.CENTER_LEFT);

        // จัดกลุ่มรูปให้อยู่ตรงกลาง
        StackPane imageHolder = new StackPane(coverBook);
        imageHolder.setPrefHeight(180);
        imageHolder.setAlignment(Pos.CENTER);

        getChildren().addAll(imageHolder, title, author, category, bottom, stock);

        // คลิกเพื่อไปหน้าดูรายละเอียด
        setOnMouseClicked(e -> {
            BookInfo page = new BookInfo();
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(page.createScene(stage, book));
        });
    }

    // ฟังก์ชันสร้างสีเทาเวลาหารูปไม่เจอ
    private void setDefaultBackground(ImageView iv) {
        iv.setImage(null); // เคลียร์รูปเก่า
        iv.setStyle("-fx-background-color: #cccccc; -fx-border-color: #999;");
    }
}