package bean;

import controller.FXMLChatController;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.*;

public class Servidor {

    private static String mensagem;
    private static final JTextArea errorLog = new JTextArea(30, 30);
    private static final JComboBox box = new JComboBox();
    private static final JPanel container = new JPanel();
    private static Map<String, Socket> clientesLogados = new HashMap<>();
    private static String nickname;
    private static PrintStream escritor;
    private static BufferedReader leitor;
    private static Socket cliente;
    private static String msg;
    
    public static void main(String args[]) throws IOException, InterruptedException {
        try {
            simple();
            ServerSocket servidor = new ServerSocket(2424);

            System.out.println("Aguardando...");

            while (true) {
                cliente = servidor.accept();

                escritor = new PrintStream(cliente.getOutputStream(), true);
                leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                mensagem = cliente.getLocalAddress() + " entrou";
                escrever();
                //login
                while (logar(cliente) == false) {
                    escritor.println("login:false");

                }

                box.addItem(msg);
                thread(cliente).start();

            }

        } catch (IOException e) {
            System.err.println("Porta inválida");
        }

    }

    public static void simple() {

        final JFrame jFrame = new JFrame("Server Log");

        final JScrollPane scroll = new JScrollPane(errorLog);

        jFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        jFrame.getContentPane().add(scroll);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(500, 620);
        JButton desconectar = new JButton("Desconectar usuário");
        desconectar.setBounds(50, 100, 95, 30);
        jFrame.add(desconectar);
        box.addItem("-- Escolha um usuário --");
        container.add(box);
        
        jFrame.getContentPane().add(container);

        desconectar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String x = String.valueOf(box.getSelectedItem());
                    box.removeItemAt(box.getSelectedIndex());

                    remover(x);
                    
                    
                    clientesLogados.values().forEach((socket) -> {
                        try {
                            escritor = new PrintStream(socket.getOutputStream(), true);
                            listar(escritor, cliente);
                        } catch (IOException ex1) {
                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    });
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
    
  
    public static void escrever() {
        String currentText = errorLog.getText();
        String newError = new Date() + " " + mensagem;
        String newTextToAppend = newError + "\n" + currentText;
        errorLog.setText(newTextToAppend);
    }

    public static void remover(String nickname) throws IOException {
        System.out.println("Tamanho do nome: " + nickname.length());
        System.out.println(clientesLogados);
        clientesLogados.remove(nickname);
        mensagem = nickname + " foi banido";
        escrever();

    }

    public static Thread thread(Socket cliente) throws InterruptedException {

        return new Thread() {
            @Override
            public void run() {
                while (clientesLogados.containsValue(cliente)) {
                    try {
                        sleep(1000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        
                        clientesLogados.keySet().stream().filter((remetente) -> (clientesLogados.get(remetente) == cliente)).forEachOrdered((remetente) -> {
                            nickname = remetente;
                        });
                        mensagem(cliente, nickname);
                        
                        
                    } catch (IOException ex) {
                        clientesLogados.remove(nickname);   
                        clientesLogados.values().forEach((socket) -> {
                            try {
                                escritor = new PrintStream(socket.getOutputStream(), true);
                                listar(escritor, cliente);
                            } catch (IOException ex1) {
                                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        });

                    }

                }

            }

        };
    }

    public static void mensagem(Socket cliente, String remetente) throws IOException {
        leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

        String msg = leitor.readLine();

        if (msg.startsWith("mensagem:")) {
            msg = msg.substring(9);
            String[] destinatario = msg.split(":");
            if (destinatario[0].equals("*")) {
                clientesLogados.keySet().forEach((destino) -> {
                    try {
                        escritor = new PrintStream(clientesLogados.get(destino).getOutputStream(), true);
                        transmitir(escritor, remetente, destinatario[0], destinatario[1]);

                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });
            } else {
                if (destinatario[0].contains(";")) {
                    String[] maisUsuario = destinatario[0].split(";");
                    for (String destino : maisUsuario) {
                        try {
                            escritor = new PrintStream(clientesLogados.get(destino).getOutputStream(), true);
                            transmitir(escritor, remetente, destinatario[0], destinatario[1]);
                        } catch (NullPointerException e) {
                            escritor = new PrintStream(clientesLogados.get(nickname).getOutputStream(), true);
                            escritor.println(destino + " não encontrado");
                        }
                    }
                } else {
                    try {
                        escritor = new PrintStream(clientesLogados.get(destinatario[0]).getOutputStream(), true);
                        transmitir(escritor, remetente, destinatario[0], destinatario[1]);
                    } catch (NullPointerException e) {
                        //System.out.println("O usuário " + nickname + " saiu ou foi banido");
                        escritor = new PrintStream(clientesLogados.get(nickname).getOutputStream(), true);
                        escritor.println(destinatario[0] + " não encontrado");
                    }
                }

            }
        } else if (msg.trim().equalsIgnoreCase("sair")) {
            clientesLogados.remove(nickname);
            mensagem = nickname + " saiu";
            for (Socket socket : clientesLogados.values()) {
                escritor = new PrintStream(socket.getOutputStream(), true);
                listar(escritor, cliente);
            }
            escrever();
        } else {
            escritor.println("comando não reconhecido");
        }
    }

    public static void transmitir(PrintStream escritor, String remetente, String destinatario, String mensagem) {
        escritor.println("transmitir:" + remetente + ":" + destinatario + ":" + mensagem);
    }

    public static void listar(PrintStream escritor, Socket pedinte) throws IOException {
        //listar usuarios
        String listaUsuario = "lista_usuarios:";

        listaUsuario = clientesLogados.keySet().stream().map((usuarios) -> usuarios + ";").reduce(listaUsuario, String::concat);

        escritor.println(listaUsuario);

    }

    public static boolean logar(Socket cliente) throws IOException {
        msg = leitor.readLine();
        if (msg.startsWith("login:")) {
            msg = msg.substring(6);
            msg = msg.replace("login:", "");

            msg = msg.trim();
            if (msg.isEmpty() || Pattern.compile("[^a-zA-Z1-9 ]").matcher(msg).find()) {
                return false;

            } else {

                for (String nome : clientesLogados.keySet()) {
                    if (nome.equalsIgnoreCase(msg)) {
                        return false;

                    }
                }
                clientesLogados.put(msg, cliente);
                escritor.println("login:true");

                for (Socket socket : clientesLogados.values()) {
                    escritor = new PrintStream(socket.getOutputStream(), true);
                    listar(escritor, cliente);
                }
            }
        }
        return true;

    }
}
