import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CarBookingFrame extends JFrame {
    private String userEmail;
    private JTextField carIdField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTable carTable;

    public CarBookingFrame(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Book a Car");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
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

        JLabel titleLabel = new JLabel("Car Booking");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(220, 20, 200, 30);
        getContentPane().add(titleLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 70, 500, 150);
        getContentPane().add(scrollPane);

        carTable = new JTable();
        carTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
        carTable.setRowHeight(25);
        scrollPane.setViewportView(carTable);
        loadCars();

        JLabel carIdLabel = new JLabel("Select Car ID:");
        carIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        carIdLabel.setBounds(50, 240, 100, 25);
        getContentPane().add(carIdLabel);

        carIdField = new JTextField();
        carIdField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        carIdField.setBounds(160, 240, 100, 25);
        getContentPane().add(carIdField);

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        startDateLabel.setBounds(50, 280, 180, 25);
        getContentPane().add(startDateLabel);

        startDateField = new JTextField();
        startDateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        startDateField.setBounds(210, 280, 120, 25);
        getContentPane().add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        endDateLabel.setBounds(50, 320, 180, 25);
        getContentPane().add(endDateLabel);

        endDateField = new JTextField();
        endDateField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        endDateField.setBounds(210, 320, 120, 25);
        getContentPane().add(endDateField);

        JButton confirmBookingButton = new JButton("Confirm Booking");
        confirmBookingButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        confirmBookingButton.setBackground(Color.GREEN);
        confirmBookingButton.setForeground(Color.WHITE);
        confirmBookingButton.setBounds(50, 370, 150, 30);
        getContentPane().add(confirmBookingButton);
        confirmBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (carIdField.getText().isEmpty() 
                    || startDateField.getText().isEmpty() 
                    || endDateField.getText().isEmpty()) 
                {
                    JOptionPane.showMessageDialog(null, "Please fill in all booking details.");
                } else {
                    insertBooking();
                }
            }
        });

        JButton backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(250, 370, 150, 30);
        getContentPane().add(backButton);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HomeFrame(CarBookingFrame.this.userEmail).setVisible(true);
                dispose();
            }
        });
    }

    private void loadCars() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT car_id, make, model, year, rental_price_per_day FROM Cars";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new String[] {
                "Car ID", "Make", "Model", "Year", "Price/Day"
            }, 0);
            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String make = rs.getString("make");
                String modelStr = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("rental_price_per_day");
                model.addRow(new Object[] { carId, make, modelStr, year, price });
            }
            carTable.setModel(model);
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void insertBooking() {
        try {
            Connection conn = DBConnection.getConnection();
            String insertSql = 
                "INSERT INTO Bookings (user_email, car_id, rental_start_date, rental_end_date, status) "
              + "VALUES (?, ?, TO_DATE(?,'YYYY-MM-DD'), TO_DATE(?,'YYYY-MM-DD'), 'Pending')";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setString(1, this.userEmail);
            ps.setInt(2, Integer.parseInt(carIdField.getText()));
            ps.setString(3, startDateField.getText());
            ps.setString(4, endDateField.getText());
            int rows = ps.executeUpdate();
            ps.close();

            if (rows > 0) {
                int newlyInsertedBookingId = fetchNewBookingId(conn);
                JOptionPane.showMessageDialog(null, "Booking confirmed! ID: " + newlyInsertedBookingId);
                new PaymentFrame(this.userEmail, newlyInsertedBookingId).setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private int fetchNewBookingId(Connection conn) throws SQLException {
        int resultId = -1;
        String sql = 
            "SELECT booking_id FROM Bookings WHERE user_email=? AND car_id=? " +
            "AND rental_start_date=TO_DATE(?,'YYYY-MM-DD') " +
            "AND rental_end_date=TO_DATE(?,'YYYY-MM-DD') " +
            "ORDER BY booking_id DESC";
        PreparedStatement ps2 = conn.prepareStatement(sql);
        ps2.setString(1, this.userEmail);
        ps2.setInt(2, Integer.parseInt(carIdField.getText()));
        ps2.setString(3, startDateField.getText());
        ps2.setString(4, endDateField.getText());
        ResultSet rs2 = ps2.executeQuery();
        if (rs2.next()) {
            resultId = rs2.getInt("booking_id");
        }
        rs2.close();
        ps2.close();
        return resultId;
    }
}
