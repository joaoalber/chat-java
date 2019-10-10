package socketsthreads;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Cliente {

    String nome;

    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket cliente = new Socket("127.0.0.1", 2424);

        System.out.println("O cliente se conectou ao servidor!");
        System.out.println("Bem-vindo, tente 'login:' acompanhando do nome para entrar no chat.");

        Scanner teclado = new Scanner(System.in);

        PrintStream saida = new PrintStream(cliente.getOutputStream());
        Scanner entrada = new Scanner(cliente.getInputStream());
        try {
            while (teclado.hasNextLine()) {

                saida.println(teclado.nextLine());
                System.out.println(entrada.nextLine());

            }
        } catch (NoSuchElementException ex) {

        }

        saida.close();
        teclado.close();

    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
