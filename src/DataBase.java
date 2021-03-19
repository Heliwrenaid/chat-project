import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class DataBase implements Serializable{
    private String mainDir;
    // Strings: 'users', 'groups', 'channels'
    private HashMap<Integer,String> idSet = new HashMap<>();
    public DataBase(String mainDir) {
        this.mainDir = mainDir;
        createDirectories();
        save();
    }

    void createDirectories(){
        //function that creates directories
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

    User createUser(String name, String password, String bio, String avatarSrc){
        int newId = freeId();
        idSet.put(newId,"users");
        return new User(name,password,newId,mainDir,bio,avatarSrc);
    }

    void delete(int id){
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

    User getUser(int id) {
        return (User) Functions.getObject(mainDir+ File.separator+"users"+File.separator+id + File.separator + "info");
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


//-------------------Getters and setters-------------------
    public String getMainDir() {
        return mainDir;
    }

    public void setMainDir(String mainDir) {
        this.mainDir = mainDir;
    }

    /*
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
     */

}

