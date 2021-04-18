import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

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
    private JTextField emailtextField1;
    private JButton yourAvatarButton;
    private JLabel avatarPath;
    private Client client;
    String animal;

    public SignupGUI(Client client) {
        this.client=client;

        setContentPane(signupPanel);


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(wolfRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\wolf.jpg";
                }
                if(lionRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\lion.jpg";
                }
                if(tigerRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\tiger.jpg";
                }
                if(bearRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\bear.jpg";
                }
                if(eagleRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\eagle.jpg";
                }
                if(horseRadioButton.isSelected() && animal==null)
                {
                    animal = "src\\Icons\\horse.jpg";
                }

                if(userName.getText().length() == 0){
                    promptError();
                } else if(!Arrays.equals(passwordField2.getPassword(),passwordField1.getPassword())){
                   promptError();
                } else if(passwordField1.getPassword().length == 0 || passwordField2.getPassword().length == 0){
                   promptError();
                } else if (animal == null){
                    promptError();
                } else {
                   client.signUp(emailtextField1.getText(),userName.getText(),new String(passwordField1.getPassword()),bioText.getText(),animal);
                   dispose();
                }
            }
        });
        yourAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    File plik = fc.getSelectedFile();
                    animal=plik.getAbsolutePath();
                    avatarPath.setText(animal);
                }
            }
        });
    }
    public void promptError(){
        JOptionPane.showMessageDialog(signupPanel,"ERROR in signing up. Please try again !");
        dispose();
    }
}
