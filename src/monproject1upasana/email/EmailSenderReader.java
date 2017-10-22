/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monproject1upasana.email;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author UpasanaK
 */
public class EmailSenderReader {

    public EmailSenderReader(String senderID) {
        this.senderID = senderID;
    }
    private String senderID;
    private String senderName;
    
    public boolean sendEmail(){
        
        String recipientID;
        String emailTitle;
        String emailContent;
        String senderName ="abc";
        
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
              
        Scanner input = new Scanner(System.in);
        
        System.out.println("Please enter the recipient you want to sent to: ");
        recipientID = input.nextLine();
        
        System.out.println("Please enter the title of the email: ");
        emailTitle = input.nextLine();
        
        System.out.println("Please enter the content: ");
        emailContent = input.nextLine();
        
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query = "Select * from EMAIL_LOGIN_DETAILS where email_address = '"+ recipientID + "'";
            rs = st.executeQuery(query);
            if(rs.next()){
                int t = st.executeUpdate("Insert into EMAIL_CONTENT(RECIPIENT,TITLE,CONTENT,SENDER,SENDER_NAME,DATE) values ('"+ recipientID + "', '" + emailTitle + "', '"+ emailContent+ "', '"+ senderID+"', '"+ senderName+"', '"+ currentTime+"')");
            }
            else{
                System.err.println("Recipient emailId doesn't exist");
            }
    }
        catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }
    
        public void retrieveReceivedEmail(String id,boolean isSentEmail){
        
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query;
            if(isSentEmail){
               query = "Select * from EMAIL_CONTENT where SENDER = '"+ id + "'"; 
            }
            else{
                query = "Select * from EMAIL_CONTENT where RECIPIENT = '"+ id + "'";
            }
            
            rs = st.executeQuery(query);
            ArrayList<EmailContent> emailContentList = this.convertResultSetToEmailContent(rs);
            this.printEmailContent(emailContentList);
            
    }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
       
    private ArrayList<EmailContent> convertResultSetToEmailContent(ResultSet rs){
        ArrayList<EmailContent> emailContentList = new ArrayList<>();
         
        try{
            while(rs.next()){
             EmailContent e = new EmailContent();
             e.setDate(rs.getString(1));
             e.setSender(rs.getString(2));
             e.setSenderName(rs.getString(3));
             e.setRecipient(rs.getString(4));
             e.setRecipientName(rs.getString(5));
             e.setTitle(rs.getString(6));
             e.setContent(rs.getString(7));
             e.setIsNew(rs.getBoolean(8));
             e.setOriginalID(rs.getString(9));
             e.setReplyID(rs.getString(10));
             emailContentList.add(e);
         
         }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return emailContentList;
    }
    
    public void printEmailContent(ArrayList<EmailContent> emailContentList){
        for(EmailContent e : emailContentList){
            System.out.println(e.getDate()+","+e.getTitle()+","+e.getContent());
        }
    }
}
