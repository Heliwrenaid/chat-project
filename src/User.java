import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User implements Serializable {

    private String name;
    private String password;
    private String bio;
    private int id;
    private String avatarSrc;
    private String dir;

    public User(String name, String password,int id,String mainDir,String bio,String avatarSrc) {
        this.name = name;
        this.password = password;
        this.id = id;
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
        this.dir = mainDir + File.separator + "users" + File.separator + id;
        try {
            Files.createDirectories(Paths.get(dir));
        }catch (IOException e){
            System.out.println("In User.Constructor() error occurred: "+ e.getMessage());
        }
        save();
    }

    void save(){
        System.out.print("Saving user info ... ");
        try {
            FileOutputStream file = new FileOutputStream(dir+ File.separator + "info");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(this);
            output.close();
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("In User.save() error occurred: "+ e.getMessage());
        }
    }

    //-------------------Getters and setters--------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public static void main(String[] args){
       // User user = new User("jan","pass",1,)
    }
}
