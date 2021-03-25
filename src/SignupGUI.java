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
    private JTextField emailtextField1;
    private Client client;

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
                String animal="src\\Icons\\wolf.jpg";
                if(wolfRadioButton.isSelected())
                {
                    animal = "src\\Icons\\wolf.jpg";
                }
                if(lionRadioButton.isSelected())
                {
                    animal = "src\\Icons\\lion.jpg";
                }
                if(tigerRadioButton.isSelected())
                {
                    animal = "src\\Icons\\tiger.jpg";
                }
                if(bearRadioButton.isSelected())
                {
                    animal = "src\\Icons\\bear.jpg";
                }
                if(eagleRadioButton.isSelected())
                {
                    animal = "src\\Icons\\eagle.jpg";
                }
                if(horseRadioButton.isSelected())
                {
                    animal = "src\\Icons\\horse.jpg";
                }

                if(userName.getText().length() == 0 && passwordField1 != passwordField2 && passwordField1.getText().length()!=0){
                    JOptionPane.showMessageDialog(signupPanel,"ERROR in signing up. Please try again !");
                    dispose();
                }
                else
                {
                   client.signUp(emailtextField1.getText(),userName.getText(),passwordField1.getText(),bioText.getText(),animal);
                   dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
//        JFrame frame = new JFrame("SignupGUI");
//        frame.setContentPane(new SignupGUI().signupPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
    }
}
