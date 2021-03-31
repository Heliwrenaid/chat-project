import javax.swing.*;
import java.awt.*;

public class ChatRenderer extends JLabel implements ListCellRenderer<Chat>{

    public ChatRenderer(){
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<? extends Chat> list, Chat chat, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        String code = chat.getAvatarSrc();

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(code).getImage().getScaledInstance(20,17,Image.SCALE_DEFAULT));
        // src https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        setIcon(imageIcon);
        setText(chat.getName());

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
