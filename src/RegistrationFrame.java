import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegistrationFrame() {
        setTitle("Car Rental System - Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        ImageIcon bgIcon = new ImageIcon("src/resources/carrentalbg.jpeg");
        Image backgroundImage = bgIcon.getImage().getScaledInstance(450, 350, Image.SCALE_SMOOTH);
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("Register New Account");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 51, 102));
        titleLabel.setBounds(120, 20, 250, 30);
        getContentPane().add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(0, 51, 102));
        usernameLabel.setBounds(50, 70, 80, 25);
        getContentPane().add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        usernameField.setBounds(140, 70, 200, 25);
        getContentPane().add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(0, 51, 102));
        emailLabel.setBounds(50, 110, 80, 25);
        getContentPane().add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        emailField.setBounds(140, 110, 200, 25);
        getContentPane().add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(0, 51, 102));
        passwordLabel.setBounds(50, 150, 80, 25);
        getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordField.setBounds(140, 150, 200, 25);
        getContentPane().add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 153, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBounds(140, 210, 90, 30);
        getContentPane().add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                } else {
                    try {
                        Connection conn = DBConnection.getConnection();
                        String sql = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, username);
                        ps.setString(2, email);
                        ps.setString(3, password);
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null, "Registration successful!");
                            new LoginFrame().setVisible(true);
                            dispose();
                        }
                        ps.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                    }
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBackground(new Color(204, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBounds(250, 210, 90, 30);
        getContentPane().add(backButton);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RegistrationFrame frame = new RegistrationFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
