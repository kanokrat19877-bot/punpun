package bookstore.ui;

import bookstore.model.CartSession;
import bookstore.model.Book;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BookInfo {

    private int quantity = 1;

    public Scene createScene(Stage stage, Book book) {

        BorderPane root = new BorderPane();

        // ===================== NAV BAR =====================

        NavBar nav = new NavBar();
        root.setTop(nav.createNavBar(stage, null, null));

        // ===================== CONTENT =====================

        HBox content = new HBox(50);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.CENTER_LEFT);

        // รูปหนังสือ
        Pane bookImage = new Pane();
        bookImage.setPrefSize(300, 400);
        bookImage.setStyle("-fx-background-color:#cccccc;");

        VBox infoBox = new VBox(15);

        Label category = new Label(book.getCategory());
        category.setStyle("-fx-text-fill:gray;");

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-size:20px; -fx-font-weight:bold;");

        Label author = new Label(book.getAuthorName());

        Label rating = new Label("★ " + book.getRating());

        // ===================== QUANTITY =====================

        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER_LEFT);

        Label qtyLabel = new Label("จำนวน");

        Button minusBtn = new Button("-");
        Button plusBtn = new Button("+");

        Label qtyValue = new Label(String.valueOf(quantity));
        qtyValue.setMinWidth(40);
        qtyValue.setAlignment(Pos.CENTER);

        minusBtn.setOnAction(e -> {
            if (quantity > 1) {
                quantity--;
                qtyValue.setText(String.valueOf(quantity));
            }
        });

        plusBtn.setOnAction(e -> {
            quantity++;
            qtyValue.setText(String.valueOf(quantity));
        });

        quantityBox.getChildren().addAll(qtyLabel, minusBtn, qtyValue, plusBtn);

        // ===================== PRICE =====================

        Label price = new Label(book.getPrice() + " บาท");
        price.setStyle("-fx-text-fill:red; -fx-font-size:18px; -fx-font-weight:bold;");

        // ===================== BUTTON =====================

        HBox buttons = new HBox(20);

        Button addCart = new Button("ใส่ตะกร้า");
        Button buyNow = new Button("ซื้อ");

        buyNow.setStyle("-fx-background-color:#cbbba0;");

        buttons.getChildren().addAll(addCart, buyNow);

        addCart.setOnAction(e -> {

            for(int i = 0 ; i < quantity ; i++){
                CartSession.getCart().addBook(book);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("เพิ่มหนังสือลงตะกร้าแล้ว");
            alert.showAndWait();
        });

        infoBox.getChildren().addAll(
                category,
                title,
                author,
                rating,
                quantityBox,
                price,
                buttons
        );

        content.getChildren().addAll(bookImage, infoBox);

        // ===================== BACK BUTTON =====================

        Button backBtn = new Button("< Back");

        backBtn.setOnAction(e -> {

            StorePage store = new StorePage();
            stage.setScene(store.createStoreScene(stage));

        });

        VBox pageContent = new VBox(20);
        pageContent.setPadding(new Insets(40));
        pageContent.getChildren().addAll(backBtn, content);

        root.setCenter(pageContent);

        return new Scene(root, 1000, 600);
    }

    public int getQuantity(){
        return quantity;
    }
}