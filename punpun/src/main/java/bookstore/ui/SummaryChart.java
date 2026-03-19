package bookstore.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
        summary.setPadding(new Insets(20));
        summary.setPrefWidth(280);
        summary.setAlignment(Pos.TOP_CENTER);

        buildUI();
        calculate(); 
    }

    protected void buildUI(){
    	
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
        
        // --- หัวข้อ ---
        Label title = new Label("สรุปราคา");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // --- ส่วนจำนวนหนังสือ ---
        VBox qtyContainer = new VBox();
        qtyContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");
        Label qtyHeader = new Label("จำนวน");
        qtyHeader.setMaxWidth(Double.MAX_VALUE);
        qtyHeader.setPadding(new Insets(8, 15, 8, 15));
        qtyHeader.setStyle("-fx-background-color: #3d4147; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5 5 0 0;");
        
        HBox qtyRow = new HBox();
        qtyRow.setPadding(new Insets(12));
        Label bookLabelText = new Label("หนังสือเล่ม");
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        qtyRow.getChildren().addAll(bookLabelText, spacer1, totalBookLabel);
        qtyContainer.getChildren().addAll(qtyHeader, qtyRow);

        // --- ส่วนราคา ---
        VBox priceContainer = new VBox();
        priceContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 5;");
        Label priceHeader = new Label("ราคา");
        priceHeader.setMaxWidth(Double.MAX_VALUE);
        priceHeader.setPadding(new Insets(8, 15, 8, 15));
        priceHeader.setStyle("-fx-background-color: #3d4147; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5 5 0 0;");
        
        HBox priceRow = new HBox();
        priceRow.setPadding(new Insets(12));
        priceRow.setAlignment(Pos.CENTER_RIGHT);
        priceRow.getChildren().add(totalPriceLabel);
        totalPriceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        priceContainer.getChildren().addAll(priceHeader, priceRow);

        // --- 🔘 ปุ่มคำนวณ (แบบเส้น Stroke รอบๆ) ---
        Button calcBtn = new Button("คำนวณ");
        calcBtn.setPrefWidth(200);
        calcBtn.setStyle("""
            -fx-background-color: transparent; 
            -fx-border-color: #D9D9D9; 
            -fx-border-width: 1; 
            -fx-border-radius: 10; 
            -fx-text-fill: #3d4147; 
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """);
        calcBtn.setOnAction(e -> calculate());

        // --- 🔘 ปุ่มซื้อ (ตามแบบในภาพ) ---
        Button buyBtn = new Button("ซื้อ");
        buyBtn.setPrefWidth(200);
        buyBtn.setPrefHeight(45);
        buyBtn.setStyle("""
            -fx-background-color: #d8cbb2; 
            -fx-text-fill: #1f2733; 
            -fx-font-weight: bold; 
            -fx-font-size: 18px; 
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);
        """);
        
        buyBtn.setOnAction(e -> {
            calculate();
            try {
                int totalBook = Integer.parseInt(totalBookLabel.getText());
                double totalPrice = Double.parseDouble(totalPriceLabel.getText().replace(" บาท",""));
                PaymentPage pay = new PaymentPage();
                stage.setScene(pay.createScene(stage, totalBook, totalPrice));
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        });

        summary.getChildren().addAll(title, qtyContainer, priceContainer, calcBtn, buyBtn);
    }

    protected void calculate(){
        int totalBook = 0;
        double totalPrice = 0;
        if (items != null) {
            for (CardBookChart itemCard : items) {
                if (itemCard.isSelected()) {
                    totalBook += itemCard.getQuantity();
                    totalPrice += itemCard.getTotalPrice();
                }
            }
        }
        totalBookLabel.setText(String.valueOf(totalBook));
        totalPriceLabel.setText(String.format("%.0f บาท", totalPrice));
    }

    public VBox getView(){
        return summary;
    }
}