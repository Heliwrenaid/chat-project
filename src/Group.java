import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Group implements Serializable {
    private int groupId;
    private String dir;
    private PermissionManager permManager = new PermissionManager();
    private ArrayList<Integer> subscribers = new ArrayList<Integer>();
    private ArrayList<Integer> admins = new ArrayList<Integer>();
    private ArrayList<Integer> messages = new ArrayList<Integer>();

    public Group(int groupId, String mainDir) {
        this.groupId = groupId;
        this.dir = mainDir + File.separator + "groups" + File.separator + groupId;
    }

    boolean addSubscriber(int userId){
        /*
        add 'userId' to 'subscribers'
        false if contains else true
         */
        if(subscribers.contains(userId))
            return false;
        else
            subscribers.add(userId);
        return true;
    }

    boolean removeSubscriber(int userId){
        /*
        remove 'userId' from 'subscribers'
        false if not contains else true
         */
        if(!subscribers.contains(userId)){
            return false;
        }
        else
        {
            subscribers.remove(userId);
        }
        return true;
    }
    boolean addAdmin(int userId){
        //add to 'admins'
        if(admins.contains(userId)){
            return false;
        }
        else{
            admins.add(userId);
        }
        return true;
    }
    boolean removeAdmin(int userId){
        //remove from 'admins'
        if(!admins.contains(userId)){
            return false;
        }
        else
        {
            admins.remove(userId);
        }
        return true;
    }
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
                FileOutputStream file = new FileOutputStream(dir+File.separator+"messages"+File.separator+newId);
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
    void createDirectories(){
        /*
            groups\ (już utworzone przez DataBase)
                    1\
                        messages\
                                    1 (to jest plik wiadomosci)
                        groupData (plik do zapisania całego obiektu tworzony przez save())
                    ...
         */
        try {
            Files.createDirectories(Paths.get(dir+File.separator+"messages"));
        }catch (IOException e){
            System.out.println("In Database.createDirectories() error occurred: "+ e.getMessage());
        }
    }
    void save(){
        Functions.save(this,dir+File.separator+"groupData");
    }

    Message getMessage(int id){
        /*FileInputStream fileStream = null;
        Message message = null;
        try {
            fileStream = new FileInputStream(dir+ File.separator+"messages"+File.separator+id);
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
             message = (Message) objStream.readObject();
            objStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;*/
        return (Message) Functions.getObject(dir+ File.separator+"messages"+File.separator+id);
    }

    int nextMessageId(){
        // jak w DataBase
        return Functions.nextId(messages);
    }
    //

    public static void main(String[] args) {
        Group group = new Group(1,"C:\\");
        group.createDirectories();
        Message m = new Message(10000);
        Message mi = new Message(10);
        group.addMessage(m);
        group.addMessage(mi);
        System.out.println(group.getMessage(1).getData());
        group.save();
    }
}
