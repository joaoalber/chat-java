package controller;

import bean.Cliente;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FXMLChatController implements Initializable {

    private static Cliente cliente;
    private static String nickname;
    ObservableList<String> online;
    BufferedReader leitor;
    PrintStream escritor;
    private boolean x = true;
    private boolean banido;
    private Thread thread;
    private Thread thread2;
    private int contador;
    private boolean t2 = true;

    public static void setCliente(Cliente cliente) {
        FXMLChatController.cliente = cliente;
    }

    public static void setNickname(String nickname) {
        FXMLChatController.nickname = nickname;
    }

    public static String getNickname() {
        return FXMLChatController.nickname;
    }

    public static Cliente getCliente() {
        return FXMLChatController.cliente;
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
    private ImageView imageView;

    @FXML
    void enviarAction(ActionEvent event) throws IOException, InterruptedException {
        enviarButton.setText("Aguarde 3s..");
        enviarButton.setDisable(true);
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
            chatPane.appendText("você enviou " + mensagem + ":" + destino[destino.length - 1] + "\n");
        } else {
            escritor.println("mensagem:" + campoMensagem.getText());
            chatPane.appendText("você enviou " + campoMensagem.getText() + "\n");
        }

        banido = true;

    }

    public void mudarAvatar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")+"/src/imagens"));
        
        int result = fileChooser.showOpenDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File file = new File(selectedFile.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
    }

    public void retornarButton() throws InterruptedException {
        thread.sleep(5000);
        enviarButton.setDisable(false);
        Platform.runLater(() -> {
            enviarButton.setText("Enviar");
        }
        );

    }

    public void threadDois() {
        thread2 = new Thread(() -> {
            while (t2) {
                try {
                    thread2.sleep(1000);
                    if (banido) {
                        contador++;
                        if (contador > 7) {
                            t2 = false;
                            Platform.runLater(() -> {
                                JOptionPane.showMessageDialog(null, "Você foi banido");
                                Stage tela = (Stage) enviarButton.getScene().getWindow();
                                tela.close();
                                try {
                                    listar(leitor.readLine());
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLChatController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                            );

                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLChatController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        thread2.start();
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
        nicknameLabel.setText(nicknameLabel.getText() + nickname);
        thread = new Thread(() -> {
            try {
                while (x) {
                    banido = false;
                    contador = 0;
                    progressoBar.setProgress(-1.0F);
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
                    retornarButton();

                }

            } catch (IOException ex) {
                System.out.println(ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(FXMLChatController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        thread.start();
        threadDois();

    }

}
