import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DashBoard extends JFrame implements ActionListener {
    private JMenuBar bar;
    private JMenu menu;
    private JMenuItem it1, it2, it3, it4;
    private JLabel l1;
    private static Connection connection;
    private String name;

    //---------------------------------------------------------------------------------------------------------
    public void createDashBoard(String username, Connection conn) {
        connection = conn;
        name = username;
        bar = new JMenuBar();
        menu = new JMenu("☰");
        menu.setForeground(Color.WHITE);
        Font menuFont = bar.getFont();
        Font newMenuFont = menuFont.deriveFont(24.0f);
        menu.setFont(newMenuFont);
        it1 = new JMenuItem("Inbox");
        it2 = new JMenuItem("Send Message");
        it3 = new JMenuItem("My Account");
        it4 = new JMenuItem("Sent Messages");
        it1.addActionListener(this);
        it2.addActionListener(this);
        it3.addActionListener(this);
        it4.addActionListener(this);
        menu.add(it3);
        menu.add(it4);
        menu.add(it1);
        menu.add(it2);
        bar.add(menu);
        bar.setBackground(Color.BLACK);
        bar.setSize(500, 500);
        JPanel jp1 = new JPanel(new FlowLayout());
        l1 = new JLabel("Welcome '" + name + "'!");
        l1.setForeground(Color.WHITE);
        jp1.setBackground(Color.BLACK);
        jp1.add(l1);
        Font labelFont = l1.getFont();
        Font newLabelFont = labelFont.deriveFont(16.0f);
        l1.setFont(newLabelFont);
        it1.setFont(newLabelFont);
        it2.setFont(newLabelFont);
        it3.setFont(newLabelFont);
        it4.setFont(newLabelFont);
        bar.setPreferredSize(new Dimension(100, 40));
        BackgroundPanel backgroundPanel = new BackgroundPanel("D:\\Gallery\\Pictures\\fallen-knight-dark-souls-3-from-software.jpg");

        //Frame
        setLayout(new BorderLayout());
        setJMenuBar(bar);
        add(jp1, BorderLayout.NORTH);
        add(backgroundPanel, BorderLayout.CENTER);
        setTitle("Dash Board");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Inbox") {
            Inbox inbox = new Inbox();
            try {
                inbox.createInboxPage(connection, name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand() == "Send Message") {
            SendMessage sendMessage = new SendMessage();
            sendMessage.createSendMessagePage(connection, name);
        } else if (e.getActionCommand() == "Sent Messages") {
            SentMessages sentMessages = new SentMessages();
            try {
                sentMessages.showSentMessages(connection, name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            MyAccount account = new MyAccount();
            try {
                account.showAccountDetails(connection, name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------
    class MyWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Confirm Close", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }

    //---------------------------------------------------------------------------------------------------------
    static class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    //---------------------------------------------------------------------------------------------------------
}
