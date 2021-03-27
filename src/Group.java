import java.io.*;
import java.util.HashMap;

public class Group extends Chat implements Serializable {
    // 'user','admin','banned'
    HashMap <Integer,String> users = new HashMap<>();
    private int ownerId;
    private String groupType;

    public Group(Group group,String mainDir, String avatarSrc) {
        super(mainDir,group.id,avatarSrc);

    }
    public Group(){}
    public Group(String name, String bio,String avatarSrc){
        // create group from Client.createGroup(...)
        super(name,bio,avatarSrc);

    }
//
    public boolean addUser(int userId){
        if(users.containsKey(userId)){
            // banned user can't join
            return false;
        }
        else {
            users.put(userId,"user");
            User user = getUser(userId);
            if(user == null){
                return false;
            }
            user.subscribeChat(id);
            save();
            return true;
        }
    }

    public boolean removeUser(int userId, int execId){
        if(!users.containsKey(userId)){
            return false;
        }
        else {
            if(users.get(execId).equals("admin") || userId == execId){
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
    public boolean addAdmin(int userId, int execId){
        if(execId != ownerId) return false;
        if(!users.containsKey(execId)) return false;
        if(!users.containsKey(userId)){
            return false;
        }
        else {
            String status = users.get(userId);
            if (status.equals("admin") || status.equals("banned")) {
                return false;
            } else {
                users.remove(userId);
                users.put(userId, "admin");
                save();
                return true;
            }
        }
    }
    public boolean removeAdmin(int userId, int execId){
        if (execId != ownerId || !users.get(execId).equals("admin")) return false;
        if(!users.containsKey(execId)) return false;
        if(!users.containsKey(userId)){
            return false;
        }
        else {
           users.remove(userId);
           users.put(userId,"user");
            save();
           return true;
        }
    }
    public boolean banUser(int userId, int execId){
        if(!users.containsKey(execId)) return false;
        if(!users.containsKey(userId)) return false;
        if(execId == ownerId || users.get(execId).equals("admin")){
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
        if(execId == ownerId || users.get(execId).equals("admin")){
            users.remove(userId);
            save();
            return true;
        }
        return false;
    }
    @Override
    public boolean takeAction(Message message){
        switch (message.getSubCmd()){
            case "join": return addUser(message.getDestUserId());
            case "leave": return removeUser(message.getDestUserId(),message.getExecId());
            case "addAdmin": return addAdmin(message.getDestUserId(),message.getExecId());
            case "removeAdmin": return removeAdmin(message.getDestUserId(),message.getExecId());
            case "banUser": return banUser(message.getDestUserId(),message.getExecId());
            case "unbanUser": return unbanUser(message.getDestUserId(),message.getExecId());
            default: return false;
        }
    }
    public void p(){
        System.out.println("group");
    }
    public void createOwner(int ownerId){
        this.ownerId = ownerId;
        users.put(ownerId,"admin");
        User user = getUser(ownerId);
        if(user == null){
            return;
        }
        user.subscribeChat(id);
    }

    public void save(){
        Functions.save(this,dir+File.separator+"info");
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public HashMap<Integer, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, String> users) {
        this.users = users;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

}
