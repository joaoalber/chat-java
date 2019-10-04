package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

public class Servidor {

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(2424);
        ArrayList<Socket> clientes = new ArrayList();
        
        System.out.println("Porta 2424 aberta! Aguardando conexão...");

        while (true) {

            Socket cliente = servidor.accept();
            clientes.add(cliente);
            Conexao cnx = new Conexao(cliente);

            cnx.start();

        }

    }
}
