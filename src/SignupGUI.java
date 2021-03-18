import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupGUI extends JFrame {
    private JPanel signupPanel;
    private JButton cancelButton;
    private JButton continueButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField textField2;
    private JPasswordField passwordField2;
    private JRadioButton wolfRadioButton;
    private JRadioButton bullRadioButton;
    private JRadioButton horseRadioButton;
    private JRadioButton eagleRadioButton;
    private JRadioButton lionRadioButton;
    private JRadioButton bearRadioButton;

    public SignupGUI() {
        setContentPane(signupPanel);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupPanel.setVisible(false);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SignupGUI");
        frame.setContentPane(new SignupGUI().signupPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
