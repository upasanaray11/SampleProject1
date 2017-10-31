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
        String content;
        String senderName ="abc";
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the recipient you want to sent to: ");
        recipientID = input.nextLine();
        
        System.out.println("Please enter the title of the email: ");
        emailTitle = input.nextLine();
        
        System.out.println("Please enter the content: ");
        content = input.nextLine();
        
        EmailContent emailContent = new EmailContent();
        emailContent.setContent(content);
        emailContent.setRecipient(recipientID);
        emailContent.setTitle(emailTitle);
        emailContent.setSender(this.senderID);
        return insertEmailToDB(emailContent);
       
    }
    
    public boolean insertEmailToDB(EmailContent emailContent){
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query = "Select * from EMAIL_LOGIN_DETAILS where email_address = '"+ emailContent.getRecipient() + "'";
            rs = st.executeQuery(query);
            if(rs.next()){
                boolean t = st.execute("Insert into EMAIL_CONTENT(RECIPIENT,TITLE,CONTENT,SENDER,SENDER_NAME,DATE,REPLY_ID,IS_NEW) values ('"+ emailContent.getRecipient() + "', '" + emailContent.getTitle() + "', '"+ emailContent.getContent()+ "', '"+ emailContent.getSender()+"', '"+ emailContent.getSenderName()+"', '"+ currentTime+"', "+ emailContent.getReplyID()+", "+true+")");
                System.out.println("Email sent successfuly");
            }
            else{
                System.err.println("Recipient emailId doesn't exist");
                return false;
            }
    }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        finally{
            try{
                conn.close();
                st.close();
                rs.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        
    }
        return true;
    }
    
    public ArrayList<EmailContent> retrieveReceivedEmail(String id,boolean isSentEmail){
        
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
            return emailContentList;
            
    }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
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
        int index=1;
        for(EmailContent e : emailContentList){
            if(!e.isIsNew()){
                System.out.println(index +". "+e.getDate()+","+e.getTitle());
            }
            else{
                System.out.println(index +". "+e.getDate()+","+e.getTitle()+" (new)");
            }
            index++;
        }        
    }
    
    public EmailContent viewEmailOfGivenIndex(ArrayList<EmailContent> emailContentList, int i, boolean isSentFolder){
        if(i>emailContentList.size()){
            System.out.println("Index provided to view email is incorrect");
            return null;
        }
        EmailContent emailContent = emailContentList.get(i);
        if(emailContent.isIsNew()){
            this.markMailAsRead(emailContent);
        }
        if(isSentFolder || emailContent.getReplyID() == null){
            System.out.println("Date: "+emailContent.getDate());
            System.out.println("Title: "+emailContent.getTitle());
            System.out.println("Content: ");
            System.out.println(emailContent.getContent());
            System.out.println();
            return emailContent;
        }
        EmailContent originalEmail = null;
        
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query = "Select * from EMAIL_CONTENT where ID = "+emailContent.getReplyID();
            rs = st.executeQuery(query);
            originalEmail = this.convertResultSetToEmailContent(rs).get(0);
            }
        catch(SQLException e){
            e.printStackTrace();
        }
        
        if(originalEmail == null && emailContent.getReplyID() != null){
            System.out.println("Something wrong in fectching the reply email.");
            return null;
        }
        
            System.out.println("Date: "+emailContent.getDate());
            System.out.println("Title: "+emailContent.getTitle());
            System.out.println("Content: ");
            System.out.println(emailContent.getContent());
            System.out.println();
            System.out.println("------------------------------------------------");
            System.out.println("Date: "+originalEmail.getDate());
            System.out.println("Title: "+originalEmail.getTitle());
            System.out.println("Content: ");
            System.out.println(originalEmail.getContent());
            System.out.println();
            return emailContent;
    
    }

    private void markMailAsRead(EmailContent emailContent) {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query = "Update EMAIL_CONTENT set IS_NEW ="+false+" where ID = "+emailContent.getOriginalID();
            int t = st.executeUpdate(query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
