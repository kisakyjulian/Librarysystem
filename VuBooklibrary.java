/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.company.vu.booklibrary;



/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class VuBooklibrary extends javax.swing.JFrame{
 private JTextField txtTitle, txtAuthor, txtYear;
    private JTable table;
    private DefaultTableModel tablemodel;
    
    public VuBooklibrary(){
        setTitle("Library System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Title:"));
        txtTitle = new JTextField();
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        formPanel.add(txtAuthor);
        formPanel.add(new JLabel("Year:"));
        txtYear = new JTextField();
        formPanel.add(txtYear);
        
        JButton btnAdd = new JButton("Add Book");
        formPanel.add(btnAdd);
        
        add(formPanel, BorderLayout.NORTH);
        
        // Table Panel
        table = new JTable();
        tablemodel = new DefaultTableModel(new String[]{"BookID", "Title", "Author", "Year"}, 0);
        table.setModel(tablemodel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton btnDelete = new JButton("Delete Book");
        buttonPanel.add(btnDelete);
        JButton btnRefresh = new JButton("Refresh List");
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Button Actions
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
        
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });
        
        // Load Books on Startup
        loadBooks();
        
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void addBook() {
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        int year = Integer.parseInt(txtYear.getText());
        
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO Books (Title, Author, Year) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtTitle.getText());
            pstmt.setString(2, txtAuthor.getText());
            pstmt.setInt(3, Integer.parseInt(txtYear.getText()));
            pstmt.executeUpdate();
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        }
    }
    
    private void deleteBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int bookID = (int) tablemodel.getValueAt(selectedRow, 0);
            try (Connection conn = getConnection()) {
                String sql = "DELETE FROM Books WHERE BookID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, bookID);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
                loadBooks();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        }
    }
    
    private void loadBooks() {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM Books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            tablemodel.setRowCount(0);
            while (rs.next()) {
                int bookID = rs.getInt("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                int year = rs.getInt("Year");
                tablemodel.addRow(new Object[]{bookID, title, author, year});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
private Connection getConnection()throws SQLException{
String url = "jdbc:ucanaccess://C:\\Users\\Julian\\Documents\\NetBeansProjects\\Vu.booklibrary\\booklibrary.accdb";
return DriverManager.getConnection(url);
}
 public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Book_library().setVisible(true);
            }
        });

    
    }

}
