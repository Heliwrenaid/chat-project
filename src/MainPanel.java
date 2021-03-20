import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPanel extends JFrame {

   // User user = new User("Jan","abc",60,null,"Lubie placki","src\\Icons\\wolf.jpg");
   // DataBase db = new DataBase("C:\\Users\\janfi");
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
    private JComboBox comboBox1;
    private JTextArea textMessageArea;
    private JLabel avatarIcon;
    private JButton logOutButton;
    private JButton newGroupButton;
    private JLabel komunikatorField;
    private JLabel infoField;
    private JButton darkModebutton1;
    private JLabel organizName;
    private JLabel organDescrip;
    private JTextArea messageTextArea;
    private Client client;


    public MainPanel(Client client) {
        this.client=client;

//       infoField.setText("Witaj "+client.getActualUser().getName()+"!");
//        ImageIcon icon = new ImageIcon(client.getActualUser().getAvatarSrc());
//        avatarIcon.setIcon(icon);

        setContentPane(mainPanel);


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textMessageArea.append(client.getActualUser().getName()+": "+chatField.getText()+"\n\n");
                chatField.setText("");
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(info,"Project created for JPWP subject. AGH University of Science and Technology.\n Contact:\n Jan Sciga-sciga@student.agh.edu.pl \n Michał Kurdziel- mkurdziel@student.agh.edu.pl ");
                dispose();
            }
        });
        newGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new NewOrgGUI(mainPanel.getBackground(),listChannel.getBackground());
                f.pack();
                f.setVisible(true);
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new LoginGUI();
                f.pack();
                f.setVisible(true);
                dispose();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new SettingsGUI(mainPanel.getBackground(),listChannel.getBackground());
                f.pack();
                f.setVisible(true);
            }
        });
        darkModebutton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainPanel.getBackground()!=Color.gray) {
                    mainPanel.setBackground(Color.gray);
                    leftFrame.setBackground(Color.gray);
                    rightFrame.setBackground(Color.gray);
                    modesPanel.setBackground(Color.gray);
                    groupsPanel.setBackground(Color.gray);
                    messagePanel.setBackground(Color.GRAY);
                    listChannel.setBackground(Color.lightGray);
                    listGroup.setBackground(Color.lightGray);
                    searchMsgField.setBackground(Color.lightGray);
                    chatField.setBackground(Color.lightGray);
                    comboBox1.setBackground(Color.lightGray);
                    textMessageArea.setBackground(Color.lightGray);
                }
                else{
                    mainPanel.setBackground(Color.lightGray);
                    leftFrame.setBackground(Color.lightGray);
                    rightFrame.setBackground(Color.lightGray);
                    modesPanel.setBackground(Color.lightGray);
                    groupsPanel.setBackground(Color.lightGray);
                    messagePanel.setBackground(Color.lightGray);
                    listChannel.setBackground(Color.WHITE);
                    listGroup.setBackground(Color.WHITE);
                    searchMsgField.setBackground(Color.WHITE);
                    chatField.setBackground(Color.WHITE);
                    comboBox1.setBackground(Color.WHITE);
                    textMessageArea.setBackground(Color.WHITE);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Highlighter.HighlightPainter painter =
                        new DefaultHighlighter.DefaultHighlightPainter( Color.green );
                
                int offset = textMessageArea.getText().indexOf(searchMsgField.getText());
                int length = searchMsgField.getText().length();

                while ( offset != -1)
                {
                    try
                    {
                        textMessageArea.getHighlighter().addHighlight(offset, offset + length, painter);
                        offset = searchMsgField.getText().indexOf(searchMsgField.getText(), offset+1);
                    }
                    catch(BadLocationException ble) { System.out.println(ble); }
                }
            }
        });
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoField.setText("Witaj "+client.getActualUser().getName()+"!");
                ImageIcon icon = new ImageIcon(client.getActualUser().getAvatarSrc());
                avatarIcon.setIcon(icon);
            }
        });
    }

//
    public static void main(String[] args) {
//        JFrame frame = new JFrame("MainPanel");
//        frame.setContentPane(new MainPanel().mainPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
    }




}
