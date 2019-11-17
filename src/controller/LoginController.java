package controller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import socketsthreads.Conexao;

public class LoginController  {
    
    @FXML private TextField campoNome;
    
    
    
    ArrayList<Conexao> conn;
    
    
    public void myConn(ArrayList<Conexao> cnx){
        conn = cnx;
        System.out.println(conn.size());
    }
    
    public void fazerLogin(ActionEvent event) throws IOException {
        
        System.out.println(conn.size());
        //System.out.println(conn.get(0).usuario.getNome());
        
            URL som = getClass().getResource("/imagens/login.wav");
            AudioClip audio = Applet.newAudioClip(som);
            audio.play();
        
        Parent pai = FXMLLoader.load(getClass().getResource("/view/chat.fxml"));
        Scene chat = new Scene(pai);
        
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        window.setTitle("Chat");
        window.setScene(chat);
        window.show();
        
        
    }
   
    
}
