import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyAccount extends JFrame implements ActionListener {

    private JLabel l1, l2;
    private JTextField t1, t2;
    private JButton changeP;
    private static Connection connection;
    private String name;

    public void showAccountDetails(Connection conn, String username) throws SQLException {
        connection = conn;
        name = username;
        l1 = new JLabel("Username: ");
        l2 = new JLabel("Password: ");
        t1 = new JTextField(20);
        t2 = new JTextField(20);
        t1.setEditable(false);
        t2.setEditable(false);
        changeP = new JButton("Change Password?");
        changeP.addActionListener(this);
        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(l1);
        p1.add(t1);
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(l2);
        p2.add(t2);
        JPanel p3 = new JPanel(new FlowLayout());
        p3.add(changeP);
        JPanel mainP = new JPanel();
        mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.add(p1);
        mainP.add(p2);
        mainP.add(p3);
        showDetails();

        //Frame
        setLayout(new GridLayout(3, 1));
        add(new JPanel());
        add(mainP);
        setTitle("My Account");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new MyAccount.MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        ChangePassword cp = new ChangePassword();
        dispose();
        cp.changePassword(connection, name);//Not completed yet
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
    private void showDetails() throws SQLException {
        String query = "select username, password from users where username = '" + name + "';";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            t1.setText(resultSet.getString(1));
            t2.setText(resultSet.getString(2));
        }
    }
    //---------------------------------------------------------------------------------------------------------
}
