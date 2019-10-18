package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

public class Servidor {

    static ArrayList<Conexao> cnxLista = new ArrayList();
    static ArrayList<String> nomes = new ArrayList();

    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(2424);
        ArrayList<Socket> clientes = new ArrayList();
        Autentication aut = new Autentication();

        System.out.println("Porta 2424 aberta! Aguardando conexão...");

        while (true) {

            Socket cliente = servidor.accept();
            clientes.add(cliente);

            Conexao cnx = new Conexao(cliente);

            cnxLista.add(cnx);

            cnx.start();

        }

    }

    public static void listar_usuarios() {
        for (int i = 0; i < cnxLista.size(); i++) {
            cnxLista.get(i).saida.print("lista_usuarios:");
            for (int j = 0; j < nomes.size(); j++) {
                if (nomes.size() - 1 == j) {
                    cnxLista.get(i).saida.println(nomes.get(j));
                    
                   
                } else {
                    cnxLista.get(i).saida.print(nomes.get(j) + ";");
                   
                }
            }
            //cnxLista.get(i).saida.println();

        }

    }
            

}
