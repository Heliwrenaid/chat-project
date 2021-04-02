import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SettingsGUI extends JFrame{

    private JPanel settingsPanel;
    private JPasswordField passwordField1;
    private JTextField nicktextField1;
    private JPasswordField passwordField2;
    private JTextField biotextField2;
    private JRadioButton eagleRadioButton;
    private JRadioButton tigerRadioButton;
    private JRadioButton lionRadioButton;
    private JRadioButton horseRadioButton;
    private JRadioButton bearRadioButton;
    private JRadioButton wolfRadioButton;
    private JButton continueButton;
    private JButton cancelButton;
    private JPanel downPanel;
    private JPanel upPanel;
    private JTextField emailtextField1;
    private JButton chooseYourAvatarButton;
    private JLabel avatarLabel;
    private Client client;
    private String str;
    private String animal;

    public SettingsGUI(Color darker, Color lighter,Client client) {

        setContentPane(settingsPanel);
        this.client=client;
        downPanel.setBackground(darker);
        upPanel.setBackground(darker);
        settingsPanel.setBackground(darker);
        passwordField1.setBackground(lighter);
        nicktextField1.setBackground(lighter);
        biotextField2.setBackground(lighter);
        emailtextField1.setText(client.getActualUser().getEmail());
        nicktextField1.setText(client.getActualUser().getName());
        passwordField1.setText(client.getActualUser().getPassword());
        biotextField2.setText(client.getActualUser().getBio());
        str=client.getActualUser().getAvatarSrc();


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // haslo=JOptionPane.showInternalInputDialog(settingsPanel,"Wprowadź stare hasło!");
                String haslo = JOptionPane.showInputDialog(settingsPanel,"Confirm old password: ");
                if(haslo.equals(client.getActualUser().getPassword())){
                    if(wolfRadioButton.isSelected() && animal == null)
                    {
                        animal = "src\\Icons\\wolf.jpg";
                    }
                    if(lionRadioButton.isSelected() && animal == null )
                    {
                        animal = "src\\Icons\\lion.jpg";
                    }
                    if(tigerRadioButton.isSelected() && animal == null )
                    {
                        animal = "src\\Icons\\tiger.jpg";
                    }
                    if(bearRadioButton.isSelected() && animal == null)
                    {
                        animal = "src\\Icons\\bear.jpg";
                    }
                    if(eagleRadioButton.isSelected() && animal == null)
                    {
                        animal = "src\\Icons\\eagle.jpg";
                    }
                    if(horseRadioButton.isSelected() && animal == null)
                    {
                        animal = "src\\Icons\\horse.jpg";
                    }
                    client.updateUser(nicktextField1.getText(),passwordField1.getText(),biotextField2.getText(),animal);
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(settingsPanel,"ERROR! Please try again !");
                }
            }
        });
        chooseYourAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File plik = fc.getSelectedFile();
                    animal = plik.getAbsolutePath();
                    avatarLabel.setText("Selected Avatar: "+animal);
                }
            }
        });
    }
}
