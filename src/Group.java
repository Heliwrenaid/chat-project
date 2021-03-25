import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Group extends Chat implements Serializable {

    private ArrayList<Integer> subscribers = new ArrayList<Integer>();
    private ArrayList<Integer> admins = new ArrayList<Integer>();
    private int ownerId;

    public Group(Group group,String mainDir) {
        super(mainDir,group.id);

    }
    public Group(){};
    public Group(String name){
        // create group from Client.createGroup(...)
        this.name = name;


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
    public void createOwner(int ownerId){
        this.ownerId = ownerId;
        subscribers.add(ownerId);
        admins.add(ownerId);
    }
    public int getOwnerId() {
        return ownerId;
    }

    public ArrayList<Integer> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(ArrayList<Integer> subscribers) {
        this.subscribers = subscribers;
    }

    public ArrayList<Integer> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Integer> admins) {
        this.admins = admins;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }


    void save(){
        Functions.save(this,dir+File.separator+"info");
    }


}
