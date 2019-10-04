package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao extends Thread{
    
    Socket cliente;
    String nomeUser;
    boolean login;
    
    Cliente usuario = new Cliente();
    
    Conexao(Socket conexao){
        this.cliente = conexao;
    }
    
    // esse código é executando quando a thread é inicializada
    @Override
    public void run(){
     
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
        
        Scanner teclado = new Scanner(System.in);
       
        nomeUser = entrada.nextLine();
        String tmp = nomeUser;
        if ((tmp.substring(0,6)).equals("login:")) {
            
            usuario.setNome(nomeUser.substring(6, nomeUser.length()));
            System.out.println(usuario.getNome());
    
        }
        
        while (entrada.hasNextLine()) {
            String msg = entrada.nextLine();
            
            
        }
        
    }
}
