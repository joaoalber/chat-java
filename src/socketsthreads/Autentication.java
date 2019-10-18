/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketsthreads;

import java.util.ArrayList;

/**
 *
 * @author cg3000401
 */
public class Autentication {

    public boolean verificaNome(String nome) {
        if (nome.length() < 6) {
            return false;
        }
        return nome.substring(0, 6).equals("login:") ? true : false;
    }
    
    


}
