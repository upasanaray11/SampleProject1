/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monproject1upasana;

import java.util.ArrayList;
import java.util.Scanner;
import monproject1upasana.email.EmailContent;
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
        
        Scanner input = new Scanner(System.in);
        String selection = "";
        
        //while loop to display menu
        while(!selection.equals("x"))
        {
            //show the menu
            System.out.println();
            System.out.println("Please make your selection");
            System.out.println("1: Register User");
            System.out.println("2: Login to email account");
            System.out.println("x: Finish the simulation");
            
            //get the input
            selection = input.nextLine();
            System.out.println();
            
            if(selection.equals("1")){
              Registration r = new Registration();
              r.registerUser();
            }
            if(selection.equals("2")){
                afterLoginMenu();
            }
            
        }
    }
    private static void afterLoginMenu(){
       Login l= new Login();
       String userID = l.loginUser();
              
       if(userID == null){
           return;
       }
       Scanner input = new Scanner(System.in);
       String selection = "";
       EmailSenderReader e = new EmailSenderReader(userID);
       
       while(!selection.equals("x")){
        System.out.println("Welcome "+userID);
        System.out.println("Please make your selection");
        System.out.println("1: View emails");
        System.out.println("2: Write email");
        System.out.println("3: View sent emails");
        System.out.println("x: Exit");
        
        selection = input.nextLine();
        System.out.println();
        
        if(selection.equals("1")){
           ArrayList<EmailContent> emailContentList = e.retrieveReceivedEmail(userID, false);
           e.printEmailContent(emailContentList);
            System.out.println("Please enter the email you want to view: ");
           int indexOfEmail = input.nextInt();
           EmailContent emailContent = e.viewEmailOfGivenIndex(emailContentList, indexOfEmail-1, false);
           viewEmailMenu(emailContent);
        }
        
        if(selection.equals("2")){
           e.sendEmail();
        }
        if(selection.equals("3")){
           sentEmailMenu(userID);
        }
            
       }
    }
    
    private static void sentEmailMenu(String userID){
        EmailSenderReader e = new EmailSenderReader(userID);
        ArrayList<EmailContent> emailContentList = e.retrieveReceivedEmail(userID, true);
        if(emailContentList == null ||  emailContentList.isEmpty()){
            System.out.println("No sent emails");
            return;
        }
        e.printEmailContent(emailContentList);
        Scanner input = new Scanner(System.in);
        String selection = "";
        
        while(!selection.equals("x")){
            System.out.println("Please make your selection");
            System.out.println("1: View email");
            System.out.println("x: Go back to main menu");
        
            selection = input.nextLine();
            if(selection.equals("1")){
                System.out.println("Please enter the index of the email you want to view: ");
                int indexOfEmail = input.nextInt();
                e.viewEmailOfGivenIndex(emailContentList, indexOfEmail-1,true);
            }
        }
    }
    
    private static void viewEmailMenu(EmailContent emailContent){
        Scanner input = new Scanner(System.in);
        String selection = "";
        EmailSenderReader e = new EmailSenderReader(emailContent.getRecipient());
        
        while(!selection.equals("x")){
            System.out.println("Please make your selection");
            System.out.println("1: Reply to email");
            System.out.println("x: Go back to main menu");
        
            selection = input.nextLine();
            if(selection.equals("1")){
                System.out.println("Please enter the Content of email: ");
                String content = input.nextLine();
                EmailContent emailContent1 = new EmailContent();
                emailContent1.setContent(content);
                emailContent1.setTitle("Re: "+emailContent.getTitle());
                emailContent1.setReplyID(emailContent.getOriginalID());
                emailContent1.setIsNew(true);
                emailContent1.setSender(emailContent.getRecipient());
                emailContent1.setRecipient(emailContent.getSender());
                e.insertEmailToDB(emailContent1);
                break;
            }
        }
    }
}
