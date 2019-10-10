package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {

    Autentication aut = new Autentication();
    Socket cliente;
    String nomeUser;
    boolean login;
    int tentativas = 0;

    Cliente usuario = new Cliente();

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

        String tmp = "";

        try {
            while (!(aut.verificaNome(tmp))) {
                tentativas++;
                nomeUser = entrada.nextLine();
                tmp = nomeUser;
                if (tentativas == 3) {
                    saida.println("Número de tentativas excedido... Desconectando...");
                    saida.close();
                    cliente.close();
                    System.out.println("Usuário " + cliente.getLocalAddress() + " desconectado. Motivo: número de tentativas excedido");
                    break;
                }

                if (aut.verificaNome(tmp)) {
                    usuario.setNome(nomeUser.substring(6, nomeUser.length()));
                    saida.println(usuario.getNome());
                    break;
                }
                saida.println("Nome de usuário inválido. Tente 'login:' acompanhando do nome.");
            }
        } catch (IOException ex) {

        }

        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();;
        }

    }
    
}
