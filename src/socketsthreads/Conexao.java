package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread {

    Autentication aut = new Autentication();
    Socket cliente;
    String nomeUser = "";
    boolean login;
    PrintStream saida = null;

    Cliente usuario = new Cliente();
    Servidor servidor = new Servidor();

    ArrayList<String> nomes = new ArrayList<>();

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

        try {
            saida = new PrintStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }



            while (!(aut.verificaNome(nomeUser))) {
                nomeUser = entrada.nextLine();
                while (existeNome(servidor.cnxLista)) {
                    saida.println("login:false");
                    nomeUser = entrada.nextLine();
                }

                if (aut.verificaNome(nomeUser)) {
                    saida.println("login:true");
                    usuario.setNome(nomeUser.substring(6, nomeUser.length()));
                    servidor.nomes.add(usuario.getNome());
                    servidor.listar_usuarios();
                    break;
                } else {
                    saida.println("login:false");
                }

            }

        
        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            if (msg.equals("sair")) {
                try {
                    removeByName(usuario.getNome());
                    servidor.listar_usuarios();
                } catch (IOException ex) {
                    Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

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

    public void bemvindo(Cliente nome) {
        nome.setNome(nomeUser.substring(6, nomeUser.length()));
        saida.println("Bem-vindo ao chat " + usuario.getNome());
    }

    /*public void desconectarTentativas() throws IOException {
     saida.println("Número de tentativas excedido... Desconectando...");
     System.out.println("Usuário " + cliente.getInetAddress().getHostAddress() + " desconectado.");
     saida.close();
     cliente.close();
     }*/
    public void removeByName(String nome) throws IOException {
        for (int i = 0; i < servidor.nomes.size(); i++) {
            if (servidor.nomes.get(i).equals(nome)) {
                desconectar(i);
            }
        }

    }

}
