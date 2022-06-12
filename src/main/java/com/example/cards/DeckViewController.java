package com.example.cards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckViewController extends Thread implements Initializable {
    LoginController loginController = new LoginController();
    WarningController warning = new WarningController();
    private static boolean NEXT_F = true;
    private static boolean NEXT_S = false;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    int port = 8889;

    @FXML
    private ImageView opositCardImageView;
    @FXML
    private ImageView activeCardImageView;
    @FXML
    private ImageView activeGameImageView;
    @FXML
    public Label clientName;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;
    private DeckOfCards deck;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createAcountForm();
        clientName.setText(loginController.getUserName());
        connectSocket();

        deck = new DeckOfCards();
        deck.shuffle();
        opositCardImageView.setImage(deck.getBackOfCardImage());
        activeCardImageView.setImage(deck.getBackOfCardImage());
        activeGameImageView.setImage(deck.getActiveGameImage());
    }

    @FXML
    public void newGameButtonPushed() {
        deck = new DeckOfCards();
        deck.shuffle();
        createAcountForm();
        clientName.setText(loginController.getUserName());
        opositCardImageView.setImage(deck.getBackOfCardImage());
        activeCardImageView.setImage(deck.getBackOfCardImage());

        NEXT_F = true;
        NEXT_S = false;
    }

    @FXML
    public void finishGameButtonPushed() {
        System.exit(0);
    }

    @FXML
    public void nextCardButtonPushed() {
        if (NEXT_F)
            try {
                activeCardImageView.setImage(deck.dealTopCard().getImage());
            } catch (Exception e) {
                warning.showWarning("Koniec Gry.", "Koniec rozgrywki.\nRozpocznij od nowa.");
            }
        NEXT_F = false;
        NEXT_S = true;
    }

    @FXML
    public void checkOpositCard() {
        if (NEXT_S)
            try {
                opositCardImageView.setImage(deck.dealTopCard().getImage());

            } catch (Exception e) {
                warning.showWarning("Koniec Gry.", "Koniec rozgrywki.\nRozpocznij od nowa.");
            }
        NEXT_S = false;
        NEXT_F = true;
    }

    public void createAcountForm() {
        try {

            Parent root = FXMLLoader.load(this.getClass().getResource("login.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setTitle("Podaj dane");
            Scene scene = new Scene(root, 500, 168);
            scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
            mainStage.setScene(scene);
            mainStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", port);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            if (e.getMessage().equalsIgnoreCase("Connection refused: connect")) {
                System.out.println("Błąd połączenia z serwerem gry. Zakończenie programu! ");
                warning.showWarning("Błąd aplikacji", "Brak połączenia z serwerem gry.");
                System.exit(10);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(LoginController.username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("Papa")) {
                    break;
                }
                msgRoom.appendText(msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void send() {

        String msg = msgField.getText();
        writer.println(LoginController.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Ja: " + msg + "\n");
        msgField.setText("");

        if (msg.equalsIgnoreCase("wyloguj")) {
            System.exit(0);
        }
    }
}
