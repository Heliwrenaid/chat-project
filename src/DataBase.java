import java.io.*;
import java.nio.file.*;
import java.util.HashMap;

public class DataBase implements Serializable{
    private String mainDir;
    private String userDir;
    private HashMap<Integer,String> idSet = new HashMap<>(); // Strings: 'users', 'groups'
    private HashMap <String,Integer> emails = new HashMap<>();
    public DataBase(String mainDir) {
        this.mainDir = mainDir;
        this.userDir = mainDir + File.separator+ "users";
        createDirectories();
        save();
    }

    public void createDirectories(){
        try {
            Files.createDirectories(Paths.get(mainDir + File.separator + "users"));
            Files.createDirectories(Paths.get(mainDir + File.separator + "groups"));
            Files.createDirectories(Paths.get(mainDir + File.separator + "channels"));
        }catch (IOException e){
            System.out.println("In Database.createDirectories() error occurred: "+ e.getMessage());
        }
    }

    public int freeId(){
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
        return new User(email,name,password,newId,mainDir,bio,avatarSrc);
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
        new User(user,mainDir);
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
        new User(user,mainDir);
        save();
        System.out.println("In DataBase.updateUser(): " +user.getEmail() + " was updated");

        return true;
    }
    public boolean checkIfUserExists(String email){
        if(emails == null) return false;
        if(emails.containsKey(email)) return true;
         else return false;
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
        if (user != null){
            user.save();
        }
        save();
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
    public User getUser(String email){
        if(emails != null)
            if(emails.containsKey(email)){
                return getUser(emails.get(email));
            }
        return null;
    }

    public Group getGroup(int id)  {
        return (Group) Functions.getObject(mainDir+ File.separator+"groups"+File.separator+id + File.separator + "info");
    }

    public Chat getChat(int id){
        if(!idSet.containsKey(id)) return null;
        if(idSet.get(id).equals("users")) return getUser(id);
        if(idSet.get(id).equals("groups")) return getGroup(id);
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

}

