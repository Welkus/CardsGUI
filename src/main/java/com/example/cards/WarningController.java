package com.example.cards;

import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarningController{

    public void showWarning(String title, String message) {

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        System.out.println(message);
        HBox verticalBox = new HBox();
        verticalBox.setPadding(new Insets(15, 5, 5, 5));
        verticalBox.setSpacing(10);
        ImageView alertimage = new ImageView();
        Image image = new Image(this.getClass().getResourceAsStream("/images/warning.png"));
        alertimage.setImage(image);
        alertimage.setFitWidth(40);
        alertimage.setFitHeight(40);
        alertimage.setLayoutX(20);
        alertimage.setLayoutY(20);
        Label alert = new Label(message);
        alert.setWrapText(true);

        HBox horizontalBox = new HBox();
        horizontalBox.setPadding(new Insets(45, 12, 15, 125));
        horizontalBox.setSpacing(10);
        Button buttonOk = new Button("OK");
        buttonOk.setPrefSize(100, 20);
        horizontalBox.getChildren().addAll(buttonOk);
        verticalBox.getChildren().addAll(alertimage, alert, horizontalBox);

        Scene scene = new Scene(verticalBox, 480, 110);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        dialogStage.setScene(scene);

        buttonOk.setOnAction(e->{
            dialogStage.hide();
            dialogStage.close();
        });
        dialogStage.showAndWait();

    }
}