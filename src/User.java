import java.io.*;
import java.util.ArrayList;

public class User extends Chat implements Serializable {
    private String password;
    private String email;
    private String savePath;
    private ArrayList <Integer> subscribedChats = new ArrayList<>();

    //create User
    public User(String email,String name, String password, String bio, String avatarSrc) {
        super();
        this.name = name;
        this.password = password;
        this.bio = bio;
        this.avatarSrc = avatarSrc;
        this.email = email;
    }

    //create User from DataBase
    public User(String email,String name, String password,int id,String mainDir,String bio,String avatarSrc) {
        super(mainDir + File.separator + "users" + File.separator + id,id, avatarSrc);
        this.email = email;
        this.name = name;
        this.password = password;
        this.savePath = dir + File.separator + "info";
        if (bio != null) {
            this.bio = bio;
        } else {
            this.bio = "Basic personal info";
        }
        save();
    }

    public User (){}

    // updateUser in DataBase
    public User(User user,String mainDir){
        super(mainDir + File.separator + "users" + File.separator + user.getId(),user.getId(), user.getAvatarSrc());
        updateChat(user);
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.savePath = dir + File.separator + "info";
        if(user.getSubscribedChats() != null)
            this.subscribedChats = user.getSubscribedChats();
        save();
    }

    @Override
    void save(){
        if(avatar != null) saveAvatar();
        Functions.save(this,dir+File.separator + "info");
    }

    @Override
    public String toString() {
        return "User: "+this.getName();
    }

    public static User loadUser(String path){
        return (User) Functions.getObject(path);
    }

    public void subscribeChat(int id){
        if(subscribedChats.contains(id)) return;
        subscribedChats.add(id);
        save();
    }
    public void unsubscribeChat(int id){
        if(!subscribedChats.contains(id)) return;
        subscribedChats.remove((Object)id);
        save();
    }

    // user management --------------------------------------------------

    @Override
    public boolean takeAction(Message message){
        switch (message.getSubCmd()){
            case "join": return addUser(message.getDestUserId());
            case "leave": return removeUser(message.getDestUserId(),message.getExecId());
            case "banUser": return banUser(message.getDestUserId(),message.getExecId());
            case "unbanUser": return unbanUser(message.getDestUserId(),message.getExecId());
            default: return false;
        }
    }
    public boolean addUser(int userId){
        if(users.containsKey(userId)){
            // banned user can't join
            return false;
        }
        else {
            if(userId == id) return false;
            users.put(userId,"user");
            subscribeChat(userId);
            save();
            return true;
        }
    }

    public boolean removeUser(int userId, int execId){
        if(execId == userId){
            if(users.containsKey(userId))
                users.remove(userId);
            unsubscribeChat(userId);
            return true;
        }
        users.remove(userId);
        unsubscribeChat(userId);
        save();
        return true;
    }
    public boolean banUser(int userId, int execId){
        if(execId == userId){
            if (users.containsKey(userId)){
                users.remove(userId);
                users.put(userId,"banned you");
            }
            unsubscribeChat(userId);
            return true;
        }
        users.remove(userId);
        users.put(userId,"banned");
        unsubscribeChat(userId);
        save();
        return true;
    }

    public boolean unbanUser(int userId, int execId){
        if(execId != userId ){
            if(execId == id){
                if(users.containsKey(userId)) {
                    if(!users.get(userId).equals("banned you")) {
                        users.remove(userId);
                        save();
                        return true;
                    } else return false;
                } else return false;
            } else return false;
        } else{
            if(users.containsKey(userId)){
               if ( users.get(userId).equals("banned you")){
                    users.remove(userId);
                    save();
               } else return false;
            }
        }
        return true;
    }


    //-------------------Getters and setters--------------------
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    int nextMessageId(){
        return Functions.nextId(messages);
    }

    public String getEmail() {
        return email;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Integer> getSubscribedChats() {
        return subscribedChats;
    }
}
