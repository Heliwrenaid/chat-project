import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    User user = new User("Jan","abc",15,null,"Lubie placki","C:\\Users\\janfi\\Pictures\\pau.jpg");
    DataBase db = new DataBase("C:\\Users\\janfi\\Pictures\\");
    private JPanel leftFrame;
    private JPanel rightFrame;
    private JPanel modesPanel;
    private JPanel groupsPanel;
    private JPanel headPanel;
    private JPanel messagePanel;
    private JPanel typePanel;
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
    private JLabel groupLabel;
    private JLabel avatarIcon;
    private JTextArea messageTextArea;

    public MainPanel() {
        ImageIcon icon = new ImageIcon(user.getAvatarSrc());
        avatarIcon.setIcon(icon);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textMessageArea.append(user.getName()+": "+chatField.getText()+"\n\n");
                chatField.setText("");
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(info,"Tutaj pojawią się podstawowe informacje o koncie");

            }
        });
    }
//
    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPanel");
        frame.setContentPane(new MainPanel().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    JTextArea makeBubble(String a){
        messageTextArea.setText(a);
        return messageTextArea;
    }


}
