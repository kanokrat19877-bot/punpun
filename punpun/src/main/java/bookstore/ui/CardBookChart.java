package bookstore.ui;

import bookstore.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class CardBookChart extends StackPane {

    private Book book;
    private int quantity = 1;

    private CheckBox checkBox;
    private Label qtyLabel;
    private Label priceLabel;

    public CardBookChart(Book book) {
        this.book = book;

        // --- ตั้งค่าความกว้างให้เล็กลง (เฮียสั่งจัดไป) ---
        this.setPrefWidth(450); 
        this.setMaxWidth(800); // ยืดได้แต่ไม่เกินนี้

        // --- Container หลัก ---
        HBox mainContent = new HBox(15); // ลดระยะห่างระหว่างองค์ประกอบ
        mainContent.setAlignment(Pos.CENTER_LEFT);
        mainContent.setPadding(new Insets(12, 20, 12, 20)); // ลด Padding ให้กระชับ
        mainContent.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #eeeeee;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);
                """);

        // --- 1. รูปภาพหนังสือ (ขนาดเล็กลงนิดนึง) ---
        ImageView bookImageView = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream(book.getImagePath()));
            bookImageView.setImage(img);
        } catch (Exception e) {
            System.out.println("หาภาพไม่เจอ: " + book.getImagePath());
        }
        bookImageView.setFitWidth(100); 
        bookImageView.setFitHeight(130);
        bookImageView.setPreserveRatio(true);

        // --- 2. ข้อมูลหนังสือ ---
        VBox info = new VBox(5);
        info.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        title.setWrapText(true);

        Label category = new Label("หมวดหมู่ > " + book.getCategory());
        category.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px;");

        info.getChildren().addAll(title, category);

        // --- 3. ส่วนควบคุมขวาสุด ---
        VBox rightSide = new VBox(10);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        rightSide.setMinWidth(120);

        // ปุ่มบวกลบ (เล็กหน่อยแต่กดง่าย)
        Button minus = new Button("-");
        Button plus = new Button("+");
        String btnStyle = "-fx-background-color: #f0f0f0; -fx-background-radius: 4; -fx-cursor: hand;";
        minus.setStyle(btnStyle);
        plus.setStyle(btnStyle);
        minus.setPrefSize(28, 28);
        plus.setPrefSize(28, 28);

        qtyLabel = new Label(String.valueOf(quantity));
        qtyLabel.setMinWidth(35);
        qtyLabel.setAlignment(Pos.CENTER);
        qtyLabel.setStyle("-fx-font-size: 14px;");

        minus.setOnAction(e -> {
            if (quantity > 1) {
                quantity--;
                qtyLabel.setText(String.valueOf(quantity));
            }
        });

        plus.setOnAction(e -> {
            quantity++;
            qtyLabel.setText(String.valueOf(quantity));
        });

        HBox qtyBox = new HBox(5, minus, qtyLabel, plus);
        qtyBox.setAlignment(Pos.CENTER_RIGHT);

        priceLabel = new Label(String.format("%.0f ฿", book.getPrice()));
        priceLabel.setStyle("-fx-text-fill: #ff4d4d; -fx-font-weight: bold; -fx-font-size: 16px;");

        rightSide.getChildren().addAll(qtyBox, priceLabel);

        mainContent.getChildren().addAll(bookImageView, info, rightSide);

        // --- 4. CheckBox มุมขวาบน ---
        checkBox = new CheckBox();
        AnchorPane overlay = new AnchorPane(checkBox);
        AnchorPane.setTopAnchor(checkBox, 10.0);
        AnchorPane.setRightAnchor(checkBox, 10.0);
        overlay.setMouseTransparent(false); 

        this.getChildren().addAll(mainContent, overlay);
        this.setPadding(new Insets(5, 0, 5, 0));
    }

    public boolean isSelected() { return checkBox.isSelected(); }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return book.getPrice() * quantity; }
}