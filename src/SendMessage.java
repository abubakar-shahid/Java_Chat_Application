import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendMessage extends JFrame implements ActionListener {
    private JPanel panel;
    private JLabel rec;
    private JTextField receiver;
    private JLabel mess;
    private JTextArea message;
    private JButton send;
    private static Connection connection;
    private String name;

    public void createSendMessagePage(Connection conn, String username) {
        connection = conn;
        name = username;
        rec = new JLabel("Receiver's Username: ");
        receiver = new JTextField(20);
        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(rec);
        jp1.add(receiver);
        mess = new JLabel("Write your message: ");
        message = new JTextArea();
        message.setPreferredSize(new Dimension(300, 50));
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(message);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel jp2 = new JPanel(new FlowLayout());
        jp2.add(mess);
        jp2.add(scrollPane);
        JPanel jp3 = new JPanel(new FlowLayout());
        send = new JButton("Send");
        send.addActionListener(this);
        jp3.add(send);

        panel = new JPanel(new GridLayout(3, 1));
        panel.add(jp1);
        panel.add(jp2);
        panel.add(jp3);

        //Frame
        setLayout(new GridLayout(3, 1));
        add(new JPanel());
        add(panel);
        setTitle("Send Message");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (saveData()) {
                JOptionPane.showMessageDialog(this, "Message Sent Successfully!");
                dispose();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //---------------------------------------------------------------------------------------------------------
    class MyWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
            dispose();
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
    private boolean saveData() throws SQLException {
        String query = "select username from users where username = '" + receiver.getText() + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            JOptionPane.showMessageDialog(this, "Receiver not found!", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            query = "insert into messages (date, sender, receiver, messageBody) values (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(currentDate);
                preparedStatement.setString(1, formattedDate);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, receiver.getText());
                preparedStatement.setString(4, message.getText());
                preparedStatement.executeUpdate();
            }
            return true;
        }
    }
//---------------------------------------------------------------------------------------------------------
}
