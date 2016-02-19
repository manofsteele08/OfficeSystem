/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package office;

import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Chris
 */
public class CheckOut extends JFrame implements  ActionListener{

    DBConnect dbCon = new DBConnect(); //Connects to the Database
    
    private final int WIDTH = 300, HEIGHT = 480;
    JButton done, cancel, addCheckout;
    JComboBox boxList = new JComboBox();
    JTextField fieldFName, fieldLName, fieldANum, fieldAPT, fieldPhone, fieldEmail, fieldItemDes, fieldAmount;
    JFrame outFrame = new JFrame("Check Out Item");
    MyDateTime DT = new MyDateTime();
    String staffName, dueDate, checkDate;
    ArrayList<String> itemList = new ArrayList<String>();
    ArrayList<String> checkoutList = new ArrayList<String>();
    MyDateTime CurrentDate = new MyDateTime();
    JCheckBox nonReturnItem = new JCheckBox();

    public CheckOut(){

        done = new JButton("Done");
        cancel = new JButton("Cancel");
        addCheckout = new JButton("Additional Checkout");
        nonReturnItem = new JCheckBox("Non-Checkout Item");

        done.setSize(75,30);
        cancel.setSize(75,30);
        addCheckout.setSize(150,30);
        nonReturnItem.setSize(150,30);

        done.setLocation(50,405);
        cancel.setLocation(175,405);
        addCheckout.setLocation(10,365);
        nonReturnItem.setLocation(160,365);

        done.addActionListener(this);
        cancel.addActionListener(this);
        addCheckout.addActionListener(this);

        outFrame.setSize(WIDTH, HEIGHT);
        outFrame.setLocationRelativeTo(null);
        outFrame.setLayout(null);

         try{
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT item FROM items ORDER BY UPPER(item)");
            
            while(rs.next()){
                boxList.addItem(rs.getString(1));
            }
            
            // Closes the Connection
            rs.close();
            con.close();
        } catch(Exception e){
            System.out.print(e);
            e.printStackTrace();
            JOptionPane optionPane = new JOptionPane(e.toString());
            JDialog dialog = optionPane.createDialog("Error!");
            dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
            dialog.setVisible(true);
        }

        String[] labels = {"First Name: ", "Last Name: ", "A#: ", "APT: ","Phone: ", "Email: ", "Item Description: ", "Amount(optional): $"};
        int numPairs = labels.length;

        fieldFName = new JTextField();
        fieldLName = new JTextField();
        fieldANum = new JTextField();
        fieldAPT = new JTextField();
        fieldPhone = new JTextField();
        fieldEmail = new JTextField();
        fieldItemDes = new JTextField();
        fieldAmount = new JTextField("0.00");         

        fieldFName.setSize(170,30);
        fieldLName.setSize(170,30);
        fieldANum.setSize(170,30);
        fieldAPT.setSize(170,30);
        fieldPhone.setSize(170,30);
        fieldEmail.setSize(170,30);
        fieldItemDes.setSize(170,30);
        fieldAmount.setSize(100,30);

        fieldFName.setLocation(120,5);
        fieldLName.setLocation(120,45);
        fieldANum.setLocation(120, 85);
        fieldAPT.setLocation(120,125);
        fieldPhone.setLocation(120,165);
        fieldEmail.setLocation(120,205);
        fieldItemDes.setLocation(120,245);
        fieldAmount.setLocation(120,285);

        //Create and populate the panel.
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            l.setSize(110,30);
            l.setLocation(10, i * 40 + 5);
            outFrame.add(l);
        }

        fieldAmount.setToolTipText("Amount for parking passes, lockouts, prints, etc.");

