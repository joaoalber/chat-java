package controller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socketsthreads.Cliente;
import socketsthreads.Servidor;

public class LoginController  {
    
    @FXML private TextField campoNome;
    
    Cliente cliente = new Cliente();
   
    public void fazerLogin(ActionEvent event) throws IOException {
        URL som = getClass().getResource("/imagens/login.wav");
        AudioClip audio = Applet.newAudioClip(som);
        audio.play();
        
        Parent pai = FXMLLoader.load(getClass().getResource("/view/chat.fxml"));
        Scene chat = new Scene(pai);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        System.out.println("login:"+campoNome.getText());
        window.setTitle("Chat");
        window.setScene(chat);
        window.show();
        cliente.conectar();
        
    }
    
    public void autenticar() {
        
    }
    
}
