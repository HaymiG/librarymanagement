

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class LibraryManagementSystem22 {

    // Database connection
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "@#Astu123$");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error!");
            return null;
        }
    }

    // Display a login frame
    public static void loginFrame() {
        JFrame frame = new JFrame("Login");
        frame.setTitle("Library Management System - Login");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = createButton("Login");
        JButton cancelButton = createButton("Cancel");

        // Positioning components
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(userLabel, gbc);
        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passLabel, gbc);
        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(loginButton, gbc);
        gbc.gridx = 1;
        frame.add(cancelButton, gbc);

        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and password cannot be empty!");
                return;
            }

            Connection connection = connect();
            if (connection != null) {
                try {
                    Statement stmt = connection.createStatement();
                    String query = "SELECT * FROM USERS WHERE User_name ='" + username + "' AND PASSWORD='" + password + "'";
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        String userType = rs.getString("user_type");
                        String userId = rs.getString("UID");
                        frame.dispose();
                        if ("1".equals(userType)) {
                            showLibrarianFrame();
                        } else {
                            showUserFrame(userId);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error during login!");
                }
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());
    }
    public static void showLibrarianFrame() {
        JFrame frame = new JFrame("Librarian Functions");

        JButton addBookButton = createButton("Add Book");
        JButton viewBooksButton = createButton("View Books");
        JButton addUserButton = createButton("Add User");
        JButton viewUsersButton = createButton("View Users");
        JButton removeUserButton = createButton("Remove User");
        JButton removeBookButton = createButton("Remove Book");  // New button to remove book
        JButton logoutButton = createButton("Logout");

        // Set styles for the logout button
        logoutButton.setBackground(new Color(209, 10, 227, 221));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logoutButton.setPreferredSize(new Dimension(300, 60));
        logoutButton.setForeground(Color.black);

        // Add action listeners
        addBookButton.addActionListener(e -> openAddBookFrame());
        viewBooksButton.addActionListener(e -> displayTable("SELECT * FROM BOOKS", "Books List"));
        addUserButton.addActionListener(e -> openAddUserFrame());
        viewUsersButton.addActionListener(e -> displayTable("SELECT * FROM USERS", "Users List"));

        // Action for Remove User button
        removeUserButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter username to remove:");
            if (username != null && !username.trim().isEmpty()) {
                removeUser(username);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid username.");
            }
        });

        // Action for Remove Book button
        removeBookButton.addActionListener(e -> {
            String isbn = JOptionPane.showInputDialog(frame, "Enter ID of the book to remove:");
            if (isbn != null && !isbn.trim().isEmpty()) {
                removeBook(isbn);
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
            }
        });

        // Logout button action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            loginFrame();
        });

        // Create a panel for the buttons (without the logout button)
        JPanel panel = new JPanel(new GridLayout(3, 2)); // Adjust grid layout to fit 6 buttons
        panel.add(addBookButton);
        panel.add(viewBooksButton);
        panel.add(addUserButton);
        panel.add(viewUsersButton);
        panel.add(removeUserButton);
        panel.add(removeBookButton); // Add the remove book button

        // Set the frame layout to BorderLayout
        frame.setLayout(new BorderLayout());

        // Add the panel with other buttons to the center of the frame
        frame.add(panel, BorderLayout.CENTER);

        // Add the logout button to the bottom
        frame.add(logoutButton, BorderLayout.SOUTH);

        frame.setSize(400, 250);
        frame.setVisible(true);
    }

    // Remove Book method
    public static void removeBook(String id) {
        Connection connection = connect();
        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                String query = "DELETE FROM BOOKS WHERE bookid = '" + 005 + "'";
                int rowsAffected = stmt.executeUpdate(query);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Book removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "No book found with that Id.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error removing book!");
            }
        }
    }


    // Remove User method
    public static void removeUser(String username) {
        Connection connection = connect();
        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                String query = "DELETE FROM USERS WHERE USER_NAME = '" + username + "'";
                int rowsAffected = stmt.executeUpdate(query);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "User removed successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "No user found with that username.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error removing user!");
            }
        }
    }





    // Open the add book frame
    public static void openAddBookFrame() {
        JFrame frame = new JFrame("Add Book");

        JLabel isbnLabel = createLabel("ISBN");
        JLabel nameLabel = createLabel("Name");
        JLabel publisherLabel = createLabel("Publisher");
        JLabel editionLabel = createLabel("Edition");
        JLabel priceLabel = createLabel("Price");
        JLabel pagesLabel = createLabel("Pages");

        JTextField isbnField = createTextField();
        JTextField nameField = createTextField();
        JTextField publisherField = createTextField();
        JTextField editionField = createTextField();
        JTextField genreField = createTextField();
        JTextField priceField = createTextField();
        JTextField pagesField = createTextField();

        JButton submitButton = createButton("Submit");
        JButton cancelButton = createButton("Cancel");

        submitButton.addActionListener(e -> {
            String isbn = isbnField.getText();
            String name = nameField.getText();
            String publisher = publisherField.getText();
            String edition = editionField.getText();
            String genre = genreField.getText();
            int price = Integer.parseInt(priceField.getText());
            int pages = Integer.parseInt(pagesField.getText());

            Connection connection = connect();
            if (connection != null) {
                try {
                    Statement stmt = connection.createStatement();
                    String query = "INSERT INTO BOOKS (book_isbn, book_name, book_publisher, book_edition, book_price, book_pages) " +
                            "VALUES ('" + isbn + "', '" + name + "', '" + publisher + "', '" + edition + "',  " + price + ", " + pages + ")";
                    stmt.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "Book added successfully!");
                    frame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding book!");
                }
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setLayout(new GridLayout(8, 2));
        frame.add(isbnLabel);
        frame.add(isbnField);
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(publisherLabel);
        frame.add(publisherField);
        frame.add(editionLabel);
        frame.add(editionField);
        frame.add(genreField);
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(pagesLabel);
        frame.add(pagesField);
        frame.add(submitButton);
        frame.add(cancelButton);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    // Display the user frame
    public static void showUserFrame(String userId) {
        JFrame frame = new JFrame("User Functions");

        JButton viewBooksButton = createButton("View Books");
        JButton issuedBooksButton = createButton("Issued Books");
        JButton returnedBooksButton = createButton("Returned Books");
        JButton logoutButton = createButton("Logout");
        logoutButton.setBackground(new Color(125, 8, 18));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        logoutButton.setForeground(Color.black);


        viewBooksButton.addActionListener(e -> displayTable("SELECT * FROM BOOKS", "Books List"));
        issuedBooksButton.addActionListener(e -> {
            String query = "SELECT * FROM Issued_books WHERE IID = '" + 001 + "'";
            System.out.println("Executing query: " + query);
            displayTable(query, "My Issued Books");
        });

        returnedBooksButton.addActionListener(e -> {
            String query = "SELECT * FROM returned_books WHERE RID = '" + 001 + "'";
            displayTable(query, "My Returned Books");
        });

        // Logout button action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            loginFrame();
        });

        frame.setLayout(new GridLayout(4, 1));
        frame.add(viewBooksButton);
        frame.add(issuedBooksButton);
        frame.add(returnedBooksButton);
        frame.add(logoutButton);
        frame.setSize(300, 250);
        frame.setVisible(true);
    }
    // Open the add user frame
    public static void openAddUserFrame() {
        JFrame frame = new JFrame("Add User");

        JLabel usernameLabel = createLabel("User_name");
        JLabel passwordLabel = createLabel("Password");
        JLabel userTypeLabel = createLabel("User_Type");

        JTextField usernameField = createTextField();
        JPasswordField passwordField = new JPasswordField();

        // User type selection (assuming "0" for regular users and "1" for admins)
        String[] userTypes = { "0 - Regular User", "1 - Admin" };
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);

        JButton submitButton = createButton("Submit");
        JButton cancelButton = createButton("Cancel");

        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = userTypeComboBox.getSelectedItem().toString().split(" ")[0];

            Connection connection = connect();
            if (connection != null) {
                try {
                    Statement stmt = connection.createStatement();
                    // Correct query with user_type
                    String query = "INSERT INTO USERS (USER_NAME, PASSWORD, USER_TYPE) VALUES ('" + username + "', '" + password + "', '" + userType + "')";
                    stmt.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "User added successfully!");
                    frame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding user!");
                }
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setLayout(new GridLayout(4, 2));
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(userTypeLabel);
        frame.add(userTypeComboBox);
        frame.add(submitButton);
        frame.add(cancelButton);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    // Display a table with data from a query
    public static void displayTable(String query, String title) {
        JFrame frame = new JFrame(title);

        Connection connection = connect();
        if (connection != null) {
            try {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                JTable table = new JTable();
                DefaultTableModel model = new DefaultTableModel();
                table.setModel(model);

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }

                // Add rows
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }

                frame.add(new JScrollPane(table));
                frame.setSize(800, 400);
                frame.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error retrieving data!");
            }
        }
    }

    // Helper methods for reusable components
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        label.setForeground(Color.black);
        return label;
    }

    public static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.lightGray);
        textField.setForeground(Color.black);
        return textField;
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(124, 85, 227 ));
        button.setForeground(Color.WHITE);
        return button;
    }

    // Main method
    public static void main(String[] args) {
        loginFrame();
    }
}


























































