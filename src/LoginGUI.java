import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPasswordField passwordField1;
    private JTextField nickField1;
    private JPanel loginMain;
    private JButton signInButton;
    private JButton signUpButton;
    private Client client = new Client("C:\\Users\\janfi");

    public LoginGUI() {
        setContentPane(loginMain);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //if(warunki logowania)
                JFrame f = new MainPanel();
                f.pack();
                f.setVisible(true);
            //}else{
                //
                //}
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new SignupGUI();
                f.pack();
                f.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginGUI");
        frame.setContentPane(new LoginGUI().loginMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
