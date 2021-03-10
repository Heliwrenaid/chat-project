import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private int groupId;
    private String dir;
    private PermissionManager permManager;
    private ArrayList<Integer> subscribers;
    private ArrayList<Integer> admins;
    private ArrayList<Integer> messages;

    public Group(int groupId, String mainDir) {
        this.groupId = groupId;
        this.dir = mainDir + File.separator + "groups" + File.separator + groupId;
    }

    boolean addSubscriber(int userId){
        /*
        add 'userId' to 'subscribers'
        false if contains else true
         */
    }
    boolean removeSubscriber(int userId){
        /*
        remove 'userId' from 'subscribers'
        false if not contains else true
         */
    }
    boolean addAdmin(int userId){
        //add to 'admins'
    }
    boolean removeAdmin(int userId){
        //remove from 'admins'
    }
    boolean addMessage(Message message){
        if (permManager.checkPerm(message)){

            /*
            int newId = nextMessageId();
            dodaj newId do 'messages'
            zapisz 'message' do pliku o nazwie 'newId' w folderze messages
             */
        }

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
    }
    void save(){

    }
    int nextMessageId(){
        // jak w DataBase
    }
}
