package socketsthreads;

import com.sun.javafx.util.TempState;
import controller.LoginController;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

public class Servidor {

    static ArrayList<Conexao> cnxLista = new ArrayList();
    static ArrayList<String> nomes = new ArrayList();
    static LoginController temp = new LoginController();
    
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(2424);
        ArrayList<Socket> clientes = new ArrayList();
        
        System.out.println("Porta 2424 aberta! Aguardando conexão...");

        while (true) {

            Socket cliente = servidor.accept();
            clientes.add(cliente);

            Conexao cnx = new Conexao(cliente);
            
            cnxLista.add(cnx);
            System.out.println(cnxLista.size());
            temp.myConn(cnxLista);
            
            cnx.start();
            
           

        }

    }

    public void listar_usuarios() throws IOException {
        for (int i = 0; i < cnxLista.size(); i++) {
            cnxLista.get(i).saida.print("lista_usuarios:");
            for (int j = 0; j < nomes.size(); j++) {
                if (nomes.size() - 1 == j) {
                    cnxLista.get(i).saida.println(nomes.get(j));

                } else {
                    cnxLista.get(i).saida.print(nomes.get(j) + ";");

                }
            }

        }

    }
    
    public void enviarMensagem(String[] usuarios, String mensagem) throws IOException {
        System.out.println(usuarios[0]);
        if (usuarios[0].equals("*")) {
            for (int i = 0; i < cnxLista.size(); i++) {
                cnxLista.get(i).saida.println(mensagem);
            }
        } else {
            for (int i = 0; i < cnxLista.size(); i++) {
                for (int j = 0; j < usuarios.length; j++) {
                    if (cnxLista.get(i).usuario.getNome().equals(usuarios[j])) {
                        cnxLista.get(i).saida.println(mensagem);
                    }
                }
            }
        }
    }
    
    
}
