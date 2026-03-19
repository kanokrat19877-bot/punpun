package bookstore.ui;

import bookstore.model.Book;
import bookstore.service.BookService;
import bookstore.service.RepoerService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.*;

public class CardDashboard extends VBox {
	
	public CardDashboard() {}

    public CardDashboard(BookService bookService, RepoerService reportService) {
        setSpacing(20);
        setFillWidth(true);

        // ส่วนบน: กราฟยอดขายแยกประเภท
        VBox topGraph = createTopCard(bookService, reportService);
        
        // ส่วนล่าง: ยอดรวมเงิน + อันดับขายดี (ใส่ไว้ใน Card เดียวกัน)
        VBox bottomStats = createBottomCard(reportService);

        getChildren().addAll(topGraph, bottomStats);
    }

    private VBox createTopCard(BookService bookService, RepoerService reportService){
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        String best = bookService.getBestSeller();
        Label bestLabel = new Label("🏆 Best Seller Overall: " + best);
        bestLabel.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill: #2c3e50;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("ยอดขายตามหมวดหมู่");
        chart.setPrefHeight(250);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<String, List<Book>> booksByCategory = bookService.groupByCategory();
        
        booksByCategory.forEach((category, books) -> {
            int soldInCat = books.stream().mapToInt(Book::getSoldCount).sum();
            XYChart.Data<String, Number> dataNode = new XYChart.Data<>(category, soldInCat);
            series.getData().add(dataNode);
        });

        chart.getData().add(series);
        box.getChildren().addAll(bestLabel, chart);
        return box;
    }

    private VBox createBottomCard(RepoerService reportService){
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // --- ส่วนยอดเงิน ---
        double totalMoney = reportService.getTotalSales();
        Label totalLabel = new Label("💰 รายได้รวมทั้งหมด: " + String.format("%,.2f", totalMoney) + " บาท");
        totalLabel.setStyle("-fx-font-size:20px; -fx-font-weight:bold; -fx-text-fill: #27ae60;");

        // --- ส่วนอันดับหนังสือขายดี 3 อันดับ (เพิ่มเข้ามาตรงนี้) ---
        VBox rankSection = new VBox(10);
        Label rankTitle = new Label("📈 หนังสือขายดี 3 อันดับแรก");
        rankTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #34495e;");
        
        VBox rankList = new VBox(8);
        List<Map.Entry<Book, Integer>> top3 = reportService.getTop3Books();

        if (top3 == null || top3.isEmpty()) {
            rankList.getChildren().add(new Label("ยังไม่มีข้อมูลการขาย"));
        } else {
            int i = 1;
            for(Map.Entry<Book, Integer> e : top3){
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);
                
                Label lblRank = new Label("#" + i);
                lblRank.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-min-width: 25;");
                
                Label lblTitle = new Label(e.getKey().getTitle());
                lblTitle.setStyle("-fx-font-weight: bold;");
                
                Label lblQty = new Label("(" + e.getValue() + " เล่ม)");
                lblQty.setStyle("-fx-text-fill: #7f8c8d;");

                row.getChildren().addAll(lblRank, lblTitle, lblQty);
                rankList.getChildren().add(row);
                i++;
            }
        }

        rankSection.getChildren().addAll(rankTitle, rankList);
        
        // ใส่ทั้งยอดเงินและอันดับลงในกล่องล่าง
        box.getChildren().addAll(totalLabel, new javafx.scene.shape.Line(0,0,300,0){{ setStroke(javafx.scene.paint.Color.LIGHTGRAY); }}, rankSection);
        
        return box;
    }
}