package bookstore.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import bookstore.model.CartSession;
import bookstore.model.Cart;
import bookstore.model.CartItem;

import java.util.ArrayList;

public class SummaryChart {

    protected VBox summary;

    protected Label totalBookLabel = new Label("0");
    protected Label totalPriceLabel = new Label("0 บาท");

    protected ArrayList<CardBookChart> items;

    protected Stage stage;

    public SummaryChart(Stage stage, ArrayList<CardBookChart> items){

        this.stage = stage;
        this.items = items;

        summary = new VBox(20);
        summary.setPadding(new Insets(40));
        summary.setPrefWidth(220);

        buildUI();
    }

    protected void buildUI(){

        Label title = new Label("สรุปราคา");

        Label bookText = new Label("จำนวนหนังสือ");
        Label priceText = new Label("ราคา");

        Button calcBtn = new Button("คำนวณ");
        Button buyBtn = new Button("ซื้อ");

        buyBtn.setStyle("-fx-background-color:#cbbba0;");

        calcBtn.setOnAction(e -> calculate());

        buyBtn.setOnAction(e -> {

            int totalBook = Integer.parseInt(totalBookLabel.getText());
            double totalPrice = Double.parseDouble(
                    totalPriceLabel.getText().replace(" บาท","")
            );

            PaymentPage pay = new PaymentPage();
            stage.setScene(pay.createScene(stage,totalBook,totalPrice));

        });

        summary.getChildren().addAll(
                title,
                bookText, totalBookLabel,
                priceText, totalPriceLabel,
                calcBtn,
                buyBtn
        );
    }

    protected void calculate(){

        int totalBook = 0;
        double totalPrice = 0;

        Cart cart = CartSession.getCart();

        for(CartItem item : cart.getCartItem()){
            totalBook += item.getQuantity();
            totalPrice += item.getSubtotal();
        }

        totalBookLabel.setText(String.valueOf(totalBook));
        totalPriceLabel.setText(totalPrice + " บาท");
    }

    public VBox getView(){
        return summary;
    }

}