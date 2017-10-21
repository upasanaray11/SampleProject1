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
public class Login {
    
    private String emailAddress;
    private String password;
    boolean idFound = false;
    
    private void getAllDetails(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the email address");
        emailAddress = input.nextLine();
        
        System.out.println("Enter the password");
        password = input.nextLine();
    }
    
    public void loginUser(){
        
        this.getAllDetails();
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/raythathau7484";
        
        //three important classses
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = DriverManager.getConnection(DB_URL,"raythathau7484","1570501");
            st = conn.createStatement();
            String query = "Select * from EMAIL_LOGIN_DETAILS where email_address = '"+ this.emailAddress + "'";
            rs = st.executeQuery(query);
            if(rs.next()){
                //email id is found
               
               //check password
               if(this.password.equals(rs.getString(3))){
                   //we need to create the obj of online account by using the info of the result
                   //OnlineAccount theloginAccount = new OnlineAccount(rs.getString(2), id,password);
                   //theLoginAccount.welcome();
                   System.out.println("Login successful! Welcome "+rs.getString(2));
               }
               else{
                   System.err.println("Password incorrect");
               }        
            }
            else{
                System.err.println("Username doesn't exist");
            }
        }
        catch(SQLException e){
            System.err.println("Something is wrong with DB connection");
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
    }
    
}
