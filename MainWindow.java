/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package office;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Chris
 */
public class MainWindow extends JFrame implements MouseListener, WindowListener{
    private final int WIDTH = 735, HEIGHT = 450;
    DBConnect dbCon = new DBConnect(); //Connects to the Database
  
    JFrame frame_Main = new JFrame("FASA Office");
    JFrame info = new JFrame("About");
    JFrame play = new JFrame("How To Use");
    JLabel MoResLabel = new JLabel("<html>Monthly Responsibilities<br>K = Kitchen PR = Play Room </html>");
    JLabel WeResLabel = new JLabel("Weekly Responsibilities");
    JLabel staffName = new JLabel("Staff Name: ", SwingConstants.TRAILING);
    JButton checkOut = new JButton("Check Out");
    JButton checkIn = new JButton("Check In");
    JButton oops = new JButton("OOPS!");
    JButton overDueButton = new JButton("Over Due");
    JTextField fieldSName;
    MonthlyRes MoRes = new MonthlyRes();
    WeeklyRes WeRes = new WeeklyRes();
    CheckOut checkOUT;
    CheckIn checkIN;
    ModifyDatabase modDB;
    OverDueTable overDue;
    itemManager itemManage;
    JTextArea how, monthlyRes, weeklyRes;
    JMenuBar menuBar = new JMenuBar();
    JMenu file, help;
    JMenuItem exit, about, howToUse, items, runSQL, exportExcel;
    
