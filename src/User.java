import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class User extends Chat implements Serializable {

    private String password;
    private String avatarSrc;
    private String email;
    private ArrayList <Integer> subscribedChats = new ArrayList<>();


    public User(String email,String name, String password,int id,String mainDir,String bio,String avatarSrc) {
        super(mainDir + File.separator + "users" + File.separator + id,id);
        this.email = email;
        this.name = name;
        this.password = password;

        if (bio != null) {
            this.bio = bio;
        } else {
            this.bio = "Basic personal info";
        }
        if (avatarSrc != null) {
            this.avatarSrc = avatarSrc;
        } else {
            this.avatarSrc = "resources\\avatar.png";
        }
        save();
    }

    public User(String email,String name, String password, String bio, String avatarSrc) {
        super();
        this.name = name;
        this.password = password;
        this.bio = bio;
        this.avatarSrc = avatarSrc;
        this.email = email;
    }

    public void subscribeChat(int id){
        subscribedChats.add(id);
    }
    public void deleteSub(int id){
        subscribedChats.remove(id);
    }

    //-------------------Getters and setters--------------------
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getMessageDir() {
        return messageDir;
    }

    public void setMessageDir(String messageDir) {
        this.messageDir = messageDir;
    }

    public static void main(String[] args){
       // User user = new User("jan","pass",1,)
    }
    int nextMessageId(){
        return Functions.nextId(messages);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public ArrayList<Integer> getSubscribedChats() {
        return subscribedChats;
    }
    public void setSubscribedChats(ArrayList<Integer> subscribedChats) {
        this.subscribedChats = subscribedChats;
    }
}
