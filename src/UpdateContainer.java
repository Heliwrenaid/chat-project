import java.io.Serializable;
import java.util.ArrayList;

public class UpdateContainer implements Serializable {
    private int amount;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();

    public void add(Chat chat){
        chats.add(chat);
    }
    public void add(Message message){
        messages.add(message);
    }
    public void calculateAmount(){
        amount = chats.size() + messages.size();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public boolean hasChats(){
        if(chats.size() > 0) return true;
        return false;
    }

    public boolean hasMessages(){
        if(messages.size() > 0) return true;
        return false;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
