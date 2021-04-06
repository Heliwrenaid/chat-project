import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class DataBase implements Serializable{
    private String mainDir;
    private String userDir;
    // Strings: 'users', 'groups', 'channels'
    private HashMap<Integer,String> idSet = new HashMap<>();
    private HashMap <String,Integer> emails = new HashMap<>();
    public DataBase(String mainDir) {
        this.mainDir = mainDir;
        this.userDir = mainDir + File.separator+ "users";
        createDirectories();
        save();
    }

    void createDirectories(){
        //function for creating directories
        try {
            Files.createDirectories(Paths.get(mainDir + File.separator + "users"));
            Files.createDirectories(Paths.get(mainDir + File.separator + "groups"));
            Files.createDirectories(Paths.get(mainDir + File.separator + "channels"));
        }catch (IOException e){
            System.out.println("In Database.createDirectories() error occurred: "+ e.getMessage());
        }
    }

    int freeId(){
        return Functions.freeId(idSet);
    }

    public User createUser(String email,String name, String password, String bio, String avatarSrc){
        int newId = freeId();
        if(emails.containsKey(email)){
            System.out.println("In DataBase.createUser(): " +email + " already exists ... Aborting.");
            return null;
        }
        emails.put(email,newId);
        idSet.put(newId,"users");
        save();
        return new User(email,name,password,newId,mainDir,bio,avatarSrc,null);
    }
    public boolean createUser(User user){
        // in client side
        if (user == null) return false;
        if(emails.containsKey(user.getEmail())){
            System.out.println("In DataBase.createUser(): " +user.getEmail() + " already exists ... Aborting.");
            return false;
        }
        else {

        }
        new User(user.getEmail(), user.getName(), user.getPassword(), user.getId(), mainDir, user.getBio(), user.getAvatarSrc(),user.getSubscribedChats());
        save();
        return true;
    }
    public boolean updateUser(User user){
        if (user == null) return false;
        if(!emails.containsKey(user.getEmail())){
            System.out.println("In DataBase.updateUser(): " +user.getEmail() + " not exists ... Creating User.");
            idSet.put(user.getId(),"users");
            emails.put(user.getEmail(), user.getId());
        }
        new User(user.getEmail(), user.getName(), user.getPassword(), user.getId(), mainDir, user.getBio(), user.getAvatarSrc(),user.getSubscribedChats());
        save();
        System.out.println("In DataBase.updateUser(): " +user.getEmail() + " was updated");

        return true;
    }
    public Group createGroup(Group group,int ownerId){
        int freeId = freeId();
        idSet.put(freeId,"groups");
        group.setId(freeId);
        group.setRootDir(mainDir);
        group.setDir(mainDir + File.separator + "groups" + File.separator + group.getId());
        group.createOwner(ownerId);
        group.generateDirs();
        group.createDirectories();
        saveGroup(group);
        save();
        return group;
    }
    public void createGroup(Group group){
        //in client side
        idSet.put(group.getId(),"groups");
        group.setRootDir(mainDir);
        group.setDir(mainDir + File.separator + "groups" + File.separator + group.getId());
        group.generateDirs();
        group.createDirectories();
        group.saveAvatar();
        saveGroup(group);
        User user = getUser(group.getOwnerId());
        if (user != null){ //TODO: potrzebne?
           // user.subscribeChat(group.getId());
            user.save();
        }
        save();
    }
    void delete(int id){
        //TODO: jak User to czy usunac ze wszystkich grup
        if (!idSet.containsKey(id)){
            System.out.println("In Database.delete(): " + id + " isn't in idSet");
            return;
        }
        String directory = mainDir+File.separator+idSet.get(id)+File.separator+id;
        idSet.remove(id);
        File dir = new File(directory);

        for (File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
        try{
            Files.deleteIfExists(Paths.get(directory));
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    void save(){
      Functions.save(this,mainDir+File.separator+"db");
    }
    void saveGroup(Group group){
        Functions.save(group,mainDir + File.separator + "groups" + File.separator + group.getId() + File.separator + "info");
    }

    public User getUser(int id) {
        return (User) Functions.getObject(mainDir+ File.separator+"users"+File.separator+id + File.separator + "info");
    }
    User getUser(String email){
        if(emails != null)
            if(emails.containsKey(email)){
                return getUser(emails.get(email));
            }
        return null;
    }

    Group getGroup(int id)  {
        return (Group) Functions.getObject(mainDir+ File.separator+"groups"+File.separator+id + File.separator + "info");
    }

    Channel getChannel(int id)  {
        return (Channel) Functions.getObject(mainDir+ File.separator+"channels"+File.separator+id + File.separator + "info");
    }
    public Chat getChat(int id){
        if(!idSet.containsKey(id)) return null;
        if(idSet.get(id).equals("users")) return getUser(id);
        if(idSet.get(id).equals("groups")) return getGroup(id);
        if(idSet.get(id).equals("channels")) return getChannel(id);
        return null;
    }
    static DataBase loadData(String filename) {
        if (Files.exists(Paths.get(filename))) {
            try {
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                DataBase dataBase = ((DataBase) objectInputStream.readObject());
                objectInputStream.close();
                return dataBase;
            } catch (Exception e) {
                System.out.println("Error in DataBase.loadDataBase(): " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public boolean verify(Object obj){
        switch (obj.getClass().getName()){
            case "FileContainer":{
                FileContainer fileContainer = (FileContainer) obj;
                if (!idSet.containsKey(fileContainer.getUserId()))
                    return false;
                return true;
            }
            case "Message":{
                Message message = (Message) obj;
                if (!idSet.containsKey(message.getUserId()))
                    return false;
                return true;
            }
        }
        return false;
    }

//-------------------Getters and setters-------------------
    public String getMainDir() {
        return mainDir;
    }

    public void setMainDir(String mainDir) {
        this.mainDir = mainDir;
    }

    public HashMap<Integer, String> getIdSet() {
        return idSet;
    }

    public HashMap<String, Integer> getEmails() {
        return emails;
    }

    public String getUserDir() {
        return userDir;
    }

    public void setUserDir(String userDir) {
        this.userDir = userDir;
    }

    public static void main(String[] args){
        DataBase db = loadData(System.getProperty("user.home") + File.separator + "DB\\db\\db");
        System.out.println(db.idSet.keySet());
        //db.createUser("jan","1234",null,null);
        db.delete(4);
       // User jan = db.getUser(1);
       System.out.println(db.idSet.keySet());
      //  System.out.println(jan.getName() + " ; " + jan.getId() + " ; " + jan.getBio());
        db.save();
    }


}

