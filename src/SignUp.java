import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

public class SignUp extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JTextField uName;
    private JPasswordField pass;
    private JPasswordField cPass;
    private JButton signIn;
    private static Connection connection;

    //---------------------------------------------------------------------------------------------------------
    public void createSignUpPage(final Connection conn) {
        connection = conn;
        l1 = new JLabel("Create Username: ");
        l2 = new JLabel("Create Password: ");
        l3 = new JLabel("Confirm Password: ");
        uName = new JTextField(20);
        pass = new JPasswordField(20);
        cPass = new JPasswordField(20);
        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(l1);
        jp1.add(uName);
        JPanel jp2 = new JPanel(new FlowLayout());
        jp2.add(l2);
        jp2.add(pass);
        JPanel jp3 = new JPanel(new FlowLayout());
        jp3.add(l3);
        jp3.add(cPass);
        signIn = new JButton("SignUp");
        signIn.addActionListener(this);
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.add(jp1);
        p1.add(jp2);
        p1.add(jp3);
        p1.add(signIn);

        //Frame
        setLayout(new GridLayout(4, 1));
        add(new JPanel());
        add(p1);
        setTitle("SignUp Page");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new SignUp.MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            saveData();
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
    private void saveData() throws SQLException {
        String query = "select username from users where username = '" + uName.getText() + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            String st1 = new String(pass.getPassword());
            String st2 = new String(cPass.getPassword());
            if (st1.equals(st2)) {
                query = "insert into users (username, password) values (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, uName.getText());
                    preparedStatement.setString(2, st1);
                    preparedStatement.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "SignUp Successful!");
                DashBoard db = new DashBoard();
                dispose();
                db.createDashBoard(uName.getText(), connection);
            } else {
                JOptionPane.showMessageDialog(this, "Passwords not matched!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "This username already exists!", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    //---------------------------------------------------------------------------------------------------------
}
