import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class NewOrgGUI extends JFrame{
    private JTextField nameField;
    private JButton createButton;
    private JButton cancelButton;
    private JRadioButton channelRadioButton;
    private JRadioButton groupRadioButton;
    private JTextField descriptionField;
    private JPanel newOrgPanel;
    private JPanel downPanel;
    private JPanel downsecondPanel;
    private JRadioButton greenRadioButton;
    private JRadioButton redRadioButton;
    private JRadioButton blueRadioButton;
    private JRadioButton yellowRadioButton;
    private String avatarSrc;
    private Client client;

    public NewOrgGUI(Color darker,Color lighter,Client client, JList groupList) {
        this.client=client;
        setContentPane(newOrgPanel);

        newOrgPanel.setBackground(darker);
        nameField.setBackground(lighter);
        descriptionField.setBackground(lighter);
        downPanel.setBackground(lighter);
        downsecondPanel.setBackground(lighter);
        channelRadioButton.setBackground(lighter);
        groupRadioButton.setBackground(lighter);
        avatarSrc="src\\Icons\\red_color.png";

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }

        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(channelRadioButton.isSelected()){
                        if(greenRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\green_color.png";
                        }
                        else if(redRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\red_color.png";
                        }
                        else if(blueRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\blue_color.png";
                        }
                        else if(yellowRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\yellow_color.png";
                        }
                        client.createGroup(nameField.getText(),descriptionField.getText(),avatarSrc,"channel");
                        dispose();
                    }
                    if(groupRadioButton.isSelected()){
                        if(greenRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\green_color.png";
                        }
                        else if(redRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\red_color.png";
                        }
                        else if(blueRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\blue_color.png";
                        }
                        else if(yellowRadioButton.isSelected()){
                            avatarSrc="src\\Icons\\yellow_color.png";
                        }
                        client.createGroup(nameField.getText(),descriptionField.getText(),avatarSrc,"group");
                        dispose();
                    }
                }catch (Exception m){
                    System.out.println("ERROR ! "+m.getMessage());
                }
                dispose();
            }
        });
    }

}
