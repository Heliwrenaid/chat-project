import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Group extends Chat implements Serializable {

    private ArrayList<Integer> subscribers = new ArrayList<Integer>();
    private ArrayList<Integer> admins = new ArrayList<Integer>();


    public Group(String mainDir,int groupId) {
        super(mainDir,groupId);
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

    void save(){
        Functions.save(this,dir+File.separator+"info");
    }


}
