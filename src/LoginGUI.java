import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

public class LoginGUI extends JFrame {
    private JPasswordField passwordField1;
    private JTextField nickField1;
    private JPanel loginMain;
    private JButton signInButton;

    private JButton signUpButton;
    private Client client = null;

    public LoginGUI(Client client) {
        if(this.client == null) this.client = client;
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!client.transmissionIsActive()) client.startTransmission();
                    if(client.signIn(nickField1.getText(), passwordField1.getText())) {
                        JFrame f = new MainPanel(client);
                        f.pack();
                        f.setVisible(true);
                        nickField1.setText("");
                        passwordField1.setText("");
                    }
                    else {
                        JOptionPane.showMessageDialog(loginMain,"Error! Please try again");
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
                if (!client.transmissionIsActive()) client.startTransmission();
                JFrame f = new SignupGUI(client);
                f.pack();
                f.setVisible(true);
            }
        });
    }

    public JPanel getLoginMain() {
        return loginMain;
    }

    public static void main(String[] args) {
        Client client = new Client(System.getProperty("user.home") + File.separator + "ClientData1","localhost",2700);
        JFrame frame = new JFrame(client.getMainDir());
        frame.setContentPane(new LoginGUI(client).loginMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
