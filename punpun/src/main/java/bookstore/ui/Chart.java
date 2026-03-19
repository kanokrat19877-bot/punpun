package bookstore.ui;

import bookstore.model.Book;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import bookstore.model.CartSession;
import bookstore.model.Cart;
import bookstore.model.CartItem;

import java.util.ArrayList;

public class Chart {

    private ArrayList<CardBookChart> items = new ArrayList<>();

    public Scene createScene(Stage stage){
    	
    	Font font = Font.loadFont(
                getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
                20
            );
    	System.out.println(font);

        BorderPane root = new BorderPane();

        // ================= NAV BAR =================
        HBox navBar = new HBox(20);
        navBar.setPadding(new Insets(15));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color:#1f2733;");

        Label logo = new Label("PUNPUN HORROR BOOK STORE");
        logo.setStyle("-fx-text-fill:white; -fx-font-size:16px; -fx-font-weight:bold;");

        Button cartBtn = new Button("Cart");
        Button account = new Button("Account");
        account.setStyle("-fx-background-color:#d8cbb2;");

        //HBox rightMenu = new HBox(60, cartBtn, account);

        navBar.getChildren().addAll(logo);

        root.setTop(navBar);

        // ================= BOOK LIST =================

        VBox list = new VBox(20);
        list.setPadding(new Insets(40));

        Cart cart = CartSession.getCart();

        for(CartItem item : cart.getCartItem()){

            CardBookChart card = new CardBookChart(item.getBook());

            items.add(card);
            list.getChildren().add(card);

        }

        // ================= SUMMARY =================

        SummaryChart summary = new SummaryChart(stage,items);
        

        root.setRight(summary.getView());

        // ================= BACK BUTTON =================

        Button backBtn = new Button("< Back");
        backBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-cursor: hand;
                """);
        // จัดให้ปุ่มชิดซ้าย
        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.CENTER_LEFT);
        
        backBtn.setOnAction(e -> {
            StorePage store = new StorePage();
            stage.setScene(store.createStoreScene(stage));
        });
        
        VBox pageContent = new VBox(10);
        pageContent.getChildren().addAll(backBtn,list);

        root.setLeft(pageContent);
        root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );
        return new Scene(root,1000, 600);
    }
}