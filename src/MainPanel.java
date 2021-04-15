import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.tools.JavaFileManager;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private JButton groupManagement;
    private JList messageList;
    private int actualGroupId;
    private Chat actualGroup;
    private JScrollPane scrollpane;
    private boolean status=true;
    private boolean refreshStatus=true;
    private volatile boolean execute=true;
    private Client client;


    public MainPanel(Client client) {
        this.client = client;
        setContentPane(mainPanel);
        startRefreshing();
        infoField.setText("Hello " + client.getActualUser().getName() + "!");
        ImageIcon icon = new ImageIcon(new ImageIcon(client.getActualUser().getAvatarSrc()).getImage().getScaledInstance(140,93,Image.SCALE_DEFAULT));
        avatarIcon.setIcon(icon);
        bioLabel.setText(client.getActualUser().getBio());
        messageList.setCellRenderer(new MessageRenderer(client.getDataBase()));
        setTitle("Account: "+client.getActualUser().getName() + ", id: " +  client.getActualUser().getId());

//        ComponentListener l = new ComponentAdapter() {
//
//            @Override
//            public void componentResized(ComponentEvent e) {
//                // next line possible if list is of type JXList
//                 //messageList.invalidateCellSizeCache();
//                // for core: force cache invalidation by temporarily setting fixed height
//                messageList.setFixedCellHeight(10);
//                messageList.setFixedCellHeight(-1);
//            }
//
//        };
//        messageList.addComponentListener(l);
        //add(new JScrollPane(messageList));
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if(client.transmissionIsActive()) {
                    execute=false;
                    client.logout();
                    dispose();
                }
            }
        });


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!chatField.getText().equals("")) {
                    if(actualGroup!=null) {
                        client.sendMessage(chatField.getText(), actualGroupId);
                        chatField.setText("");
                    }
                    else{
                        JOptionPane.showMessageDialog(mainPanel,"ERROR ! Please select your chat from list. (double click)");
                        chatField.setText("");
                    }
                }
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.downloadAllData();
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
                client.logout();
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

                    messageList.setBackground(Color.lightGray);



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
                    messageList.setBackground(Color.white);


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

                        //TODO: for testing ----------------------------
