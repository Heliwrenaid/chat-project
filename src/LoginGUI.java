import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoginGUI extends JFrame {
    private JPasswordField passwordField1;
    private JTextField nickField1;
    private JPanel loginMain;
    private JButton signInButton;
    private JButton signUpButton;
    private Client client = new Client(System.getProperty("user.home") + File.separator + "ClientData");

    public LoginGUI() {
        setContentPane(loginMain);
        client.startTransmission();

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(client.signIn(nickField1.getText(), passwordField1.getText())) {
                        JFrame f = new MainPanel(client);
                        f.pack();
                        f.setVisible(true);
                    }
                    else {
                        JOptionPane.showMessageDialog(loginMain,"Error! Please try again. ");
                        nickField1.setText("");
                        passwordField1.setText("");
                    }
                }catch (Exception k)
                {
                    JOptionPane.showMessageDialog(loginMain,"Error! Please try again. ");
                    nickField1.setText("");
                    passwordField1.setText("");
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new SignupGUI(client);
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
