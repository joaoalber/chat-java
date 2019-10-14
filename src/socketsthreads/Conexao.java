package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {

    String tmp = "";
    Autentication aut = new Autentication();
    Socket cliente;
    String nomeUser;
    boolean login;
    int tentativas = 0;

    Cliente usuario = new Cliente();
    Servidor servidor = new Servidor();

    Conexao(Socket conexao) {
        this.cliente = conexao;
    }

    // esse código é executando quando a thread é inicializada
    @Override
    public void run() {

        System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

        Scanner entrada = null;

        try {
            entrada = new Scanner(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintStream saida = null;

        try {
            saida = new PrintStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (!(aut.verificaNome(tmp))) {

                nomeUser = entrada.nextLine();
                tmp = nomeUser;


                while (existeNome(servidor.cnxLista)) {
                    saida.println("Nome já existente, favor digitar outro");

                    nomeUser = entrada.nextLine();
                    tmp = nomeUser;

                }

                if (aut.verificaNome(tmp)) {
                    usuario.setNome(nomeUser.substring(6, nomeUser.length()));
                    saida.println("Bem-vindo ao chat " + usuario.getNome());
                } else {
                    saida.println("Nome de usuário inválido. Tente 'login:' acompanhando do nome.");
                }

                if (estourouTentativas()) {
                    saida.println("Número de tentativas excedido... Desconectando...");
                    servidor.cnxLista.remove(servidor.cnxLista.size() - 1);
                    saida.close();
                    cliente.close();
                    System.out.println("Usuário " + cliente.getInetAddress().getHostAddress() + " desconectado. Motivo: número de tentativas excedido");
                    tentativas = 0;
                    break;
                }
                tentativas++;
            }
            
            
        } catch (IOException ex) {

        }

        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
        }

    }

    public boolean existeNome(ArrayList<Conexao> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            
            if (lista.get(lista.size() - 1).tmp.equals(lista.get(i).nomeUser)) {
                return true;
            }

        }

        return false;

    }

    public boolean estourouTentativas() {
        return tentativas == 3;
    }

}
