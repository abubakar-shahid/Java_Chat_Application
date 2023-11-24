import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends JFrame implements ActionListener {
    private JPanel p1;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JTextField uName;
    private JPasswordField pass;
    private JButton login;
    private JButton signUp;
    private static Connection connection;
    private String name;

    //---------------------------------------------------------------------------------------------------------
    public void createLoginPage(final Connection conn) {
        connection = conn;
        l1 = new JLabel("Username: ");
        l2 = new JLabel("Password: ");
        l3 = new JLabel("Don't have an account?");
        uName = new JTextField(20);
        pass = new JPasswordField(20);
        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(l1);
        jp1.add(uName);
        JPanel jp2 = new JPanel(new FlowLayout());
        jp2.add(l2);
        jp2.add(pass);
        login = new JButton("Login");
        login.addActionListener(this);
        JPanel jp3 = new JPanel(new FlowLayout());
        jp3.add(login);
        signUp = new JButton("SignUp");
        signUp.addActionListener(this);
        JPanel jp4 = new JPanel(new FlowLayout());
        jp4.add(l3);
        jp4.add(signUp);
        p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.add(jp1);
        p1.add(jp2);
        p1.add(jp3);
        p1.add(jp4);

        //Frame
        setLayout(new GridLayout(3, 1));
        add(new JPanel());
        add(p1);
        setTitle("Login Page");
        setVisible(true);
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "SignUp") {
            SignUp signUp1 = new SignUp();
            signUp1.createSignUpPage(connection);
            //dispose();
        } else {
            try {
                if (checkLoginCredentials()) {
                    JOptionPane.showMessageDialog(this, "LogIn Successful!");
                    DashBoard db = new DashBoard();
                    db.createDashBoard(name, connection);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Username or Password!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------
    private boolean checkLoginCredentials() throws SQLException {
        String query = "select * from users;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String user = resultSet.getString(1);
            String p = resultSet.getString(2);
            char[] passwordChars = pass.getPassword();
            String st = new String(passwordChars);
            if (user.equals(uName.getText()) && p.equals(st)) {
                name = uName.getText();
                return true;
            }
        }
        return false;
    }
    //---------------------------------------------------------------------------------------------------------
}
