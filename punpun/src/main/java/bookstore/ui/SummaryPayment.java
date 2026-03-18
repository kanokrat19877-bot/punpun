package bookstore.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SummaryPayment extends SummaryChart {

    public SummaryPayment(Stage stage,int totalBook,double totalPrice){

        super(stage,null);

        totalBookLabel.setText(String.valueOf(totalBook));
        totalPriceLabel.setText(totalPrice + " บาท");
    }

    @Override
    protected void buildUI(){

        Label title = new Label("สรุปราคา");

        Label amountTitle = new Label("จำนวน");
        Label amountText = new Label("หนังสือเล่ม");

        Label priceTitle = new Label("ราคา");

        VBox amountBox = new VBox(10,
                amountTitle,
                new VBox(amountText,totalBookLabel)
        );

        VBox priceBox = new VBox(10,
                priceTitle,
                totalPriceLabel
        );

        summary.getChildren().addAll(
                title,
                amountBox,
                priceBox
        );
    }
}