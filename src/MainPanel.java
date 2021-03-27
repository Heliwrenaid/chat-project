import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MainPanel extends JFrame {

    private JPanel leftFrame;
    private JPanel rightFrame;
    private JPanel modesPanel;
    private JPanel groupsPanel;
    private JPanel messagePanel;
    private JTextField chatField;
    private JButton FileButton;
    private JButton sendButton;
    private JPanel mainPanel;
    private JButton info;
    private JTextField searchMsgField;
    private JButton settingsButton;
    private JButton joinButton;
    private JPanel avatarField;
    private JList listChannel;
    private JList listGroup ;
    private JButton searchButton;
    private JTextArea textMessageArea;
    private JLabel avatarIcon;
    private JButton logOutButton;
    private JButton newGroupButton;
    private JLabel komunikatorField;
    private JLabel infoField;
    private JButton darkModebutton1;
    private JLabel organizName;
    private JLabel organDescrip;
    private JLabel bioLabel;
    private JButton buttonDelete;
    private JButton personButton;
    private JTextArea messageTextArea;
    private volatile boolean execute=true;

    private Client client;


    public MainPanel(Client client) {
        this.client = client;

        setContentPane(mainPanel);


        startRefreshing();

        infoField.setText("Hello " + client.getActualUser().getName() + "!");
        ImageIcon icon = new ImageIcon(client.getActualUser().getAvatarSrc());
        avatarIcon.setIcon(icon);
        bioLabel.setText(client.getActualUser().getBio());

//        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                execute=false;
//            }
//        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textMessageArea.append(client.getActualUser().getName() + ": " + chatField.getText() + "\n\n");
                chatField.setText("");
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(info, "Project created for JPWP. AGH University of Science and Technology.\n Contact:\n Jan Sciga-sciga@student.agh.edu.pl \n Michał Kurdziel- mkurdziel@student.agh.edu.pl ");
            }
        });

        newGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new NewOrgGUI(mainPanel.getBackground(), listGroup.getBackground(),client,listGroup);
                f.pack();
                f.setVisible(true);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute=false;
                dispose();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new SettingsGUI(mainPanel.getBackground(), listGroup.getBackground(), client);
                f.pack();
                f.setVisible(true);
            }
        });
        darkModebutton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainPanel.getBackground() != Color.gray) {
                    mainPanel.setBackground(Color.gray);
                    leftFrame.setBackground(Color.gray);
                    rightFrame.setBackground(Color.gray);
                    modesPanel.setBackground(Color.gray);
                    groupsPanel.setBackground(Color.gray);
                    messagePanel.setBackground(Color.GRAY);
                    listGroup.setBackground(Color.lightGray);
                    searchMsgField.setBackground(Color.lightGray);
                    chatField.setBackground(Color.lightGray);
                    textMessageArea.setBackground(Color.lightGray);
                } else {
                    mainPanel.setBackground(Color.lightGray);
                    leftFrame.setBackground(Color.lightGray);
                    rightFrame.setBackground(Color.lightGray);
                    modesPanel.setBackground(Color.lightGray);
                    groupsPanel.setBackground(Color.lightGray);
                    messagePanel.setBackground(Color.lightGray);
                    listGroup.setBackground(Color.WHITE);
                    searchMsgField.setBackground(Color.WHITE);
                    chatField.setBackground(Color.WHITE);
                    textMessageArea.setBackground(Color.WHITE);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter(Color.green);
                //src: https://stackoverflow.com/questions/5909419/searching-for-words-in-textarea
                int offset = textMessageArea.getText().indexOf(searchMsgField.getText());
                int length = searchMsgField.getText().length();

                while (offset != -1) {
                    try {
                        textMessageArea.getHighlighter().addHighlight(offset, offset + length, painter);
                        offset = textMessageArea.getText().indexOf(searchMsgField.getText(), offset + 1);
                    } catch (BadLocationException ble) {
                        System.out.println(ble);
                    }
                }

            }
        });
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JoinOrgGUI(mainPanel.getBackground(), listGroup.getBackground(),client);
                f.pack();
                f.setVisible(true);
            }
        });
        textMessageArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                infoField.setText("Witaj " + client.getActualUser().getName() + "!");
                ImageIcon icon = new ImageIcon(client.getActualUser().getAvatarSrc());
                avatarIcon.setIcon(icon);
                textMessageArea.getHighlighter().removeAllHighlights();
                listGroup.setModel(readAllChat());
            }
        });
        searchMsgField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                searchMsgField.setText("");
            }
        });
        chatField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                chatField.setText("");
            }
        });

        listGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JList lista = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = lista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Chat o = new Chat();
                        o = (Chat) lista.getModel().getElementAt(index);
                        System.out.println("Kliknieto: " + o.toString());
                        try {
                            organizName.setText(o.toString());
                            organDescrip.setText(o.getBio());
                        }catch (Exception p){
                            JOptionPane.showMessageDialog(mainPanel,"ERROR! There aren't any groups!");
                        }
                    }
                }
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Chat o = (Chat) listGroup.getSelectedValue();
                    client.getActualUser().unsubscribeChat(o.getId());
                    client.leaveChat(o);
                }catch (Exception exc){
                    JOptionPane.showMessageDialog(mainPanel,"ERROR ! Please try again ! ");
                }
            }
        });

        personButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Chat o = (Chat) listGroup.getSelectedValue();
                    JFrame f = new ManageSubsGUI(mainPanel.getBackground(), listGroup.getBackground(),client,o.getId());
                    f.pack();
                    f.setVisible(true);
                }catch (Exception exc){
                    JOptionPane.showMessageDialog(mainPanel,"ERROR ! Please try again ! ");
                }
            }
        });
    }

    public DefaultListModel <Chat> readAllChat() {
        ArrayList <Integer> subscribedChats  =  client.getActualUser().getSubscribedChats();

        DefaultListModel <Chat> chatList = new DefaultListModel<>();

        for(int m : subscribedChats){
            if(!chatList.contains(client.getDataBase().getChat(m))) {
                chatList.addElement(client.getDataBase().getChat(m));
            }
        }
        return chatList;
    }

    void startRefreshing(){
        Runnable listener = new Runnable() {
            public void run() {
                while (execute) {
                    try {
                        refresh();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    void refresh(){
        if(listGroup.getModel().getSize()!=client.getActualUser().getSubscribedChats().size()) {
            listGroup.setModel(readAllChat());
        }
    }

    public static void main(String[] args) {
    }




}
