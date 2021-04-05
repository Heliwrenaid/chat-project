import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MessageRenderer extends JLabel implements ListCellRenderer<Object>{
    private DataBase dataBase;
    public MessageRenderer(DataBase dataBase){
        this.dataBase = dataBase;
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<? extends Object> list, Object obj, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        String text;
        int userId;
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
        setIcon(imageIcon);
        setText(name + ": " + text);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

}
