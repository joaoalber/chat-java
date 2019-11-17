package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Servidor {

    private static Map<String, Socket> clientesLogados = new HashMap<>();
    private static String nickname;
    private static PrintStream escritor;
    private static BufferedReader leitor;

    public static void main(String args[]) throws IOException, InterruptedException {

        try {
            ServerSocket servidor = new ServerSocket(2424);

            System.out.println("Aguardando...");

            while (true) {
                Socket cliente = servidor.accept();
                escritor = new PrintStream(cliente.getOutputStream(), true);
                leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                System.out.println(cliente.getLocalAddress() + " entrou");

                //login
                while (logar(cliente) == false) {
                    escritor.println("login:false");

                }

                thread(cliente).start();

            }

        } catch (IOException e) {
            System.err.println("Porta inválida");
        }

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
                        mensagem(cliente,nickname);
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

    public static void mensagem(Socket cliente,String remetente) throws IOException {
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
                        escritor = new PrintStream(clientesLogados.get(nickname).getOutputStream(), true);

                        escritor.println(destinatario[0] + " não encontrado");
                    }
                }

            }
        } else if (msg.trim().equalsIgnoreCase("sair")) {
            clientesLogados.remove(nickname);
            System.err.println(nickname + " saiu");
            for (Socket socket : clientesLogados.values()) {
                escritor = new PrintStream(socket.getOutputStream(), true);
                listar(escritor, cliente);
            }

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
        String msg = leitor.readLine();
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
