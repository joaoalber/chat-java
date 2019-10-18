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
    int tentativas = 0;
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

        try {

            while (!(aut.verificaNome(nomeUser))) {

                if (estourouTentativas()) {
                    desconectarTentativas();
                    break;
                }
                nomeUser = entrada.nextLine();
                while (existeNome(servidor.cnxLista)) {
                    if (estourouTentativas()) {
                        desconectarTentativas();
                        break;
                    }
                    tentativas++;
                    saida.println("Nome já existente, favor digitar outro");
                    nomeUser = entrada.nextLine();
                }
                tentativas++;
                if (aut.verificaNome(nomeUser)) {
                    bemvindo(usuario);
                    servidor.nomes.add(usuario.getNome());
                    servidor.listar_usuarios();
                    break;
                }
                saida.println("Nome de usuário inválido. Tente 'login:' acompanhando do nome.");

            }

        } catch (IOException ex) {

        }

        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            if (msg.equals("sair")) {
                try {
                    System.out.println("MEU NOME PARA PARAMETRO É: " + usuario.getNome());
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

    public boolean estourouTentativas() {
        return tentativas >= 3;
    }

    public void desconectar(int index) throws IOException {
        System.out.println("ENTREI PRA DESCONECTAR");
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
    
    public void desconectarTentativas() throws IOException {
        saida.println("Número de tentativas excedido... Desconectando...");
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
