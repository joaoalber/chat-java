/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import socketsthreads.Conexao;

/**
 * FXML Controller class
 *
 * @author cg1700421
 */
public class ServidorController {

    public ArrayList<Conexao> cnxLista = new ArrayList();
    public ArrayList<String> nomes = new ArrayList();
    public LoginController temp = new LoginController();  
    
    public void startarServidor() throws IOException {
        
        ServerSocket servidor = new ServerSocket(2424);
        ArrayList<Socket> clientes = new ArrayList();
        
 

        while (true) {
            System.out.println("ENTREI NESSA MERDA");
            Socket cliente = servidor.accept();
            System.out.println("SAI DESSE LIXO");
            clientes.add(cliente);

            Conexao cnx = new Conexao(cliente);
            
            cnxLista.add(cnx);
            
            System.out.println(cnxLista.size());
            
            temp.myConn(cnxLista);
            
            cnx.start();
            
           

        }

    }
    
   
    
    /* public void listar_usuarios() throws IOException {
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

    } */
    
    /* public void enviarMensagem(String[] usuarios, String mensagem) throws IOException {
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
    } */
    
}
