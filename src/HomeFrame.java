import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HomeFrame extends JFrame {
    public HomeFrame(String userEmail) {
        setTitle("Car Rental System - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);

        // Create the menu bar with a Help dropdown
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display a dialog with a brief introduction about the platform
                JOptionPane.showMessageDialog(
                    HomeFrame.this,
                    "Welcome to the Car Rental System platform.\n" +
                    "Here you can easily book a car, view your existing bookings,\n" +
                    "and manage your account. Enjoy your experience!",
                    "About Car Rental System",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Load and scale the background image
        ImageIcon bgIcon = new ImageIcon("src/resources/carrentalbg.jpeg");
        Image backgroundImage = bgIcon.getImage().getScaledInstance(450, 350, Image.SCALE_SMOOTH);
        
        // Create a background panel with custom painting
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20, 20, 400, 300);
        tabbedPane.setOpaque(false); // Transparent so the background shows

        // Tab 1: Book a Car
        JPanel bookCarPanel = new JPanel();
        bookCarPanel.setOpaque(false);
        bookCarPanel.setLayout(null);
        JLabel bookLabel = new JLabel("Book a Car");
        bookLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        bookLabel.setBounds(130, 20, 150, 30);
        bookCarPanel.add(bookLabel);
        JButton bookCarButton = new JButton("Book a Car");
        bookCarButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        bookCarButton.setBounds(150, 80, 120, 30);
        bookCarButton.setBackground(new Color(0, 153, 0));
        bookCarButton.setForeground(Color.WHITE);
        bookCarButton.setFocusPainted(false);
        bookCarPanel.add(bookCarButton);
        bookCarButton.addActionListener(e -> {
            new CarBookingFrame(userEmail).setVisible(true);
            dispose();
        });
        tabbedPane.addTab("Book a Car", bookCarPanel);

        // Tab 2: View Bookings
        JPanel viewBookingsPanel = new JPanel();
        viewBookingsPanel.setOpaque(false);
        viewBookingsPanel.setLayout(null);
        JLabel viewLabel = new JLabel("View Bookings");
        viewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        viewLabel.setBounds(130, 20, 150, 30);
        viewBookingsPanel.add(viewLabel);
        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        viewBookingsButton.setBounds(150, 80, 140, 30);
        viewBookingsButton.setBackground(new Color(0, 153, 0));
        viewBookingsButton.setForeground(Color.WHITE);
        viewBookingsButton.setFocusPainted(false);
        viewBookingsPanel.add(viewBookingsButton);
        viewBookingsButton.addActionListener(e -> {
            new ViewBookingsFrame(userEmail).setVisible(true);
            dispose();
        });
        tabbedPane.addTab("View Bookings", viewBookingsPanel);

        // Tab 3: Logout
        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.setLayout(null);
        JLabel logoutLabel = new JLabel("Logout");
        logoutLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        logoutLabel.setBounds(180, 20, 80, 30);
        logoutPanel.add(logoutLabel);
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        logoutButton.setBounds(150, 80, 90, 30);
        logoutButton.setBackground(new Color(204, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutPanel.add(logoutButton);
        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        tabbedPane.addTab("Logout", logoutPanel);

        backgroundPanel.add(tabbedPane);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new HomeFrame("test@example.com").setVisible(true);
        });
    }
}
