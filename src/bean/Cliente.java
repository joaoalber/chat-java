package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private static Socket cliente;
    private static PrintWriter escritor;
    private static BufferedReader leitor;

    public Cliente(String ip, int porta) throws IOException {
        try {
            cliente = new Socket(ip, porta);
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escritor = new PrintWriter(cliente.getOutputStream(), true); // true autoflush(toda linha ele manda )

        } catch (UnknownHostException e) {
            System.err.println("O endereço passado é  inválido");
        } catch (IOException e) {
            System.err.println("Servidor pode estar fora do ar ");
        }
    }

    public static void main(String args[]) throws IOException {
        Cliente socket = new Cliente("127.0.0.1", 2424);

        //login do cliente
        logar();
        //mensagem

        //lendo mensagem do  servidor
        new Thread() {
            @Override
            public void run() {
                try {
                            
                    while (true) {
                        String mensagem = leitor.readLine();
                        System.out.println(mensagem);
                    }
                } catch (IOException ex) {
                    System.out.println("Impossível ler a mensagem do servidor");
                }
                        
            }
        }.start();

        //escrevendo para o servidor
        BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String mensagemTerminal = leitorTerminal.readLine();
            escritor.println(mensagemTerminal);
            if (mensagemTerminal.equalsIgnoreCase("SAIR")) {
                System.exit(0);
            }
        }

    }

    public Socket getSocket() {
        return cliente;
    }
    
    public void setSocket(Socket cliente) {
        this.cliente = cliente;
    }

    public static void logar() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Digite seu nickname:");
        String msg = in.nextLine();
        if (msg.startsWith("login:")) {
            escritor.println(msg);
        } else {
            escritor.println("login:" + msg);
        }
        String retorno = leitor.readLine();
        System.out.println(retorno);
        if(retorno.equals("login:false")){
            logar();
        }   
    }

}

