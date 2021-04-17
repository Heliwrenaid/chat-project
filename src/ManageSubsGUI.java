import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageSubsGUI extends JFrame {
    private JList subscriberList;
    private JLabel upLabel;
    private JLabel downLabel;
    private JButton cancelButton;
    private JPanel mainPanel;
    private Client client;
    private Integer groupId;


    public ManageSubsGUI(Color darker, Color lighter, Client client, Integer chatId) {
        setContentPane(mainPanel);
        this.client = client;
        groupId = chatId;
        Chat group = client.getDataBase().getChat(groupId);
        subscriberList.setCellRenderer(new ChatRenderer(group,"displayRole"));
        mainPanel.setBackground(lighter);
        subscriberList.setBackground(darker);
        upLabel.setBackground(lighter);
        downLabel.setBackground(lighter);

        upLabel.setText(client.getDataBase().getChat(chatId).getName());
        downLabel.setText("Manage your team !");


        subscriberList.setModel(readAllChat());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
               dispose();
            }
        });
        subscriberList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JList lista = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = lista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        User o = (User) lista.getModel().getElementAt(index);
                        System.out.println("Kliknieto: " + o.toString());
                      //  try {
                            //src: https://www.tutorialspoint.com/java-program-to-set-jcombobox-in-joptionpane

                            JComboBox comboBox;
                            if(group instanceof User){
                                Object[] actions = { "Ban","Unban"};
                                comboBox = new JComboBox(actions);
                            } else {
                                Object[] actions = { "Promote", "Demote", "Remove/Leave", "Ban","Unban"};
                                comboBox = new JComboBox(actions);
                            }


                            comboBox.setSelectedIndex(1);
                            JOptionPane.showMessageDialog(null, comboBox, "Team management",
                                    JOptionPane.QUESTION_MESSAGE);

                            String decision = (String)comboBox.getSelectedItem();
                            if(decision.equals("Promote")){
                                client.addAdmin(group,o.getId());
                            }
                            else if(decision.equals("Demote")){
                                client.removeAdmin(group,o.getId());
                            }
                            else if(decision.equals("Remove/Leave")){
                                client.removeUser(group,o.getId());
                            }
                            else if(decision.equals("Ban")){
                                client.banUser(group,o.getId());
                            }
                            else if(decision.equals("Unban")){
                                client.unbanUser(group,o.getId());
                            }
                      //  }catch (Exception p){
                      //      JOptionPane.showMessageDialog(mainPanel,"ERROR! There aren't any groups!");
                       // }
                    }
                }
            }
        });
    }

    public DefaultListModel <Chat> readAllChat() {
        Chat group =  client.getDataBase().getChat(groupId);
        HashMap<Integer, String> idSet = group.getUsers();
        DefaultListModel<Chat> chatList = new DefaultListModel<>();
        for (Map.Entry<Integer, String> entry : idSet.entrySet()) {
            if(!chatList.contains(client.getDataBase().getChat(entry.getKey()))) {
                chatList.addElement(client.getDataBase().getChat(entry.getKey()));
            }
        }
        return chatList;
    }
}
