/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package office;


import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.joda.time.*;

/**
 *
 * @author Chris
 */
public class CheckIn extends JFrame implements ActionListener{

    DBConnect dbCon = new DBConnect(); //Connects to the Database
    
    private final int WIDTH = 1000, HEIGHT = 500;

    JButton done, cancel, update;
    JFrame inFrame = new JFrame("Check In Item");
    JScrollPane jsp;
    JTable table;
    JComboBox comboBox = new JComboBox();
    MyDateTime DATE = new MyDateTime();
    String staffName;
    double amountOwe;

    public CheckIn() {

        done = new JButton("Done");
        cancel = new JButton("Cancel");
        update = new JButton("Update");
        done.setSize(75,30);
        cancel.setSize(75,30);
        update.setSize(75,30);
        done.addActionListener(this);
        cancel.addActionListener(this);
        update.addActionListener(this);

        inFrame.setLayout(null);
        
        table = loadTable();
        table.setAutoResizeMode(AUTO_RESIZE_OFF);
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
        jsp = new JScrollPane(table,v ,h);
        jsp.setSize(985,390);
        
        done.setLocation(350, 400);
        cancel.setLocation(576, 400);
        update.setLocation(463,400);
        jsp.setLocation(5,5);        
        
        inFrame.add(jsp);
        inFrame.add(done);
        inFrame.add(cancel);
        inFrame.add(update);
        inFrame.setSize(WIDTH, HEIGHT);
        inFrame.setLocationRelativeTo(null);
    }
    
    public void getStaffName(String name){
        staffName = name;
    }
    
    public JTable loadTable(){
        try{
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cust_id, checkoutdate, fname, lname, apt, item, description, duedate, returned FROM CHECKOUTS WHERE returned = 'NO'");

            // It creates and displays the table
            table = new JTable(buildTableModel(rs)){
            public boolean isCellEditable(int row, int column) {
                if (row >= 0 && column == 8) {
                    return true;
                } else {
                    return false;
                }
            }
        };            
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
        
        comboBox.addItem("YES");
        comboBox.addItem("NO");

        TableColumn column = null;
        for (int i = 0; i < 9; i++) {
            column = table.getColumnModel().getColumn(i);
            if(i == 0){
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 1){
                column.setMinWidth(120);
                column.setMaxWidth(120);
                column.setPreferredWidth(120);
            } else if(i == 2 || i == 3){
                column.setMinWidth(100);
                column.setMaxWidth(100);
                column.setPreferredWidth(100);
            } else if(i == 4 || i == 5){
                column.setMinWidth(100);
                column.setMaxWidth(100);
                column.setPreferredWidth(100);
            } else if(i == 6){
                column.setMinWidth(200);
                column.setMaxWidth(200);
                column.setPreferredWidth(200);
            } else if(i == 7){
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if(i == 8){
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
                column.setCellEditor(new DefaultCellEditor(comboBox));
            }
        }
        
        return table;
    }
    
public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

    ResultSetMetaData metaData = rs.getMetaData();

    // names of columns
    Vector<String> columnNames = new Vector<String>();
    int columnCount = metaData.getColumnCount();
    for (int column = 1; column <= columnCount; column++) {
        columnNames.add(metaData.getColumnName(column));
    }

    // data of the table
    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
    while (rs.next()) {
        Vector<Object> vector = new Vector<Object>();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            vector.add(rs.getObject(columnIndex));
        }
        data.add(vector);
    }

    return new DefaultTableModel(data, columnNames);
}

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == cancel){

            inFrame.dispose();
        }
        
        if(ae.getSource() == update){ 
            if(table.getSelectedRow() == -1){
                
                JFrame dummy = new JFrame();
                dummy.setVisible(false);
                dummy.setLocationRelativeTo(null);
                dummy.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(dummy, "Please select the row you wish to update!", "ATTENTION! ", JOptionPane.INFORMATION_MESSAGE);
                dummy.dispose();
            } else {
                ExecuteCommand();
            }           
        }

        if(ae.getSource() == done){
            if(table.getSelectedRow() == -1){
                
                JFrame dummy = new JFrame();
                dummy.setVisible(false);
                dummy.setLocationRelativeTo(null);
                dummy.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(dummy, "Please select the row you wish to update!", "ATTENTION! ", JOptionPane.INFORMATION_MESSAGE);
                dummy.dispose();
            } else {
                ExecuteCommand();
                inFrame.dispose();
            }
        }
    }
    
    public void ExecuteCommand(){
        double price = 0.00;
        int selectedRowIndex = table.getSelectedRow();
        int selectedColumnIndex = table.getSelectedColumn();
        String item = (String)table.getModel().getValueAt(selectedRowIndex, 5);
        String returned = (String)table.getModel().getValueAt(selectedRowIndex, 8);
        Date dueDate = (Date) table.getModel().getValueAt(selectedRowIndex, 7);
        int custID = (int)table.getModel().getValueAt(selectedRowIndex, 0);

        int days = Days.daysBetween(new DateTime(dueDate), new DateTime(DATE.getDate())).getDays();
                
        if(days < 0){
            days = 0;
        }
        
        try{
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT price_day FROM items WHERE item = '" + item + "'");

            rs.next();
            price = rs.getDouble(1);

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
        
        amountOwe = days*price;        
        
        if(returned != null){            
            String query = "UPDATE CHECKOUTS SET RETURNED='" + returned + "', CHECKINDATE='" + DATE.getDate() + "', STAFF2='" + staffName + "', AMOUNTOWE=" + amountOwe + " WHERE CUST_ID = " + custID;

            try{
                Connection con = dbCon.getConnection();
                Statement stmt = con.createStatement();

                stmt.executeUpdate(query);
                con.close();

               } catch (Exception e){
                    System.out.print(e);
                    e.printStackTrace();
                    JOptionPane optionPane = new JOptionPane(e.toString());
                    JDialog dialog = optionPane.createDialog("Error!");
                    dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                    dialog.setVisible(true);
               }
        } else{
            JOptionPane.showMessageDialog(null, "Please select the cell you want to update!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