        boxList.setSize(200,30);
        boxList.setLocation(50,325);
        outFrame.add(boxList);
        outFrame.add(done);
        outFrame.add(cancel);
        outFrame.add(addCheckout);
        outFrame.add(fieldFName);
        outFrame.add(fieldLName);
        outFrame.add(fieldANum);
        outFrame.add(fieldAPT);
        outFrame.add(fieldPhone);
        outFrame.add(fieldEmail);
        outFrame.add(fieldItemDes);
        outFrame.add(fieldAmount);
        outFrame.add(nonReturnItem);
    }

    public void getStaffName(String name){
        staffName = name;
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == cancel){

            fieldFName.setText("");
            fieldLName.setText("");
            fieldANum.setText("");
            fieldAPT.setText("");
            fieldPhone.setText("");
            fieldEmail.setText("");
            fieldItemDes.setText("");
            fieldAmount.setText("0.00");

            outFrame.dispose();
        }

        if(ae.getSource() == done){  
            
            ExecuteCommand();
            
            fieldFName.setText("");
            fieldLName.setText("");
            fieldANum.setText("");
            fieldAPT.setText("");
            fieldPhone.setText("");
            fieldEmail.setText("");
            fieldItemDes.setText("");
            fieldAmount.setText("0.00");
            
            outFrame.dispose();
        }
        
        if(ae.getSource() == addCheckout){
            ExecuteCommand();
        }
    }
    
    public void ExecuteCommand(){
        String query = "";
        int days = 0;
        String returnStatus = "";
        
        try{
            Connection con = dbCon.getConnection();
            String item = boxList.getSelectedItem().toString();
            PreparedStatement stmt = con.prepareStatement("SELECT time_days FROM items WHERE item=?");
            stmt.setString(1, item);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            days = rs.getInt(1);

            // Closes the Connection
            rs.close();
            con.close();
        } catch(Exception e){
            System.out.print(e);
            e.printStackTrace();
            //outFrame.dispose();
            JOptionPane optionPane = new JOptionPane(e.toString());
            JDialog dialog = optionPane.createDialog("Error!");
            dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
            dialog.setVisible(true);
        }
       
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);              

        cal.add(Calendar.DAY_OF_MONTH, days);

        dueDate = year + "-" + (month+1) + "-" + day;
        checkDate = day + "/" + (month+1) + "/" + year;
        
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date MyDate = newDateFormat.parse(checkDate);
            newDateFormat.applyPattern("EEE");
            String MyNewDate = newDateFormat.format(MyDate);
            //System.out.println(MyNewDate);
        } catch (ParseException ex) {
            Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        if(nonReturnItem.isSelected()){
            returnStatus = "YES";
        } else {
            returnStatus = "NO";
        }
        
        query = "INSERT INTO CHECKOUTS (CHECKOUTDATE,STAFF, FNAME, LNAME, ANUM, APT, PHONE, EMAIL, ITEM, DESCRIPTION, DUEDATE, RETURNED, AMOUNTOWE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        String varDate = CurrentDate.getDate();
        String varName = staffName;
        String varFName = fieldFName.getText();
        String varLName = fieldLName.getText();
        String varANum = fieldANum.getText();
        String varAPT = fieldAPT.getText();
        String varPhone = fieldPhone.getText();
        String varEmail = fieldEmail.getText();
        String varItem = boxList.getSelectedItem().toString();
        String varItemDes = fieldItemDes.getText();
        String varDueDate = dueDate;
        String varReturn = returnStatus;
        String varAmount = fieldAmount.getText();
        
        System.out.println(query);

        try{
            Connection con = dbCon.getConnection();
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, varDate);
            stmt.setString(2, varName);
            stmt.setString(3, varFName);
            stmt.setString(4, varLName);
            stmt.setString(5, varANum);
            stmt.setString(6, varAPT);
            stmt.setString(7, varPhone);
            stmt.setString(8, varEmail);
            stmt.setString(9, varItem);
            stmt.setString(10, varItemDes);
            stmt.setString(11, varDueDate);
            stmt.setString(12, varReturn);
            stmt.setString(13, varAmount);
            
            stmt.executeUpdate();
            
            con.close();

            JOptionPane.showMessageDialog(outFrame, "DUE DATE: " + dueDate, "ATTENTION! ", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e){
                System.out.print(e);
                e.printStackTrace();
                //outFrame.dispose();
                //JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                
                JOptionPane optionPane = new JOptionPane(e.toString());
                JDialog dialog = optionPane.createDialog("Error!");
                dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                dialog.setVisible(true);
            }

        fieldAmount.setText("0.00");
        checkoutList.clear();
        boxList.setSelectedIndex(0);
    }
}
