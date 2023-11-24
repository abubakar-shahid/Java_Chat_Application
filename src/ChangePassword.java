import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;

public class ChangePassword extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JPasswordField pass;
    private JPasswordField nPass;
    private JPasswordField cPass;
    private JButton confirm;
    private static Connection connection;
    private String name;

    //---------------------------------------------------------------------------------------------------------
    public void changePassword(Connection conn, String username) {
        connection = conn;
        name = username;
        l1 = new JLabel("Current Password: ");
        l2 = new JLabel("New Password: ");
        l3 = new JLabel("Confirm Password: ");
        pass = new JPasswordField(20);
        nPass = new JPasswordField(20);
        cPass = new JPasswordField(20);
        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(l1);
        jp1.add(pass);
        JPanel jp2 = new JPanel(new FlowLayout());
        jp2.add(l2);
        jp2.add(nPass);
        JPanel jp3 = new JPanel(new FlowLayout());
        jp3.add(l3);
        jp3.add(cPass);
        confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.add(jp1);
        p1.add(jp2);
        p1.add(jp3);
        p1.add(confirm);

        //Frame
        setLayout(new GridLayout(4, 1));
        add(new JPanel());
        add(p1);
        setTitle("Change Password");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new ChangePassword.MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(saveData()){
                JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
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
        String query = "select password from users where username = '" + name + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(new String(pass.getPassword()))) {
                if (new String(nPass.getPassword()).equals(new String(cPass.getPassword()))) {
                    query = "update users set password = ? where username = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, (new String(nPass.getPassword())));
                        preparedStatement.setString(2, name);
                        preparedStatement.executeUpdate();
                    }
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Passwords not matched!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Password!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------------------
}
