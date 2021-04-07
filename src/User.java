import java.io.*;
import java.util.ArrayList;

public class User extends Chat implements Serializable {

    private String password;
    private String email;
    private String savePath;
    private ArrayList <Integer> subscribedChats = new ArrayList<>();


    public User(String email,String name, String password,int id,String mainDir,String bio,String avatarSrc,ArrayList<Integer> subsChats,ArrayList<Integer> messages) {
        super(mainDir + File.separator + "users" + File.separator + id,id, avatarSrc);
        this.email = email;
        this.name = name;

        this.password = password;
        this.savePath = dir + File.separator + "info";
        if(subsChats != null)
            this.subscribedChats = subsChats;

        if(messages != null)
            this.messages = messages;

        if (bio != null) {
            this.bio = bio;
        } else {
            this.bio = "Basic personal info";
        }
        save();
    }

	@Override
    public String toString() {
        return "User: "+this.getName();
    }

    public User (){
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
        if(subscribedChats.contains(id)) return;
        subscribedChats.add(id);
        save();
    }
    public void unsubscribeChat(int id){
        if(!subscribedChats.contains(id)) return;
        subscribedChats.remove((Object)id);
        save();
    }

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
            users.put(userId,"user");
            subscribeChat(userId);
            save();
            return true;
        }
    }

    public boolean removeUser(int userId, int execId){
        if(!users.containsKey(userId)){
            return false;
        }
        else {
            if(userId == execId){
                users.remove(userId);
                User user = getUser(userId);
                if(user == null){
                    return false;
                }
                user.unsubscribeChat(id);
                save();
                return true;
            }
            return false;
        }
    }
    public boolean banUser(int userId, int execId){
        if(!users.containsKey(execId)) return false;
        if(!users.containsKey(userId)) return false;
        if(execId == id){
            users.remove(userId);
            users.put(userId,"banned");
            User user = getUser(userId);
            if(user == null){
                return false;
            }
            user.unsubscribeChat(id);
            save();
            return true;
        }
        return false;
    }

    public boolean unbanUser(int userId, int execId){
        if(!users.containsKey(execId)) return false;
        if(!users.containsKey(userId)) return false;
        if(execId == id ){
            users.remove(userId);
            save();
            return true;
        }
        return false;
    }

    @Override
    void save(){
        if(avatar != null) saveAvatar();
        Functions.save(this,dir+File.separator + "info");
    }

    public static User loadUser(String path){
        return (User) Functions.getObject(path);
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

    public static void main(String[] args){
       // User user = new User("jan","pass",1,)
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
    public void setSubscribedChats(ArrayList<Integer> subscribedChats) {
        this.subscribedChats = subscribedChats;
    }
    public void deleteFromSubscribedChats(Chat chat) {
        this.subscribedChats.remove(chat.getId());
        System.out.println("Group removed from subscribed chats!");
        save();
    }
}
