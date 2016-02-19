/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package office;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Chris
 */
public class ModifyDatabase extends JFrame implements ActionListener{

    DBConnect dbCon = new DBConnect(); //Connects to the Database
    
    private final int WIDTH = 1000, HEIGHT = 700;

    JButton done, cancel, update;
    JFrame modFrame = new JFrame("Modify Database");
    JScrollPane jsp;
    JTable table;
    JComboBox comboBox = new JComboBox();

    public ModifyDatabase() {

        done = new JButton("Done");
        cancel = new JButton("Cancel");
        update = new JButton("Update");
        done.setSize(75,30);
        cancel.setSize(75,30);
        update.setSize(75,30);
        done.addActionListener(this);
        cancel.addActionListener(this);
        update.addActionListener(this);

        modFrame.setLayout(null);
        
        table = loadTable();
        table.setAutoResizeMode(AUTO_RESIZE_OFF);
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        jsp = new JScrollPane(table,v ,h);
        jsp.setSize(985,600);        
        
        done.setLocation(350, 625);
        cancel.setLocation(576, 625);
        update.setLocation(463,625);
        jsp.setLocation(5,5);        
        
        modFrame.add(jsp);
        modFrame.add(done);
        modFrame.add(cancel);
        modFrame.add(update);
        modFrame.setSize(WIDTH, HEIGHT);
        modFrame.setLocationRelativeTo(null);
    }
    
