package bookstore.ui;

import bookstore.model.User;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class CardProflie extends VBox {

    public CardProflie(User user) {

        setSpacing(10);
        setAlignment(Pos.CENTER);
        setPrefWidth(250);
        setStyle(
                "-fx-background-color:#f4f4f4;" +
                "-fx-border-color:#b8a98f;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-padding:20;"
        );

        // ===== PROFILE IMAGE =====
        ImageView profileImg = new ImageView(
                new Image("https://via.placeholder.com/150") // เปลี่ยนทีหลังได้
        );
        profileImg.setFitWidth(120);
        profileImg.setFitHeight(120);

        // ทำให้เป็นวงกลม
        Circle clip = new Circle(60, 60, 60);
        profileImg.setClip(clip);

        // ===== TEXT INFO =====
        Label nameLabel = new Label("Name: " + user.getName());
        Label emailLabel = new Label("E-mail: " + user.getEmail());
        Label roleLabel = new Label("Role: " + user.getClass().getSimpleName());

        nameLabel.setStyle("-fx-font-size:14px;");
        emailLabel.setStyle("-fx-font-size:14px;");
        roleLabel.setStyle("-fx-font-size:14px;");

        VBox infoBox = new VBox(5, nameLabel, emailLabel, roleLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // ===== ADD ALL =====
        getChildren().addAll(profileImg, infoBox);
    }
}