import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupGUI extends JFrame {
    private JPanel signupPanel;
    private JButton cancelButton;
    private JButton continueButton;
    private JTextField userName;
    private JPasswordField passwordField1;
    private JTextField bioText;
    private JPasswordField passwordField2;
    private JRadioButton wolfRadioButton;
    private JRadioButton tigerRadioButton;
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
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!passwordField1.getPassword().equals(passwordField2.getPassword()) || userName.getText().length() == 0){
                    JOptionPane.showMessageDialog(signupPanel,"ERROR in signing up. Please try again !");
                    signupPanel.setVisible(false);
                }
                else
                {
                    User user = new User(userName.getText(),passwordField1.getPassword().toString(),90,null,null,null);
                    signupPanel.setVisible(false);

                }
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
