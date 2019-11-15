package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {

    Socket cliente;
    String nomeUser = "";
    boolean login;
    PrintStream saida = null;

    public Cliente usuario = new Cliente();
    public Servidor servidor = new Servidor();

    ArrayList<String> nomes = new ArrayList<>();

    Conexao(Socket conexao) {
        this.cliente = conexao;
    }

    // esse código é executando quando a thread é inicializada
    @Override
    public void run() {

        System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

       

    }
    
    public void login(String nome) {
        usuario.setNome(nomeUser);
    }

    public boolean existeNome(ArrayList<Conexao> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {

            if (lista.get(lista.size() - 1).nomeUser.equals(lista.get(i).nomeUser)) {
                return true;
            }

        }

        return false;

    }

    public void desconectar(int index) throws IOException {
        servidor.cnxLista.remove(index);
        servidor.nomes.remove(index);
        System.out.println("Usuário " + cliente.getInetAddress().getHostAddress() + " desconectado.");
        saida.close();
        cliente.close();

    }

    public void removeByName(String nome) throws IOException {
        for (int i = 0; i < servidor.nomes.size(); i++) {
            if (servidor.nomes.get(i).equals(nome)) {
                desconectar(i);
            }
        }

    }

}
