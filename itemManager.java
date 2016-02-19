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
import java.util.logging.*;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Chris
 */
public class itemManager extends JFrame implements ActionListener{
    
    DBConnect dbCon = new DBConnect(); //Connects to the Database
    
    private final int WIDTH = 425, HEIGHT = 480;
    private static final Logger log = Logger.getLogger(itemManager.class.getName());

    JButton done, update, newItem, deleteItem;
    JFrame itemFrame = new JFrame("Item Database");
    JScrollPane jsp;
    JTable table;
    JComboBox comboBox = new JComboBox();
    int amountOwe;

    public itemManager() {

        done = new JButton("Done");
        update = new JButton("Update");
        newItem = new JButton("New");
        deleteItem = new JButton("Delete");
        done.setSize(75,30);
        update.setSize(75,30);
        newItem.setSize(75,30);
        deleteItem.setSize(75,30);
        done.addActionListener(this);
        update.addActionListener(this);
        newItem.addActionListener(this);
        deleteItem.addActionListener(this);

        itemFrame.setLayout(null);
        
        table = loadTable();
        table.setAutoResizeMode(AUTO_RESIZE_OFF);
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
        jsp = new JScrollPane(table,v ,h);
        jsp.setSize(410,390);

        newItem.setLocation(25, 400);
        update.setLocation(125,400);
        done.setLocation(325, 400);
        deleteItem.setLocation(225,400);
        jsp.setLocation(5,5);        
        
        itemFrame.add(jsp);
        itemFrame.add(done);
        itemFrame.add(update);
        itemFrame.add(newItem);
        itemFrame.add(deleteItem);
        itemFrame.setSize(WIDTH, HEIGHT);
        itemFrame.setLocationRelativeTo(null);
    }
    
    public JTable loadTable(){
        try{
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM items ORDER BY item ASC");

            // It creates and displays the table
            table = new JTable(buildTableModel(rs)){
            public boolean isCellEditable(int row, int column) {
                if (row >= 0 && (column == 1 || column == 2 || column == 3)) {
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
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if(i == 0){
                column.setMinWidth(60);
                column.setMaxWidth(60);
                column.setPreferredWidth(60);
            } else if(i == 1){
                column.setMinWidth(150);
                column.setMaxWidth(150);
                column.setPreferredWidth(150);
            } else if(i == 2){
                column.setMinWidth(90);
                column.setMaxWidth(90);
                column.setPreferredWidth(90);
            } else if(i == 3){
                column.setMinWidth(90);
                column.setMaxWidth(90);
                column.setPreferredWidth(90);
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
        
        if(ae.getSource() == newItem){
            
            itemFrame.setAlwaysOnTop(true);
            
            JFrame dummy = new JFrame();
            dummy.setVisible(false);
            dummy.setLocationRelativeTo(null);
            dummy.setAlwaysOnTop(true);
            
            JTextField item = new JTextField();
            JTextField lengthField = new JTextField();
            JTextField price = new JTextField();
            
            Object[] fields = new Object[]{"Item Name", item, "Length (in days) to be checked out (e.g. 2)", lengthField, "Price per day (e.g. 1.00)", price};
            
            int box = JOptionPane.showConfirmDialog(dummy, fields, "New Item", JOptionPane.OK_CANCEL_OPTION);
            
            

            if(box == JOptionPane.OK_OPTION){
                String temp = lengthField.getText();
                int tempLength = Integer.parseInt(temp);

                String query = "INSERT INTO items(item, time_days, price_day) VALUES(?,?,?)";
                
                try{
                    Connection con = dbCon.getConnection();
                    PreparedStatement stmt = con.prepareStatement(query);
                    stmt.setString(1, item.getText());
                    stmt.setInt(2, tempLength);
                    stmt.setString(3, price.getText());

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
                dummy.dispose();
                itemFrame.dispose(); 
            } else {
                dummy.dispose();
                itemFrame.dispose(); 
            }         
        }
        
        if(ae.getSource() == deleteItem){
            
            itemFrame.setAlwaysOnTop(true);
            
            JFrame dummy = new JFrame();
            dummy.setVisible(false);
            dummy.setLocationRelativeTo(null);
            dummy.setAlwaysOnTop(true);
            
            int box = JOptionPane.showConfirmDialog(dummy, "Are you sure you want to delete the selected item?", "Delete Item", JOptionPane.OK_CANCEL_OPTION);
            
            if(box == JOptionPane.OK_OPTION){
                
                int selectedRowIndex = table.getSelectedRow();
                int selectedColumnIndex = table.getSelectedColumn();
                int itemID = (int)table.getModel().getValueAt(selectedRowIndex, 0);
                
                String query = "DELETE FROM items WHERE item_id = " + itemID;
                
                try{
                    Connection con = dbCon.getConnection();
                    Statement stmt = con.createStatement();

                    stmt.executeUpdate(query);
                    con.close();
                    stmt.close();

                   } catch (Exception e){
                       System.out.print(e);
                        e.printStackTrace();
                        JOptionPane optionPane = new JOptionPane(e.toString());
                        JDialog dialog = optionPane.createDialog("Error!");
                        dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                        dialog.setVisible(true);
                   }
            }
            
            dummy.dispose();
            itemFrame.dispose();
        }
        
        if(ae.getSource() == update){  
            ExecuteCommand();            
        }

        if(ae.getSource() == done){
            itemFrame.dispose();
        }
    }
    
    public void ExecuteCommand(){
        int selectedRowIndex = table.getSelectedRow();
        int selectedColumnIndex = table.getSelectedColumn();
        int itemID = (int)table.getModel().getValueAt(selectedRowIndex, 0); 
        String item = (String)table.getModel().getValueAt(selectedRowIndex, 1);
        String length = table.getModel().getValueAt(selectedRowIndex, 2).toString();
        Object priceDay = table.getModel().getValueAt(selectedRowIndex, 3);
        
        String query = "UPDATE items SET item=?, time_days=?, price_day=? WHERE item_id = ?";

        try{
            Connection con = dbCon.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, item);
            stmt.setString(2, length);
            stmt.setObject(3, priceDay);
            stmt.setInt(4, itemID);

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
