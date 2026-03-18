package bookstore.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SelectPay {

    private VBox view;

    private RadioButton codBtn;
    private RadioButton debitBtn;

    private TextField cardField;

    private Button confirmBtn;

    public SelectPay(){

        view = new VBox(20);
        view.setPadding(new Insets(40));

        ToggleGroup group = new ToggleGroup();

        // ชำระปลายทาง
        codBtn = new RadioButton("ชำระเงินปลายทาง");
        codBtn.setToggleGroup(group);

        // debit card
        debitBtn = new RadioButton("Debit Card");
        debitBtn.setToggleGroup(group);

        // ช่องกรอกเลขบัตร
        cardField = new TextField();
        cardField.setPromptText("Card Number");
        cardField.setDisable(true);

        // ปุ่มยืนยัน
        confirmBtn = new Button("ยืนยันการชำระเงิน");
        confirmBtn.setDisable(true);

        // ===== logic =====

        codBtn.setOnAction(e -> {

            cardField.setDisable(true);
            cardField.clear();

            confirmBtn.setDisable(false);

        });

        debitBtn.setOnAction(e -> {

            cardField.setDisable(false);

            validate();

        });

        cardField.textProperty().addListener((obs,oldV,newV)->{

            validate();

        });

        view.getChildren().addAll(
                codBtn,
                debitBtn,
                cardField
        );

    }

    private void validate(){

        if(debitBtn.isSelected() && !cardField.getText().isEmpty()){
            confirmBtn.setDisable(false);
        }else if(codBtn.isSelected()){
            confirmBtn.setDisable(false);
        }else{
            confirmBtn.setDisable(true);
        }

    }

    public VBox getView(){
        return view;
    }

    public Button getConfirmButton(){
        return confirmBtn;
    }

}