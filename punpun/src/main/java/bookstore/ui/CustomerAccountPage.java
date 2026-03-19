package bookstore.ui;

import bookstore.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class CustomerAccountPage {

    public Scene createSceneAccCustomer(Stage stage) {
    	
    	Font font = Font.loadFont(
                getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
                20
            );
    	System.out.println(font);
    	
        // 1. ดึง User จาก Session และตรวจสอบว่าเป็น Customer หรือไม่
        User user = UserSession.getUser();
        if (user == null || !(user instanceof Customer)) {
            // ถ้าไม่ใช่ Customer ให้กลับไปหน้า Login กันเหนียว
            return new LoginPage().createLoginScene(stage);
        }
        Customer customer = (Customer) user;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // --- TOP NAV ---
        HBox navBar = new HBox(20);
        navBar.setPadding(new Insets(15, 25, 15, 25));
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setStyle("-fx-background-color: #1f2733;");
        Label logo = new Label("PUNPUN HORROR");
        logo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button backBtn = new Button("< กลับไปหน้าหลัก");
        backBtn.setStyle("-fx-background-color:transparent; "
        		+ "-fx-text-fill:white; "
        		+ "-fx-cursor:hand;");
        backBtn.setOnAction(e -> stage.setScene(new StorePage().createStoreScene(stage)));
        
        navBar.getChildren().addAll(backBtn, logo);
        root.setTop(navBar);

        // --- MAIN BODY ---
        HBox mainBody = new HBox(40);
        mainBody.setPadding(new Insets(40, 60, 40, 60));
        mainBody.setAlignment(Pos.TOP_LEFT);

        // === LEFT SIDE (Profile) ===
        VBox leftSide = new VBox(20);
        leftSide.setPrefWidth(280);
        leftSide.setAlignment(Pos.TOP_CENTER);

        Label leftTitle = new Label("ข้อมูลส่วนตัว\n& แดชบอร์ด");
        leftTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        leftTitle.setAlignment(Pos.CENTER);

        // Profile Card
        VBox profileCard = new VBox(15);
        profileCard.setPadding(new Insets(25));
        profileCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; "
                           + "-fx-border-color: #eaeaea; -fx-border-radius: 15; "
                           + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Circle avatar = new Circle(45, Color.web("#b2bec3"));
        
        Label nameInfo = new Label("ชื่อ : " + customer.getName());
        Label emailInfo = new Label("อีเมล : " + customer.getEmail());
        Label roleInfo = new Label("สถานะ : ลูกค้าทั่วไป");
        
        profileCard.getChildren().addAll(new StackPane(avatar), nameInfo, emailInfo, roleInfo);

        // Button: ตัด Edit Profile ออกเหลือแค่ Log out
        Button logoutBtn = new Button("ออกจากระบบ");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setPrefHeight(40);
        logoutBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            UserSession.setUser(null);
            stage.setScene(new LoginPage().createLoginScene(stage));
        });

        leftSide.getChildren().addAll(leftTitle, profileCard, logoutBtn);

        // === RIGHT SIDE (ประวัติการซื้อ - ดึงจาก OrderHistory) ===
        VBox rightSide = new VBox(15);
        HBox.setHgrow(rightSide, Priority.ALWAYS);

        Label rightTitle = new Label("ประวัติการซื้อ");
        rightTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox historyList = new VBox(15);
        
        // ดึง List ของ Order มาจาก Customer
        List<Order> orders = customer.viewOrderHistory();

        if (orders == null || orders.isEmpty()) {
            // ถ้ายังไม่มีประวัติการซื้อ
            Label noHistory = new Label("ยังไม่มีประวัติการซื้อในขณะนี้");
            noHistory.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 14px;");
            historyList.getChildren().add(noHistory);
        } else {
            // วนลูปแสดงผลทุุก Order ที่เคยซื้อ
            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    historyList.getChildren().add(createRealItemCard(item));
                }
            }
        }

        ScrollPane scroll = new ScrollPane(historyList);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(450);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");

        rightSide.getChildren().addAll(rightTitle, scroll);

        mainBody.getChildren().addAll(leftSide, rightSide);
        root.setCenter(mainBody);
        root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );
        return new Scene(root, 1000, 600);
    }

    // ฟังก์ชันสร้างการ์ดโดยอิงจากข้อมูล OrderItem จริงๆ
    private HBox createRealItemCard(OrderItem item) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                    + "-fx-border-color: #eaeaea; -fx-border-radius: 12; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 10, 0, 0, 5);");
        
        // สมมติเป็นสีแทนรูปภาพ
        Pane img = new Pane();
        img.setPrefSize(80, 100);
        img.setStyle("-fx-background-color: #dfe6e9; -fx-background-radius: 5;");
        
        VBox info = new VBox(8);
        Label lbTitle = new Label(item.getbook().getTitle()); // ดึงชื่อหนังสือ
        lbTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        lbTitle.setWrapText(true);

        Label lbPrice = new Label("ราคา: " + String.format("%.2f", item.getSubtotal()) + " บาท");
        lbPrice.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
        
        Label lbQty = new Label("จำนวน: " + item.getQuantity() + " เล่ม");
        lbQty.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        
        info.getChildren().addAll(lbTitle, lbPrice, lbQty);
        card.getChildren().addAll(img, info);
        return card;
    }
}