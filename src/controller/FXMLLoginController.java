package controller;

import controller.FXMLChatController;
import bean.Cliente;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FXMLLoginController implements Initializable {

    private static Cliente cliente;

    public static void setCliente(Cliente cliente) {
        FXMLLoginController.cliente = cliente;
    }

    @FXML
    private TextField nicknameTextField;

    @FXML
    private Button logarButton;

    @FXML
    void logarAction(ActionEvent event) throws IOException {
        //manda para o servidor
        PrintWriter escritor = new PrintWriter(cliente.getSocket().getOutputStream(), true); // true autoflush(toda linha ele manda )
        String nickname = nicknameTextField.getText();
        escritor.println("login:" + nickname);
        
        //le msg do servidor
        BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getSocket().getInputStream()));

        if (leitor.readLine().equals("login:false")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erro");
            alert.setHeaderText("");
            alert.setContentText("Esse nickname já foi cadastrado!");
           
            alert.showAndWait();
        } else {
            
            FXMLChatController.setCliente(cliente);
            FXMLChatController.setNickname(nickname); 
            //fechando tela de cadastro
            Stage tela = (Stage) logarButton.getScene().getWindow();
            tela.close();
            URL som = getClass().getResource("/imagens/login.wav");
            AudioClip audio = Applet.newAudioClip(som);
            audio.play();

            //chamar Chat
            Parent root = FXMLLoader.load(getClass().getResource("/view/Chat.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/imagens/icon.png"));
            stage.setTitle("Konoha Chat");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nicknameTextField.setPromptText("Digite seu usuário");
    }
}
