import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewBookingsFrame extends JFrame {
    private String userEmail;
    private JTable bookingsTable;

    public ViewBookingsFrame(String userEmail) {
        this.userEmail = userEmail;
        setTitle("View Bookings - Car Rental System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
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

        JLabel titleLabel = new JLabel("Your Bookings");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBounds(220, 20, 200, 30);
        getContentPane().add(titleLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 70, 500, 200);
        getContentPane().add(scrollPane);

        bookingsTable = new JTable();
        bookingsTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
        bookingsTable.setRowHeight(25);
        scrollPane.setViewportView(bookingsTable);

        JButton backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(230, 300, 140, 30);
        getContentPane().add(backButton);
        backButton.addActionListener(e -> {
            new HomeFrame(ViewBookingsFrame.this.userEmail).setVisible(true);
            dispose();
        });

        loadBookings();
    }

    private void loadBookings() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT booking_id, car_id, " +
                         "TO_CHAR(rental_start_date, 'YYYY-MM-DD') AS rental_start_date, " +
                         "TO_CHAR(rental_end_date, 'YYYY-MM-DD') AS rental_end_date, " +
                         "status " +
                         "FROM Bookings " +
                         "WHERE user_email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, this.userEmail);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new String[] {"Booking ID", "Car ID", "Start Date", "End Date", "Status"}, 
                0
            );

            while(rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int carId = rs.getInt("car_id");
                String start = rs.getString("rental_start_date");
                String end = rs.getString("rental_end_date");
                String status = rs.getString("status");
                model.addRow(new Object[] {bookingId, carId, start, end, status});
            }
            bookingsTable.setModel(model);

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
