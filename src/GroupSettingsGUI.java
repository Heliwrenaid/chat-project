import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GroupSettingsGUI extends JFrame{
    private JTextField nameField;
    private JTextField descriptionField;
    private JRadioButton redRadioButton;
    private JRadioButton greenRadioButton;
    private JRadioButton blueRadioButton;
    private JRadioButton yellowRadioButton;
    private JLabel topPanel;
    private JButton continueButton;
    private JButton cancelButton;
    private JLabel currentPath;
    private JButton chooseYourOwnAvatarButton;
    private JPanel mainPanel;
    private Client client;
    private String avatarSrc=null;

    public GroupSettingsGUI(Color darker, Color lighter, Client client, Integer chatId) {
        this.client=client;
        mainPanel.setBackground(darker);
        topPanel.setBackground(darker);
        currentPath.setBackground(darker);
        nameField.setBackground(lighter);
        descriptionField.setBackground(lighter);
        nameField.setText(client.getDataBase().getChat(chatId).getName());
        descriptionField.setText(client.getDataBase().getChat(chatId).getBio());

        setContentPane(mainPanel);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        chooseYourOwnAvatarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File plik = fc.getSelectedFile();
                    avatarSrc = plik.getAbsolutePath();
                    currentPath.setText(avatarSrc);
                }
            }

        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(redRadioButton.isSelected() & avatarSrc == null)
                    {
                        avatarSrc = "src\\Icons\\red_color.png";
                    }
                    if(yellowRadioButton.isSelected() && avatarSrc == null)
                    {
                        avatarSrc = "src\\Icons\\yellow_color.png";
                    }
                    if(greenRadioButton.isSelected() && avatarSrc == null)
                    {
                        avatarSrc = "src\\Icons\\green_color.png";
                    }
                    if(blueRadioButton.isSelected() && avatarSrc == null)
                    {
                        avatarSrc = "src\\Icons\\blue_color.png";
                    }
                    String haslo = JOptionPane.showInputDialog("Confirm your password !");
                    if(haslo.equals(client.getActualUser().getPassword())){
                        client.updateGroup(chatId,nameField.getText(),descriptionField.getText(),avatarSrc);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(mainPanel,"ERROR! Please try again!");
                    }

                }catch (Exception exception){
                    JOptionPane.showMessageDialog(mainPanel,"ERROR! Please try again!");
                }

            }
        });
    }
}
