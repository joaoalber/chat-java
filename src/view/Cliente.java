package view;

import java.net.Socket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author joaunQ
 */
public class Cliente extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Socket cliente = new Socket("127.0.0.1", 2424);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/view/Main.css");
        stage.setScene(scene);
        stage.getIcons().add(new Image("/imagens/icon.png"));
        stage.setTitle("Tela de Login");
        stage.setResizable(false);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
