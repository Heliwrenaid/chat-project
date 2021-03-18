import javax.swing.*;

public class LoginGUI {
    private JPasswordField passwordField1;
    private JTextField nickField1;
    private JPanel loginMain;
    private JButton signInButton;
    private JButton signUpButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginGUI");
        frame.setContentPane(new LoginGUI().loginMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
