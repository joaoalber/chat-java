package controller;
import bean.Cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FXMLChatController implements Initializable {

    private static Cliente cliente;
    private static String nickname; 
    ObservableList<String> online;
    BufferedReader leitor;
    PrintStream escritor;

    public static void setCliente(Cliente cliente) {
        FXMLChatController.cliente = cliente;
    }

    public static void setNickname(String nickname) {
        FXMLChatController.nickname = nickname;
    }
    

    @FXML
    private ListView<String> listaCliente;

    @FXML
    private ProgressBar progressoBar;

    @FXML
    private Button enviarButton;

    @FXML
    private TextArea campoMensagem;
    @FXML
    private Label nicknameLabel = new Label();
    @FXML
    private TextArea chatPane;

    @FXML
    void enviarAction(ActionEvent event) throws IOException {
        
        escritor = new PrintStream(cliente.getSocket().getOutputStream(), true);
        
        if (campoMensagem.getText().trim().equalsIgnoreCase("sair")) {
           
            escritor.println("sair");
                   Stage tela = (Stage) enviarButton.getScene().getWindow();
            tela.close();
             listar(leitor.readLine());
            
        }

        String[] destino = campoMensagem.getText().split(":");
        if (destino.length > 2) {
            String mensagem = "";
            for (int i = 0; i < destino.length - 1; i++) {
                mensagem += destino[i] + ";";
            }
            escritor.println("mensagem:" + mensagem + ":" + destino[destino.length - 1]);
            chatPane.appendText("você enviou para " + mensagem + ":" + destino[destino.length - 1] + "\n");
        } else {
            escritor.println("mensagem:" + campoMensagem.getText());
            chatPane.appendText("você enviou para " + campoMensagem.getText() + "\n");
        }

    }

    public void listar(String msg) {
        msg = msg.trim();
        msg = msg.substring(14);
        msg = msg.replaceAll(":", "");
        
        String[] usuarios = msg.split(";");
       
        online = FXCollections.observableArrayList(usuarios);
        online.remove(nickname);
    }

    public FXMLChatController() throws IOException {
        this.leitor = new BufferedReader(new InputStreamReader(cliente.getSocket().getInputStream()));
    }

    @FXML
    public void listMouseEvent(MouseEvent event) {
        ObservableList<String> itemsSelecionados = listaCliente.getSelectionModel().getSelectedItems();
        if (!itemsSelecionados.isEmpty()) {
            String destinatarios = "";
            destinatarios = itemsSelecionados.stream().map((destino) -> destino + ":").reduce(destinatarios, String::concat);
            campoMensagem.setText(destinatarios);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nicknameLabel.setText(nicknameLabel.getText() +  nickname);
        new Thread(() -> {
            try {
                while (true) {
                    progressoBar.setProgress(-1.0F);
                    sleep(1000);
                    String msg = leitor.readLine();
                    if (msg.startsWith("lista_usuarios")) {
                        listar(msg);
                        progressoBar.setVisible(false);
                        Platform.runLater(() -> listaCliente.setItems(online));
                        listaCliente.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    }

                    if (msg.startsWith("transmitir:")) {
                        msg = msg.substring(11);
                        chatPane.appendText(msg + "\n");
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLChatController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLChatController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();

    }

}