    public MainWindow(){
                
        frame_Main.setSize(WIDTH, HEIGHT);
        frame_Main.setLocationRelativeTo(null);
        frame_Main.setLayout(null);
        
        file = new JMenu("File");
        help = new JMenu("Help");

        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        howToUse = new JMenuItem("How To");
        items = new JMenuItem("Item List");
        runSQL = new JMenuItem("Run SQL");
        exportExcel = new JMenuItem("Export To Excel");

        fieldSName = new JTextField();
        monthlyRes = new JTextArea(MoRes.getRes());
        weeklyRes = new JTextArea(WeRes.getRes());
        monthlyRes.setEditable(false);
        weeklyRes.setEditable(false);
        monthlyRes.setLineWrap(true);
        weeklyRes.setLineWrap(true);

        MoResLabel.setSize(165,45);
        WeResLabel.setSize(165,45);
        monthlyRes.setSize(275,200);
        weeklyRes.setSize(300,200);
        checkOut.setSize(125,50);
        checkIn.setSize(125,50);
        oops.setSize(125,50);
        overDueButton.setSize(125,50);
        staffName.setSize(100,30);
        fieldSName.setSize(150,30);

        MoResLabel.setLocation(100,110);
        WeResLabel.setLocation(400,105);
        monthlyRes.setLocation(50,150);
        weeklyRes.setLocation(350,150);
        checkOut.setLocation(15,60);
        checkIn.setLocation(200,60);
        oops.setLocation(570,60);
        overDueButton.setLocation(385, 60);
        staffName.setLocation(10,10);
        fieldSName.setLocation(115,10);

        checkOut.addMouseListener(this);
        checkIn.addMouseListener(this);
        oops.addMouseListener(this);
        overDueButton.addMouseListener(this);
        exit.addMouseListener(this);
        about.addMouseListener(this);
        howToUse.addMouseListener(this);
        items.addMouseListener(this);
        runSQL.addMouseListener(this);
        exportExcel.addMouseListener(this);
        
        frame_Main.getContentPane().setBackground(new Color(1,37,57));
//        menuBar.setBackground(new Color(1,37,57));
        monthlyRes.setBackground(new Color(1,37,57));
        weeklyRes.setBackground(new Color(1,37,57));
        monthlyRes.setBorder(BorderFactory.createLineBorder(Color.yellow));
        weeklyRes.setBorder(BorderFactory.createLineBorder(Color.yellow));
        
        monthlyRes.setForeground(Color.WHITE); 
        weeklyRes.setForeground(Color.WHITE);
        MoResLabel.setForeground(Color.LIGHT_GRAY);
        WeResLabel.setForeground(Color.LIGHT_GRAY);
        staffName.setForeground(Color.LIGHT_GRAY);

        help.add(runSQL);
        help.add(about);
        help.add(howToUse);
        file.add(items);
        file.add(exportExcel);
        file.add(exit);
        menuBar.add(file);
        menuBar.add(help);

        frame_Main.add(fieldSName);
        frame_Main.add(staffName);
        frame_Main.add(oops);
        frame_Main.add(overDueButton);
        frame_Main.add(checkIn);
        frame_Main.add(checkOut);
        frame_Main.add(MoResLabel);
        frame_Main.add(monthlyRes);
        frame_Main.add(weeklyRes);
        frame_Main.add(WeResLabel);
        frame_Main.setJMenuBar(menuBar);
        frame_Main.setVisible(true);
        frame_Main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void mousePressed(MouseEvent e){
        JFrame dummy = new JFrame();
        
        if(e.getSource() == exit){
            System.exit(0);
        } else if(e.getSource() == runSQL){
            
            dummy.setVisible(false);
            dummy.setLocationRelativeTo(null);
            dummy.setAlwaysOnTop(true);
            
            String query = JOptionPane.showInputDialog(dummy, "SQL Query: ");
            
            try{
                Connection con = dbCon.getConnection();
                
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
            
                con.close();
            } catch(Exception er){
                System.out.print(er);
                er.printStackTrace();
                
                JOptionPane optionPane = new JOptionPane(e.toString());
                JDialog dialog = optionPane.createDialog("Error!");
                dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                dialog.setVisible(true);
                
            }
            
        } else if(e.getSource() == exportExcel){
            
            try {
                Connection con = dbCon.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT ANUM, FNAME, LNAME, ITEM, DESCRIPTION, CHECKOUTDATE, CHECKINDATE, AMOUNTOWE FROM APP.CHECKOUTS ORDER BY ANUM ASC");

                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("lawix10");
                HSSFRow rowhead = sheet.createRow((short) 0);

                rowhead.createCell((short) 0).setCellValue("A#");
                rowhead.createCell((short) 1).setCellValue("First Name");
                rowhead.createCell((short) 2).setCellValue("Last Name");
                rowhead.createCell((short) 3).setCellValue("Item");
                rowhead.createCell((short) 4).setCellValue("Description");
                rowhead.createCell((short) 5).setCellValue("Check Out Date");
                rowhead.createCell((short) 6).setCellValue("Check In Date");
                rowhead.createCell((short) 7).setCellValue("Charges");
                int i = 1;
                while (rs.next()){
                    HSSFRow row = sheet.createRow((short) i);
                    row.createCell((short) 0).setCellValue(rs.getString("ANUM"));
                    row.createCell((short) 1).setCellValue(rs.getString("FNAME"));
                    row.createCell((short) 2).setCellValue(rs.getString("LNAME"));
                    row.createCell((short) 3).setCellValue(rs.getString("ITEM"));
                    row.createCell((short) 4).setCellValue(rs.getString("DESCRIPTION"));
                    row.createCell((short) 5).setCellValue(rs.getString("CHECKOUTDATE"));
                    row.createCell((short) 6).setCellValue(rs.getString("CHECKINDATE"));
                    row.createCell((short) 7).setCellValue(rs.getString("AMOUNTOWE"));
                    i++;
                }
                
                String yemi = "";
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a file to save");   
                JFrame parentFrame = new JFrame();
                parentFrame.setVisible(true);
                parentFrame.setResizable(false);
                parentFrame.addWindowListener(this);
                parentFrame.setAlwaysOnTop(true);
                parentFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                int userSelection = fileChooser.showSaveDialog(parentFrame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                    yemi = fileToSave.getAbsolutePath() + ".xls";
                    FileOutputStream fileOut = new FileOutputStream(yemi);
                    workbook.write(fileOut);
                    fileOut.close();
                    parentFrame.dispose();
                } else {
                    parentFrame.dispose();
                }

            } catch (SQLException e1) {
               e1.printStackTrace();
                JOptionPane optionPane = new JOptionPane(e.toString());
                JDialog dialog = optionPane.createDialog("Error!");
                dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                dialog.setVisible(true);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                JOptionPane optionPane = new JOptionPane(e.toString());
                JDialog dialog = optionPane.createDialog("Error!");
                dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                dialog.setVisible(true);
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane optionPane = new JOptionPane(e.toString());
                JDialog dialog = optionPane.createDialog("Error!");
                dialog.setAlwaysOnTop(this.isAlwaysOnTopSupported());
                dialog.setVisible(true);
            }
            
        } else if(e.getSource() == items){
                
            itemManage = new itemManager();
            itemManage.itemFrame.setVisible(true);
            itemManage.itemFrame.setResizable(false);
            itemManage.itemFrame.addWindowListener(this);
            itemManage.itemFrame.setAlwaysOnTop(true);
            itemManage.itemFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else if(e.getSource() == about){
            info.setSize(200,100);
            info.setLocationRelativeTo(null);
            info.setBackground(Color.DARK_GRAY);
            info.add(new JLabel("<html>Office Program <br> By: Chris Steele <br> chris.d.steele@aggiemail.usu.edu </html>"));
            info.setVisible(true);
            info.setResizable(false);
            info.addWindowListener(this);
            info.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else if(e.getSource() == howToUse){
            how = new JTextArea("***Welcome to Office Program 1.0!***");
            how.setEditable(false);
            how.setLineWrap(true);
            how.setWrapStyleWord(true);
            how.setBackground(Color.DARK_GRAY);
            how.setForeground(Color.ORANGE);

            play.setSize(400, 200);
            play.setLocationRelativeTo(null);
            play.setBackground(Color.DARK_GRAY);
            play.add(how);
            play.setVisible(true);
            play.setResizable(false);
            play.addWindowListener(this);
            play.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } else if(fieldSName.getText().matches("[a-zA-Z]+") && fieldSName.getText() != " "){
            if(e.getSource() == checkOut){
                checkOUT = new CheckOut();
                checkOUT.outFrame.setVisible(true);
                checkOUT.outFrame.setResizable(false);
                checkOUT.outFrame.addWindowListener(this);
                checkOUT.outFrame.setAlwaysOnTop(true);
                checkOUT.getStaffName(fieldSName.getText());
                checkOUT.outFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }

            if(e.getSource() == checkIn){
                checkIN = new CheckIn();
                checkIN.inFrame.setVisible(true);
                checkIN.inFrame.setResizable(false);
                checkIN.inFrame.addWindowListener(this);
                checkIN.inFrame.setAlwaysOnTop(true);
                checkIN.getStaffName(fieldSName.getText());
                checkIN.inFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
            
            if(e.getSource() == overDueButton){
                overDue = new OverDueTable();
                overDue.dueFrame.setVisible(true);
                overDue.dueFrame.setResizable(false);
                overDue.dueFrame.addWindowListener(this);
                overDue.dueFrame.setAlwaysOnTop(true);
                overDue.dueFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }

            if(e.getSource() == oops){
                modDB = new ModifyDatabase();
                modDB.modFrame.setVisible(true);
                modDB.modFrame.setResizable(false);
                modDB.modFrame.addWindowListener(this);
                modDB.modFrame.setAlwaysOnTop(true);
                modDB.modFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        } else {
            
            dummy.setVisible(false);
            dummy.setLocationRelativeTo(null);
            dummy.setAlwaysOnTop(true);
            
            JOptionPane.showMessageDialog(dummy, "Please enter a valid name in the 'Staff Name' box!", "ATTENTION! ", JOptionPane.INFORMATION_MESSAGE);
        }
        dummy.dispose();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
       play.dispose();
       info.dispose();
    }

    public void windowClosed(WindowEvent e) {

        frame_Main.setEnabled(true);
        frame_Main.isActive();
        frame_Main.setAlwaysOnTop(true);
        frame_Main.repaint();
    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

        info.setAlwaysOnTop(true);
        play.setAlwaysOnTop(true);
        frame_Main.setEnabled(false);
    }

    public void windowDeactivated(WindowEvent e) {

    }
}