//                        ArrayList<Integer> arr = new ArrayList<>();
//                        for(int i = 1; i <=6 ;i++) arr.add(i);
//                        o.setMessages(arr);
//                        o.save();
                        // ---------------------------------------------

                        try {
                            Chat o = (Chat) lista.getModel().getElementAt(index);
                            if(o instanceof User){
                                status=false;
                            }
                            else {
                                status=true;
                            }
                            actualGroup = o;
                            actualGroupId=o.getId();
                            System.out.println("Kliknieto: " + o.toString());
                            organizName.setText(client.getDataBase().getChat(o.getId()).toString());
                            organDescrip.setText(client.getDataBase().getChat(o.getId()).getBio());
                        }catch (Exception p){
                            JOptionPane.showMessageDialog(mainPanel,"ERROR! There aren't any chats!");
                        }
                    }
                }
            }
        });
        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JList lista = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = lista.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Object obj = lista.getModel().getElementAt(index);
                        if (obj instanceof FileContainer){
                            FileContainer file = (FileContainer) obj;

                            if(client.checkIfFileIsDownloaded(file)){
                                Object[] options = {"Redownload",
                                        "Save as",
                                        "Cancel"};
                                int n = JOptionPane.showOptionDialog(mainPanel,
                                        "What would you like to do with your file ?",
                                        "Important question",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[2]);
                                // buttony: redownload, save as, cancel
                                // file.getOriginalFileName() is already download
                                if(n==0){
                                    client.getFile(file);
                                }
                                if(n==1){
//                                    Functions.saveFile(new JFileChooser(),file);
                                   JFileChooser fileChooser= new JFileChooser();
                                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                                        String destinationPath=fileChooser.getSelectedFile().getAbsolutePath();
                                        try {
                                            Chat chat = client.getDataBase().getChat(file.getDestId());
                                            Files.copy(Paths.get(chat.getFileDir()+File.separator+file.getOriginalFileName()), Paths.get(destinationPath));
                                        }
                                        catch (Exception exe){
                                            System.out.println("ERROR in copying file");
                                    }
                                    }
                                }
                            }
                            else {
                                // button: download, cancel
                               // client.getFile(file);
                                Object[] options = {"Download",
                                        "Cancel"};
                                int n = JOptionPane.showOptionDialog(mainPanel,
                                        "What would you like to do with your file",
                                        "Important question",
                                        JOptionPane.YES_NO_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        options,
                                        options[1]);
                                if(n==0){
                                    client.getFile(file);
                                }
                            }
                            //TODO: tutaj
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
                    JOptionPane.showMessageDialog(mainPanel,"ERROR ! Please select your chat from list ! (double click)");
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
                    JOptionPane.showMessageDialog(mainPanel,"ERROR ! Please select your chat from list ! (double click) ");
                }
            }
        });
        FileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(actualGroup!=null) {
                    JFileChooser fc = new JFileChooser();
                    if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File plik = fc.getSelectedFile();
                        client.sendFile(plik.getAbsolutePath(), actualGroupId);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel,"ERROR! Please select your group from list! (double click)");
                }

            }
        });
        groupManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Chat o = (Chat) listGroup.getSelectedValue();
                    JFrame f = new GroupSettingsGUI(mainPanel.getBackground(), listGroup.getBackground(), client, o.getId());
                    f.pack();
                    f.setVisible(true);
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(mainPanel,"ERROR! Please select your chat from list (double click) !");

                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String findString = searchMsgField.getText();
                ListModel listModel= messageList.getModel();
                ArrayList <Integer> list = new ArrayList<>();
                for(int i = 0;i<listModel.getSize();i++){
                    if(listModel.getElementAt(i).toString().contains(findString)){
                       list.add(i);
                    }
                }
                messageList.setSelectedIndices(convertIntegers(list));
                System.out.println(list);
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

    public DefaultListModel <Object> readAllMessages(int groupId) {
        System.out.println("groupMessages -------------- : " + groupId);
        ArrayList <Integer> messages  =  client.getDataBase().getChat(groupId).getMessages();
        DefaultListModel <Object> messageList = new DefaultListModel<>();

        for(int m : messages){
            if(!messageList.contains(m)) {
                Object obj = client.getDataBase().getChat(groupId).getMessage(m);
                if(obj != null) {
                    System.out.println("ssss");
                    messageList.addElement(obj);}
            }
        }
        return messageList;
    }

    public DefaultListModel <Object> readAllUserMessages(int userId) {
        ArrayList <Integer> messages  =  client.getDataBase().getChat(userId).getMessages();
        DefaultListModel <Object> messageList = new DefaultListModel<>();
        System.out.println("UserMessages -------------------- : " + userId);
        for(int m : messages){
            if(!messageList.contains(m)) {

                Object p = client.getDataBase().getChat(userId).getMessage(m);
                if(p != null) {
                    int id = 0;
                    int id2 = 0;
                    switch (p.getClass().getName()) {
                        case "FileContainer": {
                            id = ((FileContainer) p).getUserId();
                            id2 = Integer.parseInt(((FileContainer) p).getInfo1());
                        }
                        break;
                        case "Message": {
                           // ((Message)p).print();
                            id = ((Message) p).getUserId();
                            id2 = ((Message) p).getInfo1();
                        }
                        break;
                    }
                    if (((client.getActualUser().getId() == id || client.getActualUser().getId() == id2) && ( actualGroupId == id || actualGroupId == id2))){// &&
                        Object obj = client.getDataBase().getChat(userId).getMessage(m);
                        if(obj != null) messageList.addElement(obj);
                    }
                }
            }
        }
        return messageList;
    }


    void startRefreshing(){
        Runnable listener = new Runnable() {
            public void run() {
                while (execute) {
                    try {
                        if(searchMsgField.getText().equals("") || searchMsgField.getText().equals("Search...")){
                            refreshStatus=true;
                        }
                        else {
                            refreshStatus=false;
                        }
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
        try {
            if (listGroup.getModel().getSize() != client.getActualUser().getSubscribedChats().size()) {
                listGroup.setModel(readAllChat());
            }

            actualGroup=(Chat)listGroup.getSelectedValue();
             if (checkStatus()) {
               if(refreshStatus) {
                   if (status) {
                       messageList.setModel(readAllMessages(actualGroupId));
                   } else {
                       messageList.setModel(readAllUserMessages(actualGroupId));
                   }
               }
           }

            listGroup.setCellRenderer(new ChatRenderer());
            infoField.setText("Hello " + client.getActualUser().getName() + "!");
            ImageIcon icon = new ImageIcon(new ImageIcon(client.getActualUser().getAvatarSrc()).getImage().getScaledInstance(140, 93, Image.SCALE_DEFAULT));
            avatarIcon.setIcon(icon);
            bioLabel.setText(client.getActualUser().getBio());
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

    }
    public boolean checkStatus(){
        if (actualGroupId == 0) return false;
        Chat chat = client.getDataBase().getChat(actualGroupId);
        if(chat == null) return false;
        ArrayList<Integer> arrayList = chat.getMessages();
        if (arrayList == null) return false;

        ListModel listModel = messageList.getModel();
        int minSize = -1;
        if(listModel.getSize() != arrayList.size() ){
            return true;
        }
        else {
            if(listModel.getSize() > arrayList.size() ){
                minSize = arrayList.size();
            }
            else {
               minSize = listModel.getSize();
            }
            if(listModel != null){
                for(int i = 0; i < minSize; i++){
                    Object obj = chat.getMessage(i+1);
                    Object listObj = listModel.getElementAt(i);
                    if(obj != null) {
                        if (obj.getClass().getName() != listObj.getClass().getName()) return true;
                        if (obj instanceof Message){
                            Message message = (Message) obj;
                            Message listMessage = (Message) listObj;
                            if (!message.getMessage().equals(listMessage.getMessage())) return true;
                            if (message.getInfo1() != listMessage.getInfo1()) return true;
                            if (message.getUserId() != listMessage.getUserId()) return true;
                        }
                        if (obj instanceof FileContainer){
                            FileContainer fc = (FileContainer) obj;
                            FileContainer listFc = (FileContainer) listObj;
                            if (! fc.getOriginalFileName().equals(listFc.getOriginalFileName())) return true;
                            if(! fc.getInfo1().equals(listFc.getInfo1())) return true;
                            if (fc.getUserId() != listFc.getUserId()) return true;
                        }
                    }
                }
            }
        }
        return false;

    }


    public static int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public static void main(String[] args) {
    }




}
