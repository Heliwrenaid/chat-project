import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Group extends Chat implements Serializable {
    private int groupId;
    private String dir;

    private ArrayList<Integer> subscribers = new ArrayList<Integer>();
    private ArrayList<Integer> admins = new ArrayList<Integer>();


    public Group(int groupId, String mainDir) {
        this.groupId = groupId;
        this.dir = mainDir + File.separator + "groups" + File.separator + groupId;
        fileDir = mainDir + File.separator + "files";
        messageDir = mainDir + File.separator + "messages";
    }
    public Group(){};

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
        Functions.save(this,dir+File.separator+"info");
    }


}
