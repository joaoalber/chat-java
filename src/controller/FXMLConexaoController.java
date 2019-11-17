package controller;

import bean.Cliente;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import static javafx.application.Platform.exit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.Principal;

public class FXMLConexaoController implements Initializable {

    @FXML
    private Label porta;

    @FXML
    private TextField portaTextField;

    @FXML
    private Label ip;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button conectarButton;

    @FXML
    void conectarAction(ActionEvent event) throws IOException {

        Cliente cliente = new Cliente(ipTextField.getText(), parseInt(portaTextField.getText()));

        if (cliente.getSocket() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erro");
            alert.setHeaderText("");
            alert.setContentText("Impossivel se conectar com servidor");

            alert.showAndWait();
        } else {
            //fechando tela de conexão
            Stage tela = (Stage) conectarButton.getScene().getWindow();
            tela.close();
            //chamar Nickname
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();
            FXMLLoginController.setCliente(cliente);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        portaTextField.setText("2424");
        ipTextField.setText("127.0.0.1");
    }

}
