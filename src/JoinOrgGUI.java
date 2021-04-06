import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JoinOrgGUI extends JFrame {
    private JPanel panel1;
    private JButton joinButton;
    private JButton cancelButton;
    private JList organizationList;
    private JPanel downPanel;
    private JComboBox comboBox1;
    private JButton filterButton;
    private JPanel nextPanel;
    private JButton download;
    private Client client;

    public JoinOrgGUI(Color darker, Color lighter, Client client) {

        setContentPane(panel1);
        panel1.setBackground(darker);
        organizationList.setBackground(lighter);
        downPanel.setBackground(darker);
        nextPanel.setBackground(darker);

        this.client = client;
        organizationList.setCellRenderer(new ChatRenderer());
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Chat chat = (Chat) organizationList.getSelectedValue();
                    if(client.getActualUser().getSubscribedChats().contains(chat.getId())){
                        JOptionPane.showMessageDialog(panel1,"You have already joined this Chat !");
                    }
                    else {
                        client.joinChat(chat);
                        dispose();
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(panel1,"ERROR! Please try again :) ");
                    dispose();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                organizationList.setModel(readAllChat());
            }
        });
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getAllChats();
            }
        });
    }

    public DefaultListModel<Chat> readAllChat() {
        HashMap<Integer, String> idSet = client.getDataBase().getIdSet();
        String filter = comboBox1.getSelectedItem().toString();

        DefaultListModel<Chat> chatList = new DefaultListModel<>();

        for (Map.Entry<Integer, String> entry : idSet.entrySet()) {
            if (filter.equals("All")) {
                chatList.addElement(client.getDataBase().getChat(entry.getKey()));
            } else if (filter.equals("Channels")) {
                if (entry.getValue().equals("channels")) {
                    chatList.addElement(client.getDataBase().getChat(entry.getKey()));
                }
            } else if (filter.equals("Groups")) {
                if (entry.getValue().equals("groups")) {
                    chatList.addElement(client.getDataBase().getChat(entry.getKey()));
                }
            } else if (filter.equals("Users")) {
                if (entry.getValue().equals("users")) {
                    chatList.addElement(client.getDataBase().getChat(entry.getKey()));
                }
            }
        }
        return chatList;
    }


}
