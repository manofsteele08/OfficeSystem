/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package office;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Chris
 */
public class DBConnect {
    
    public DBConnect(){
        
    }
    
    public Connection getConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            
            Connection con = DriverManager.getConnection("jdbc:derby:D:/Documents/Google Drive/Office Program (JAVA) v.2 .1/OfficeDB/DB;create=true"); //Laptop Location
            //Connection con = DriverManager.getConnection("jdbc:derby:G:/Chris/Google Drive/Office Program (JAVA) v.2 .1/OfficeDB/DB;create=true"); //My Location
            //Connection con = DriverManager.getConnection("jdbc:derby:S:/Housing/ResLife/ProStaff/Area Administration/FASA/Office Checkout Program - DO NOT PLAY/OfficeDB/DB;create=true"); //FASA Location
            return con;

        } catch(Exception e){
            JOptionPane optionPane = new JOptionPane(e.toString());
            JDialog dialog = optionPane.createDialog("Error!");
            dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
            dialog.setVisible(true);
            return null;
        }
    }

    private boolean isAlwaysOnTopSupported() {
        throw new UnsupportedOperationException("Connection Error."); //To change body of generated methods, choose Tools | Templates.
    }
}
