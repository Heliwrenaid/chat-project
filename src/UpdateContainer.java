import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class UpdateContainer implements Serializable {
    private int amount;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<FileContainer> files = new ArrayList<>();

    public void add(Chat chat){
        if(chat != null) {
            if(!chats.contains(chat)) chats.add(chat);
        }
    }
    public void add(Message message){
        if(message != null) {
            if(!messages.contains(message)) messages.add(message);
        }
    }
    public void add(FileContainer file){
        if(file != null) {
            if(!files.contains(file)) files.add(file);
        }
    }
    public void add(Object obj){
        if (obj == null) return;
        if (obj instanceof Chat) add((Chat) obj);
        if (obj instanceof Message) add((Message) obj);
        if (obj instanceof FileContainer) add((FileContainer) obj);
    }
    public void calculateAmount(){
        amount = chats.size() + messages.size() + files.size();
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
    public boolean hasFiles(){
        if(files.size() > 0) return true;
        return false;
    }

    // getters and setters -------------------------------------

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<FileContainer> getFiles() {
        return files;
    }
}
