/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author joaun
 */
public class LoginController {
    
    public void fazerLogin(ActionEvent event) {
        System.out.println("OIIIIi");
        URL som = getClass().getResource("/imagens/som.wav");
        AudioClip audio = Applet.newAudioClip(som);
        audio.play();
    }
    
}
