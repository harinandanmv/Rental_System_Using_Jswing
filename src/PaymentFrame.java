import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class PaymentFrame extends JFrame {
    private String userEmail;
    private int bookingId;
    private JTextField amountField;
    private JComboBox<String> paymentMethodBox;

    public PaymentFrame(String userEmail, int bookingId) {
        this.userEmail = userEmail;
        this.bookingId = bookingId;
        setTitle("Car Rental System - Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        getContentPane().setBackground(new Color(245, 245, 245));
        getContentPane().setLayout(null);
        
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

        JLabel titleLabel = new JLabel("Payment");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(160, 20, 120, 30);
        getContentPane().add(titleLabel);

        JLabel bookingIdLabel = new JLabel("Booking ID: " + bookingId);
        bookingIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        bookingIdLabel.setBounds(50, 80, 300, 25);
        getContentPane().add(bookingIdLabel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        amountLabel.setBounds(50, 120, 100, 25);
        getContentPane().add(amountLabel);

        amountField = new JTextField();
        amountField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        amountField.setBounds(160, 120, 200, 25);
        getContentPane().add(amountField);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        paymentMethodLabel.setBounds(50, 160, 130, 25);
        getContentPane().add(paymentMethodLabel);

        String[] methods = {"Credit Card", "PayPal", "Cash"};
        paymentMethodBox = new JComboBox<>(methods);
        paymentMethodBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        paymentMethodBox.setBounds(160, 160, 200, 25);
        getContentPane().add(paymentMethodBox);

        JButton processPaymentButton = new JButton("Process Payment");
        processPaymentButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        processPaymentButton.setBackground(Color.GREEN);
        processPaymentButton.setForeground(Color.WHITE);
        processPaymentButton.setBounds(50, 210, 150, 30);
        getContentPane().add(processPaymentButton);
        processPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (amountField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the amount.");
                } else {
                    insertPayment();
                }
            }
        });

        JButton backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(220, 210, 150, 30);
        getContentPane().add(backButton);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeFrame(PaymentFrame.this.userEmail).setVisible(true);
                dispose();
            }
        });
    }

    private void insertPayment() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO Payments (booking_id, payment_date, amount, payment_method, transaction_status) "
                       + "VALUES (?, SYSDATE, ?, ?, 'Completed')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, this.bookingId);
            ps.setDouble(2, Double.parseDouble(amountField.getText()));
            ps.setString(3, paymentMethodBox.getSelectedItem().toString());
            int rows = ps.executeUpdate();
            ps.close();

            if (rows > 0) {
                updateBookingStatus(conn);
                JOptionPane.showMessageDialog(null, "Payment processed successfully for Booking ID: " + this.bookingId);
                new HomeFrame(this.userEmail).setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void updateBookingStatus(Connection conn) throws SQLException {
        String updateSql = "UPDATE Bookings SET status = 'Booked' WHERE booking_id = ?";
        PreparedStatement ps2 = conn.prepareStatement(updateSql);
        ps2.setInt(1, this.bookingId);
        ps2.executeUpdate();
        ps2.close();
    }
}
