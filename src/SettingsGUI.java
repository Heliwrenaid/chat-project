import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  //  private String haslo;



    public SettingsGUI(Color darker, Color lighter) {

        setContentPane(settingsPanel);
        downPanel.setBackground(darker);
        upPanel.setBackground(darker);
        settingsPanel.setBackground(darker);
        passwordField1.setBackground(lighter);
        nicktextField1.setBackground(lighter);
        passwordField2.setBackground(lighter);
        biotextField2.setBackground(lighter);


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
            }
        });
    }
}
