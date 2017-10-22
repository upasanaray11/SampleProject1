/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monproject1upasana;

import monproject1upasana.email.EmailSenderReader;
import monproject1upasana.login.Login;
import monproject1upasana.login.Registration;

/**
 *
 * @author UpasanaK
 */
public class MonProject1Upasana {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Registration r = new Registration();
        Login l= new Login();
       //r.registerUser();
        //l.loginUser();
        EmailSenderReader e = new EmailSenderReader("upasanau1");
        e.sendEmail();
    }
    
}
