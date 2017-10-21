/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monproject1upasana.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author UpasanaK
 */
public class Registration {
    
    private String emailAddress;
    private String name;
    private String password;
    
    
    /*private boolean isConfirmPasswordSame(String password,String confirmPassword){
        return false;
    }*/
    
    private boolean getAllDetails(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the email address");
        emailAddress = input.nextLine();
        
        System.out.println("Enter the name");
        name = input.nextLine();
        
        System.out.println("Enter the password");
        password = input.nextLine();
        
        if(!this.validateEmailAddress()){
            System.err.println("Your email address should contain alphanumeric characters!");
            return false;
        }
        return true;
    }
    
    public void registerUser(){
        if(!this.getAllDetails()){
            return;
        }
        //this.getAllDetails();
        //insert the user in the database
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        //three important classses
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            //connect to the databse
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            //create statement object 
            st = conn.createStatement();
            //do a query
            rs = st.executeQuery("Select * from EMAIL_LOGIN_DETAILS where email_address = '"+ this.emailAddress+"'");
            //if rs contains any record ,err msg
            //else insert a new record into email_login_details table with (email id, name,password)
            if(rs.next()){
                System.err.println("Account creation failed because email address is already used,please try new one.");
            }
            else{
                int t= st.executeUpdate("Insert into EMAIL_LOGIN_DETAILS values ('"+ this.emailAddress + "', '" + this.name + "', '"+ this.password+ "')");
                System.out.println("Account creation successfull");
            }
            
        }
        catch(SQLException e){
            
            e.printStackTrace(); //important for troubleshooting
        }
        finally{
            //close the db
            try{
                conn.close();
                st.close();
                rs.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private boolean validateEmailAddress(){
       //this.emailAddress.matches(".*\\d.*");
       return this.emailAddress.matches("[A-Za-z0-9]+");
      /* boolean hasNumber = this.emailAddress.contains("[0-9]+");
       boolean hasCharacter = this.emailAddress.contains("[A-Z]")||this.emailAddress.contains("[a-z]");*/
       //return hasNumber && hasCharacter;
    }
}
