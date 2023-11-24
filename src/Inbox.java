import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Inbox extends JFrame implements ActionListener {

    private static int hoveredRow = -1;

    private JTable table;
    private DefaultTableModel model;
    private static Connection connection;
    private String name;

    public void createInboxPage(Connection conn, String username) throws SQLException {
        connection = conn;
        name = username;
        model = new DefaultTableModel();
        String[] columns = {"Date", "From", "Read Message"};
        model.setColumnIdentifiers(columns);
        table = new JTable(model);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(new ButtonEditor());
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                table.repaint();
            }
        });

        table.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);

                if (row != hoveredRow) {
                    hoveredRow = row;
                    table.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // Not used in this example
            }
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (row == hoveredRow) {
                    component.setBackground(Color.LIGHT_GRAY);
                } else {
                    component.setBackground(table.getBackground());
                }
                return component;
            }
        });
        JScrollPane sp = new JScrollPane(table);
        JPanel mainP = new JPanel(new FlowLayout());
        mainP.add(sp);

        String query = "select * from messages;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        int rows = 0;
        while(resultSet.next()){
            if(resultSet.getString(3).equals(name)){
                Object[] data = {
                        resultSet.getString(1),
                        resultSet.getString(2),
                };
                model.addRow(data);
                model.setValueAt(createButton(rows, resultSet.getString(4), resultSet.getString(2)), rows, 2);
                rows++;
            }
        }

        //Frame
        add(mainP);
        setTitle("Inbox");
        setVisible(true);
        setSize(500, 600);
        addWindowListener(new Inbox.MyWindowListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    //---------------------------------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {

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
    private JButton createButton(int i, String st, String name) {
        JButton button = new JButton("Read");
        final JFrame[] showContent = new JFrame[1];
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContent[0] = new JFrame(name);
                showContent[0].setVisible(true);
                showContent[0].setSize(400, 400);
                showContent[0].addWindowListener(new MyListener());
                showContent[0].setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                JPanel content = new JPanel(new BorderLayout());
                JTextArea ta = new JTextArea();
                ta.setWrapStyleWord(true);
                ta.setLineWrap(true);
                ta.setText(st);
                ta.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(ta);
                content.add(scrollPane, BorderLayout.CENTER);
                showContent[0].add(content);
            }

            class MyListener implements WindowListener {
                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    showContent[0].dispose();
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
        });
        return button;
    }
    //---------------------------------------------------------------------------------------------------------

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (Component) value;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private String originalText;

        public ButtonEditor() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                JOptionPane.showMessageDialog(null, "Button Clicked!");
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                button = (JButton) value;
                originalText = button.getText();
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            // Restore the original text when editing is stopped
            button.setText(originalText);
            return button;
        }
    }
    //---------------------------------------------------------------------------------------------------------
}
