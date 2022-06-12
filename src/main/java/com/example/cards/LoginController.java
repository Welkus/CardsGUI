package com.example.cards;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

public class LoginController {
    public LoginController() {
    }

    @FXML public TextField userName;

    public static String username;
    public static ArrayList<String> users = new ArrayList<String>();
    //public DeckViewController mainController = new DeckViewController();

    public void handleJoin() {
        System.out.println(userName.getText());
        username = userName.getText();
        users.add(username);
        Stage stage = (Stage)userName.getScene().getWindow();
        stage.close();
    }
    public String getUserName(){
     String label = username;
        return label;

    }

}