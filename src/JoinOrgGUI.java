import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JoinOrgGUI extends JFrame {
    private JPanel panel1;
    private JButton joinButton;
    private JButton cancelButton;
    private JList organizationList;
    private JPanel downPanel;
    private Client client;

    public JoinOrgGUI(Color darker,Color lighter,Client client,JList myJlist) {
        this.client=client;
        setContentPane(panel1);
        organizationList.setModel(readAllChat());
        panel1.setBackground(darker);
        organizationList.setBackground(lighter);
        downPanel.setBackground(darker);

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chat chat = (Chat) organizationList.getSelectedValue();
                myJlist.setModel(readAllChat());
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public DefaultListModel <Chat> readAllChat() {
        ArrayList<Integer> subscribedChats  =  client.getActualUser().getSubscribedChats();

        // DefaultListModel <Integer> chatList = new DefaultListModel<>();
        DefaultListModel <Chat> chatList = new DefaultListModel<>();

        for(int m : subscribedChats){
            chatList.addElement(client.getDataBase().getChat(m));
        }
        return chatList;
    }
}
