import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Chat extends JLabel implements Serializable {
    protected String name;
    protected String dir;
    protected String rootDir;
    protected String fileDir;
    protected String messageDir;
    protected String cmd;
    protected String bio;
    protected String avatarSrc;
    protected FileContainer avatar;
    protected int id = 0;
    protected ArrayList<Integer> messages = new ArrayList<Integer>();
    protected PermissionManager permManager = new PermissionManager();


    public Chat(String dir,int id, String avatarSrc) {
        this.id = id;
        this.dir = dir;
        if (avatarSrc == null) {
            this.avatarSrc = "src" + File.separator + "Icons" + File.separator + "wolf.jpg";
        }
        else this.avatarSrc = avatarSrc;
        generateDirs();
        createDirectories();
    }
    public Chat(String name, String bio,String avatarSrc){
        this.name = name;
        this.bio = bio;
        attachAvatar(avatarSrc);
        generateDirs(); //??
        //createDirectories(); TODO:??


    }
    public Chat(){
        generateDirs();
    }
    void save(){
        Functions.save(this,dir+File.separator + "info");
    }
    int addMessage(Message message){
        if (permManager.checkPerm(message)){
            /*
            int newId = nextMessageId();
            dodaj newId do 'messages'
            zapisz 'message' do pliku o nazwie 'newId' w folderze messages
             */
            int newId = nextMessageId();
            messages.add(newId);

            try {
                System.out.print("Saving Message with id: " + newId + " ... ");
                FileOutputStream file = new FileOutputStream(messageDir+File.separator+newId);
                ObjectOutputStream output = new ObjectOutputStream(file);
                output.writeObject(message);
                output.close();
                System.out.println("Saved !");
                save();
                return newId;
            } catch (Exception e) {
                System.out.println("Error in Chat.addMessage(): " + e.getMessage());
            }
        }
        return 0;
    }
    int addMessageClient(Message message){
        if (permManager.checkPerm(message)){
            /*
            int newId = nextMessageId();
            dodaj newId do 'messages'
            zapisz 'message' do pliku o nazwie 'newId' w folderze messages
             */
            int newId = message.getInfo();
            messages.add(newId);
            try {
                System.out.print("Saving Message with id: " + message.getInfo() + " ... ");
                FileOutputStream file = new FileOutputStream(messageDir+File.separator+newId);
                ObjectOutputStream output = new ObjectOutputStream(file);
                output.writeObject(message);
                output.close();
                System.out.println("Saved !");
                save();
                return newId;
            } catch (Exception e) {
                System.out.println("Error in Chat.addMessage(): " + e.getMessage());
            }
        }
        return 0;
    }


    void createDirectories(){
        try {
            Files.createDirectories(Paths.get(dir));
            Files.createDirectories(Paths.get(messageDir));
            Files.createDirectories(Paths.get(fileDir));
        }catch (IOException e){
            System.out.println("In Database.createDirectories() error occurred: "+ e.getMessage());
        }
    }
    public void generateDirs(){
        this.fileDir =dir + File.separator + "files";
        this.messageDir = dir + File.separator + "messages";
    }


    @Override
    public String toString() {
        return "Chat: " +name +'\n';
    }
    int nextMessageId(){
        return Functions.nextId(messages);
    }

    public User getUser(int id) {
        return (User) Functions.getObject(rootDir+ File.separator+"users"+File.separator+id + File.separator + "info");
    }
    public String getDir() {
        return dir;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getMessageDir() {
        return messageDir;
    }
    Message getMessage(int id){
        return (Message) Functions.getObject(dir+ File.separator+"messages"+File.separator+id);
    }
    public boolean verify(Object obj){
        //TODO
        return true;
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

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public void setMessageDir(String messageDir) {
        this.messageDir = messageDir;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public boolean takeAction(Message message){
       return true;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public ArrayList<Integer> getMessages() {
        return messages;
    }

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }

    public void saveAvatar(){
        this.avatarSrc = dir + File.separator + "avatar.png";
        avatar.setDestinationDirectory(dir);
        avatar.setFilename("avatar.png");
        avatar.saveFileData();
    }
    public void attachAvatar(String avatarPath){
        // when create group
        avatar = new FileContainer(avatarPath);
    }
/*
    public static void main(String [] args){
        Chat chat = new Chat();
        Message message = new Message("a","b","c","d");
        //chat.addMessage()
        chat.messages.add(chat.nextMessageId());
        System.out.println(chat.nextMessageId());

    }
*/
}