    public JTable loadTable(){
        
        try{
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CHECKOUTS ORDER BY checkoutdate DESC");

            // It creates and displays the table
            table = new JTable(buildTableModel(rs)){
            public boolean isCellEditable(int row, int column) {
                if (row >= 0 && (column > 2)) {
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
        for (int i = 0; i < 18; i++) {
            column = table.getColumnModel().getColumn(i);
            if(i == 0){                      //Size for Cust_id                       
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 1){                //Size for Check Out Date
                column.setMinWidth(120);
                column.setMaxWidth(120);
                column.setPreferredWidth(120);
            } else if(i == 2){                //Size for Staff
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 3 || i == 4 || i == 5){     //Size for FName,LName,Anum
                column.setMinWidth(100);
                column.setMaxWidth(100);
                column.setPreferredWidth(100);
            } else if(i == 6){                //Size for APT
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 7){                //Size for Phone
                column.setMinWidth(100);
                column.setMaxWidth(100);
                column.setPreferredWidth(100);
            } else if(i == 8){                //Size for Email
                column.setMinWidth(200);
                column.setMaxWidth(200);
                column.setPreferredWidth(200);
            } else if(i == 9){                //Size for Item
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if(i == 10){                //Size for Description
                column.setMinWidth(200);
                column.setMaxWidth(200);
                column.setPreferredWidth(200);
            } else if(i == 11){                //Size for Due Date
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if(i == 12){                //Size for Returned
                column.setMinWidth(80);
                column.setMaxWidth(80);
                column.setPreferredWidth(80);
            } else if(i == 13){                //Size for Checkin date
                column.setMinWidth(100);
                column.setMaxWidth(100);
                column.setPreferredWidth(100);
            } else if(i == 14){                //Size for Staff checked in
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 15){                //Size for Amount Owed
                column.setMinWidth(90);
                column.setMaxWidth(90);
                column.setPreferredWidth(90);
            } else if(i == 16){                //Size for 1st Follow up
                column.setMinWidth(200);
                column.setMaxWidth(200);
                column.setPreferredWidth(200);
            } else if(i == 17){                //Size for 2nd Follow up
                column.setMinWidth(200);
                column.setMaxWidth(200);
                column.setPreferredWidth(200);
            }

            if (i == 12) {
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
            modFrame.dispose();
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
                modFrame.dispose();
            }
            
        }
    }
    
    public void ExecuteCommand(){
        int selectedRowIndex = table.getSelectedRow();
        int selectedColumnIndex = table.getSelectedColumn();
        int custID = (int)table.getModel().getValueAt(selectedRowIndex, 0);
        String staff = (String)table.getModel().getValueAt(selectedRowIndex, 2);
        String fName = (String)table.getModel().getValueAt(selectedRowIndex, 3);
        String lName = (String)table.getModel().getValueAt(selectedRowIndex, 4);
        String aNum = (String)table.getModel().getValueAt(selectedRowIndex, 5);
        String apt = (String)table.getModel().getValueAt(selectedRowIndex, 6);
        String phone = (String)table.getModel().getValueAt(selectedRowIndex, 7);
        String email = (String)table.getModel().getValueAt(selectedRowIndex, 8);
        String item = (String)table.getModel().getValueAt(selectedRowIndex, 9);
        String descript = (String)table.getModel().getValueAt(selectedRowIndex, 10);
        String dueDate = table.getModel().getValueAt(selectedRowIndex, 11).toString();
        String returned = (String)table.getModel().getValueAt(selectedRowIndex, 12);
        Object checkInDate = table.getModel().getValueAt(selectedRowIndex, 13);
        String staff2 = (String)table.getModel().getValueAt(selectedRowIndex, 14);
        Object amountOwe = table.getModel().getValueAt(selectedRowIndex, 15);
        String follow1 = (String)table.getModel().getValueAt(selectedRowIndex, 16);
        String follow2 = (String)table.getModel().getValueAt(selectedRowIndex, 17);


        if(checkInDate != null){            
            String query = "UPDATE CHECKOUTS SET STAFF=?,FNAME=?,LNAME=?,ANUM=?,APT=?, PHONE=?, EMAIL=?, ITEM=?, DESCRIPTION=?, DUEDATE=?, RETURNED=?, CHECKINDATE=?, STAFF2=?, AMOUNTOWE=?, FOLLOWUP1=?, FOLLOWUP2=? WHERE CUST_ID=?";

            try{
                Connection con = dbCon.getConnection();

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, staff);
                stmt.setString(2, fName);
                stmt.setString(3, lName);
                stmt.setString(4, aNum);
                stmt.setString(5, apt);
                stmt.setString(6, phone);
                stmt.setString(7, email);
                stmt.setString(8, item);
                stmt.setString(9, descript);
                stmt.setString(10, dueDate);
                stmt.setString(11, returned);
                stmt.setObject(12, checkInDate);
                stmt.setString(13, staff2);
                stmt.setObject(14, amountOwe);
                stmt.setString(15, follow1);
                stmt.setString(16, follow2);
                stmt.setInt(17, custID);
                
                stmt.executeUpdate();
                con.close();

               } catch (Exception e){
                   System.out.print(e);
                    e.printStackTrace();
                    JOptionPane optionPane = new JOptionPane(e.toString());
                    JDialog dialog = optionPane.createDialog("Error!");
                    dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                    dialog.setVisible(true);
               }
        } else if(checkInDate == null){
            String query = "UPDATE CHECKOUTS SET STAFF=?,FNAME=?,LNAME=?,ANUM=?,APT=?, PHONE=?, EMAIL=?, ITEM=?, DESCRIPTION=?, DUEDATE=?, RETURNED=?, CHECKINDATE=?, STAFF2=?, AMOUNTOWE=?, FOLLOWUP1=?, FOLLOWUP2=? WHERE CUST_ID=?";

            try{
                Connection con = dbCon.getConnection();
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, staff);
                stmt.setString(2, fName);
                stmt.setString(3, lName);
                stmt.setString(4, aNum);
                stmt.setString(5, apt);
                stmt.setString(6, phone);
                stmt.setString(7, email);
                stmt.setString(8, item);
                stmt.setString(9, descript);
                stmt.setString(10, dueDate);
                stmt.setString(11, returned);
                stmt.setObject(12, null);
                stmt.setString(13, staff2);
                stmt.setObject(14, amountOwe);
                stmt.setString(15, follow1);
                stmt.setString(16, follow2);
                stmt.setInt(17, custID);
                
                stmt.executeUpdate();
                con.close();

               } catch (Exception e){
                   System.out.print(e);
                    e.printStackTrace();
                    JOptionPane optionPane = new JOptionPane(e.toString());
                    JDialog dialog = optionPane.createDialog("Error!");
                    dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                    dialog.setVisible(true);
               }
        }
    }
}
