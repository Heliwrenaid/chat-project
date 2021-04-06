import javax.swing.*;
import java.awt.*;
import java.io.File;

//https://stackoverflow.com/questions/7306295/swing-jlist-with-multiline-text-and-dynamic-height

public class MessageRenderer extends JPanel implements ListCellRenderer<Object>{
    private DataBase dataBase;
    private JPanel iconPanel;
    private JLabel label;
    private JTextArea textArea;
<<<<<<< HEAD

    public MessageRenderer(DataBase dataBase){

=======
    public MessageRenderer(DataBase dataBase){
>>>>>>> b1ac644cb8519efac1cd4608f1a5632934d18bcd
        this.dataBase = dataBase;
        setLayout(new BorderLayout());

        // iconPanel = avatar + name
        iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(Color.WHITE);
        label = new JLabel();
        iconPanel.add(label, BorderLayout.NORTH);
        add(iconPanel, BorderLayout.WEST);

        // text
        textArea = new JTextArea();
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        add(textArea, BorderLayout.CENTER);
<<<<<<< HEAD


=======
>>>>>>> b1ac644cb8519efac1cd4608f1a5632934d18bcd
    }

    public Component getListCellRendererComponent(JList<? extends Object> list, Object obj, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
<<<<<<< HEAD

=======
>>>>>>> b1ac644cb8519efac1cd4608f1a5632934d18bcd
        String text;
        int userId;
        if(obj == null) return null;
        switch (obj.getClass().getName()){
            case "FileContainer":{
                FileContainer fileContainer = (FileContainer) obj;
                text = fileContainer.getOriginalFileName();
                userId = fileContainer.getUserId();
            }
            break;
            case "Message":{
                Message message = (Message) obj;
                text = message.getMessage();
                userId = message.getUserId();
            }
            break;
            default: {
                text = "Incompatible class";
                userId = 0;
            }
        }

        String avatarSrc;
        String name;
        User user = dataBase.getUser(userId);
        if(user != null) {
            avatarSrc = user.getAvatarSrc();
            name = user.getName();
        } else {
            avatarSrc = "src" + File.separator + "Icons" + File.separator + "wolf.jpg";
            name = "Error 404: User was not found";
        }

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(avatarSrc).getImage().getScaledInstance(20,17,Image.SCALE_DEFAULT));
        // src https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        label.setIcon(imageIcon);
        label.setText(name + ":  ");

        textArea.setText(text);
        int width = list.getWidth();
        // this is just to lure the textArea's internal sizing mechanism into action
        if (width > 0)
            textArea.setSize(width, Short.MAX_VALUE);

        if (isSelected) {
<<<<<<< HEAD
            iconPanel.setBackground(list.getSelectionBackground());
            iconPanel.setForeground(list.getSelectionForeground());
            textArea.setBackground(list.getSelectionBackground());
            textArea.setForeground(list.getSelectionForeground());
        } else {
                iconPanel.setBackground(list.getBackground());
                iconPanel.setForeground(list.getForeground());
                textArea.setBackground(list.getBackground());
                textArea.setForeground(list.getForeground());
=======
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
>>>>>>> b1ac644cb8519efac1cd4608f1a5632934d18bcd
        }
        return this;
    }

}
