import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChatRenderer extends JLabel implements ListCellRenderer<Chat>{
    private String mode = "default";
    private Chat group;
    public ChatRenderer(Chat group,String mode){
        this.group = group;
        this.mode = mode;
        setOpaque(true);
    }
    public ChatRenderer(){
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<? extends Chat> list, Chat chat, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        String text;
        String avatarSrc = null;
        if(chat != null){
            avatarSrc = chat.getAvatarSrc();
            if(mode.equals("displayRole")){
                if (group == null){
                    text = chat.getName() + "  " + "error";
                }
                text = chat.getName() + "  " + group.getUsers().get(chat.getId());
            } else{
                text = chat.getName();
            }

        }
        else {
            text = "Error 404: Chat not found";
        }
        if (avatarSrc == null) avatarSrc = "src" + File.separator + "Icons" + File.separator + "wolf.jpg";

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(avatarSrc).getImage().getScaledInstance(20,17,Image.SCALE_DEFAULT));
        // src https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        setIcon(imageIcon);
        setText(text);

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
