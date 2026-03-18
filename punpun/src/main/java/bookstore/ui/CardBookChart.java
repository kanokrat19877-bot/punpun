package bookstore.ui;

import bookstore.model.Book;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CardBookChart extends HBox {

    private Book book;
    private int quantity = 1;

    private CheckBox checkBox;
    private Label qtyLabel;
    private Label priceLabel;

    public CardBookChart(Book book) {

        this.book = book;

        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("""
                -fx-background-color:white;
                -fx-padding:20;
                -fx-border-radius:10;
                -fx-background-radius:10;
                -fx-border-color:#dddddd;
                """);

        // checkbox
        checkBox = new CheckBox();

        // รูป placeholder
        Pane img = new Pane();
        img.setPrefSize(120,120);
        img.setStyle("-fx-background-color:#cccccc;");

        // info
        VBox info = new VBox(5);

        Label title = new Label(book.getTitle());
        title.setStyle("-fx-font-weight:bold");

        Label category = new Label(book.getCategory());

        priceLabel = new Label(book.getPrice() + " บาท");
        priceLabel.setStyle("-fx-text-fill:red; -fx-font-weight:bold");

        info.getChildren().addAll(title,category,priceLabel);

        // quantity control
        Button minus = new Button("-");
        Button plus = new Button("+");

        qtyLabel = new Label(String.valueOf(quantity));
        qtyLabel.setMinWidth(30);
        qtyLabel.setAlignment(Pos.CENTER);

        minus.setOnAction(e -> {
            if(quantity>1){
                quantity--;
                qtyLabel.setText(String.valueOf(quantity));
            }
        });

        plus.setOnAction(e -> {
            quantity++;
            qtyLabel.setText(String.valueOf(quantity));
        });

        HBox qtyBox = new HBox(10,minus,qtyLabel,plus);
        qtyBox.setAlignment(Pos.CENTER);

        getChildren().addAll(checkBox,img,info,qtyBox);
        
        
    }

    public boolean isSelected(){
        return checkBox.isSelected();
    }

    public int getQuantity(){
        return quantity;
    }

    public double getTotalPrice(){
        return book.getPrice()*quantity;
    }
}