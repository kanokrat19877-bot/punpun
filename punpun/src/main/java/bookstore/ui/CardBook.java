package bookstore.ui;

import bookstore.model.Book;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CardBook extends VBox {

    public CardBook(Book book) {

        setSpacing(6);
        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(150);

        setStyle("""
                -fx-background-color: white;
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-padding: 8;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6,0,0,2);
                """);

        // 🖼 รูป
        ImageView cover = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream(book.getImagePath()));
            cover.setImage(img);
        } catch (Exception e) {
            cover.setStyle("-fx-background-color: #cccccc;");
        }

        cover.setFitWidth(130);
        cover.setFitHeight(160);
        cover.setPreserveRatio(true);

        // 📖 ชื่อ
        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight: bold;");
        title.setWrapText(true);
        title.setMaxHeight(40);

        // ✍️ ผู้แต่ง
        Label author = new Label(book.getAuthorName());
        author.setStyle("-fx-text-fill: #666;");

        // 🏷 หมวดหมู่
        Label category = new Label(book.getCategory());
        category.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        // 💰 ราคา
        Label price = new Label(book.getPrice() + " ฿");
        price.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // ⭐ เรตติ้ง
        Label rating = new Label("⭐ " + book.getRating());

        // 📦 stock
        Label stock = new Label("Stock: " + book.getStock());
        stock.setStyle("-fx-font-size: 11px;");


        HBox bottom = new HBox(8, price, rating);

        getChildren().addAll(
                cover,
                title,
                author,
                category,
                bottom,
                stock
        );

        setOnMouseClicked(e -> {
            BookInfo page = new BookInfo();
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(page.createScene(stage, book));
        });
    }
}