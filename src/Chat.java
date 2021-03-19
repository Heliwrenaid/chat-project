import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Chat {
    protected String dir;
    protected String fileDir;
    protected String messageDir;
    protected ArrayList<Integer> messages = new ArrayList<Integer>();
    protected PermissionManager permManager = new PermissionManager();

    boolean addMessage(Message message){
        if (permManager.checkPerm(message)){
            /*
            int newId = nextMessageId();
            dodaj newId do 'messages'
            zapisz 'message' do pliku o nazwie 'newId' w folderze messages
             */
            int newId = nextMessageId();
            messages.add(newId);
            try {
                FileOutputStream file = new FileOutputStream(dir+ File.separator+"messages"+File.separator+newId);
                ObjectOutputStream output = new ObjectOutputStream(file);
                output.writeObject(message);
                output.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
    int nextMessageId(){
        return Functions.nextId(messages);
    }
    public String getDir() {
        return dir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getMessageDir() {
        return messageDir;
    }
    Message getMessage(int id){
        return (Message) Functions.getObject(dir+ File.separator+"messages"+File.separator+id);
    }
}
