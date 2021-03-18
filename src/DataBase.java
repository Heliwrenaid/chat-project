import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;


public class DataBase implements Serializable{
    private String mainDir;
    private ArrayList<Integer> channels = new ArrayList<Integer>();
    private ArrayList<Integer> groups = new ArrayList<Integer>();
    private ArrayList<Integer> users = new ArrayList<Integer>() ;

    public DataBase(String mainDir) {
        this.mainDir = mainDir;
        createDirectories();
        save();
        /*
        users.add(1);
        users.add(2);
        //users.add(3);
        users.add(4);
        //users.add(5);
        users.add(6);
        */
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

    int nextId(ArrayList<Integer> arr){
        return Functions.nextId(arr);
    }

    int nextUserId(){
        return nextId(users);
    }
    int nextGroupId(){
        return nextId(groups);
    }
    int nextChannelId(){
        return nextId(channels);
    }

    User createUser(String name, String password, String bio, String avatarSrc){
        int newId = nextId(users);
        users.add(newId);
        return new User(name,password,newId,mainDir,bio,avatarSrc);
    }

    void deleteUser(int id){
        String director =  ((mainDir+File.separator+"users"+File.separator+id));
        File dir = new File(director);

        for (File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
        try{
            Files.deleteIfExists(Paths.get(director));
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    void save(){
      Functions.save(this,mainDir+File.separator+"dB");
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
    public ArrayList<Integer> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Integer> channels) {
        this.channels = channels;
    }

    public ArrayList<Integer> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Integer> groups) {
        this.groups = groups;
    }
    public ArrayList<Integer> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Integer> users) {
        this.users = users;
    }

    /*
    public static void main(String[] args){
        DataBase db = new DataBase("C:\\Dell\\chatDb");
        //System.out.println(db.getUsers());
        //System.out.println(db.nextId(db.users));
        db.createUser("jan","1234",null,null);
        User jan = db.getUser(1);
        System.out.println(jan.getName() + " ; " + jan.getId() + " ; " + jan.getBio());
        db.deleteUser(1);
        db.save();

    }
    */
}

