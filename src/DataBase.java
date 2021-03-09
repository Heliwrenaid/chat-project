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
        if (arr.size() == 0) return 1;
        if (!arr.contains(1)) return 1;
        for(int i =0;i<arr.size()-1;i++){
            if(arr.get(i) != arr.get(i+1)-1){
                return arr.get(i)+1;
            }
        }
        return arr.get(arr.size()-1)+1;
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

    void createUser(String name, String password, String bio, String avatarSrc){
        int newId = nextId(users);
        new User(name,password,newId,mainDir,bio,avatarSrc);
        users.add(newId);
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
        System.out.print("Saving database ... ");
        try {
            FileOutputStream file = new FileOutputStream(mainDir+ File.separator+"db");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(this);
            output.close();
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("In Database.save() error occurred: "+ e.getMessage());
        }
    }

    User getUser(int id) {
        FileInputStream fileStream = null;
        User user = null;
        try {
            fileStream = new FileInputStream(mainDir+ File.separator+"users"+File.separator+id + File.separator + "info");
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
            user = (User) objStream.readObject();
            objStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    Group getGroup(int id)  {
        FileInputStream fileStream = null;
        Group group = null;
        try {
            fileStream = new FileInputStream(mainDir + File.separator + "groups" + File.separator + id + File.separator + "info");
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
            group = (Group) objStream.readObject();
            objStream.close();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return group;
    }


    Channel getChannel(int id) throws IOException, ClassNotFoundException {
        FileInputStream fileStream = null;
        Channel channel= null;
        try {
            fileStream = new FileInputStream(mainDir + File.separator + "channels" + File.separator + id + File.separator + "info");
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
            channel = (Channel) objStream.readObject();
            objStream.close();
        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
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

    public static void main(String[] args){
        DataBase db = new DataBase("C:\\Dell\\chatDb");
        //System.out.println(db.getUsers());
        //System.out.println(db.nextId(db.users));
        db.createUser("jan","1234",null,null);
        User jan = db.getUser(1);
        System.out.println(jan.getName() + " ; " + jan.getId() + " ; " + jan.getBio());
        db.deleteUser(1);


        //db.deleteUser(1); //TODO
    }
}

