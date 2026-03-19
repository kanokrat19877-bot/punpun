package bookstore.ui;

import javafx.animation.PauseTransition;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PaymentPage {

    public Scene createScene(Stage stage,int totalBook,double totalPrice){
    	
    	Font font = Font.loadFont(
                getClass().getResourceAsStream("/resources/font/Kanit-Regular.ttf"),
                20
            );
    	System.out.println(font);
        BorderPane root = new BorderPane();

        // ================= NAVBAR =================

        /*NavBar nav = new NavBar();
        root.setTop(nav.createNavBar(stage, null, null));*/

        // ================= BACK BUTTON =================

        Button backBtn = new Button("< Back");
        backBtn.setStyle("""
                -fx-background-color:transparent;
                -fx-font-size:14px;
                -fx-font-weight:bold;
                """);

        backBtn.setOnAction(e -> {
            Chart chart = new Chart();
            stage.setScene(chart.createScene(stage));
        });

        // ================= COMPONENT =================

        SummaryPayment summary = new SummaryPayment(stage,totalBook,totalPrice);
        SelectPay selectPay = new SelectPay();

        // ดึงปุ่มจาก SelectPay
        Button confirmBtn = selectPay.getConfirmButton();

        Label successLabel = new Label();
        successLabel.setStyle("-fx-font-size:14px; -fx-text-fill:green;");

        // ================= CONFIRM PAYMENT =================

        confirmBtn.setOnAction(e -> {

            successLabel.setText("ชำระเสร็จสิ้นแล้ว\nกรุณารอ 3 วินาที");

            PauseTransition delay = new PauseTransition(Duration.seconds(3));

            delay.setOnFinished(event -> {
                StorePage store = new StorePage();
                stage.setScene(store.createStoreScene(stage));
            });

            delay.play();
        });

        // ================= CONTENT =================

        HBox content = new HBox(60);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(
                summary.getView(),
                selectPay.getView()
        );

        // ================= CARD =================

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setMaxWidth(700);

        card.getChildren().addAll(
                content,
                confirmBtn,
                successLabel
        );

        card.setStyle("""
                -fx-background-color:#e8e8e8;
                -fx-background-radius:15;
                -fx-effect:dropshadow(three-pass-box,rgba(0,0,0,0.2),10,0,0,5);
                """);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(40));

        // ================= PAGE =================

        VBox page = new VBox(10);
        page.getChildren().addAll(backBtn,center);

        root.setCenter(page);
        root.getStylesheets().add(
                getClass().getResource("/resources/style.css").toExternalForm()
            );

        return new Scene(root,1000,600);
    }
}