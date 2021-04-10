import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client {
    private Socket socket = null;
    private DataBase dataBase = null ;
    private ClientThread clientThread = null;
    private Event event = new Event();
    private String mainDir = null;
    private String dataBasePath = null;
    private String host = "localhost";
    private int port = 2700;

    public Client(String mainDir,String host, int port){
        try {
            if(host != null) this.host = host;
            this.port = port;
            this.mainDir = mainDir;
            this.dataBasePath = mainDir + File.separator + "db";
            if (!Files.isDirectory(Paths.get(mainDir)))
                Files.createDirectory(Paths.get(mainDir));
            loadDataBase(dataBasePath);
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
    public void closeClient(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadDataBase(String dataBasePath) {
        dataBase = DataBase.loadData(dataBasePath);
        if(dataBase != null){
            System.out.println("dataBase was successfully loaded");
        }
        else {
            System.out.println("dataBase data was not found .. creating new one ...");
            dataBase = new DataBase(mainDir);
            System.out.println("Done");
        }
    }
    public void saveDataBase(){
        dataBase.save();
    }

    public void startTransmission(){
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientThread = new ClientThread(socket,dataBase,event);
    }
    public void signUp(String email,String name, String pass, String bio, String avatarSrc){
        User user = new User(email,name,pass,bio,avatarSrc);
        user.setCmd("signUp");
        if (clientThread != null)
            send(user);
        else
            System.out.println("In Client.signUp(): 'clientThread' is null");

    }
    public boolean signIn(String email, String password){
        Message message = new Message();
        message.setEmail(email);
        message.setPassword(password);
        message.setCmd("signIn");
        send(message);
        event.block();
        if(getActualUser() == null){
            return false;
        }
        else return true;
    }
    public void createGroup(String name, String bio, String avatarSrc, String groupType){
        Group group = new Group(name,bio,avatarSrc);
        group.setBio(bio);
        group.setGroupType(groupType);
        group.setCmd("createGroup");
        send(group);
    }
    public void updateGroup(int groupId,String name, String bio, String avatarSrc){
        Message message = new Message();
        message.setGroupData(groupId,name,bio,avatarSrc);
        message.setCmd("updateGroup");
        message.setUserId(getActualUser().getId());
        send(message);
    }
    public User getActualUser(){
        return clientThread.getActualUser();
    }

    public void send(Object obj){
        if(obj != null) clientThread.send(obj);
        else System.out.println("Client.send(): 'obj' is null");
    }
    public void sendMessage(String text, int destId){
        Message message = new Message();
        message.setMessage(text);
        message.setUserId(getActualUser().getId());
        message.setDestId(destId);
        message.setCmd("messageRequest");

        send(message);
    }
    public void sendFile(String filePath, int destId){
        FileContainer fileContainer = new FileContainer(filePath);
        fileContainer.setUserId(getActualUser().getId());
        fileContainer.setDestId(destId);
        fileContainer.setCmd("sendFile");
        send(fileContainer);
    }
    public void getFile(FileContainer file){
        System.out.println(file.getOriginalFileName() + " is being downloaded");
        int fileName = Integer.parseInt(file.getFilename());
        int destId = file.getDestId();
        Message message = new Message();
        message.setUserId(getActualUser().getId());
        message.setDestId(destId);
        message.setInfo(fileName);
        message.setCmd("getFile");
        send(message);
    }
    public boolean checkIfFileIsDownloaded(FileContainer file){
        Chat chat = dataBase.getChat(file.getDestId());
        if(chat == null) return false;

        String filePath = chat.getFileDir() + File.separator + file.getOriginalFileName();
        if(Files.exists(Paths.get(filePath))){
            return true;
        }
        else return false;
    }
    public boolean transmissionIsActive(){
        if(clientThread == null) return false;
        if(socket == null) return false;
        if(socket.isClosed()) return false;
        return true;
    }
    public void logout(){
        try {
            clientThread.stop();
            clientThread = null;
            socket.close();
        } catch (NullPointerException | IOException e){
            e.printStackTrace();
        }

    }

    public void getAllChats(){
        Message message = new Message();
        message.setCmd("getChats");
        message.setUserId(getActualUser().getId());
        send(message);
    }
    public void joinChat(Chat chat){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("join");
        message.setUserId(getActualUser().getId());
        message.setDestUserId(getActualUser().getId());

        send(message);
    }
    public void downloadAllData(){
        Message message = new Message();
        message.setCmd("downloadUserData");
        message.setUserId(getActualUser().getId());
        message.setPassword(getActualUser().getPassword());
        send(message);
    }
    public void leaveChat(Chat chat){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("leave");
        message.setDestUserId(getActualUser().getId());
        message.setExecId(getActualUser().getId());

        send(message);
    }
    public void removeUser(Chat chat, int userId){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("leave");
        message.setDestUserId(userId);
        message.setExecId(getActualUser().getId());

        send(message);
    }
    public void addAdmin(Chat chat, int userId){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("addAdmin");
        message.setDestUserId(userId);
        message.setExecId(getActualUser().getId());

        send(message);
    }
    public void removeAdmin(Chat chat, int userId){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("removeAdmin");
        message.setDestUserId(userId);
        message.setExecId(getActualUser().getId());

        send(message);
    }
    public void banUser(Chat chat, int userId){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("banUser");
        message.setDestUserId(userId);
        message.setExecId(getActualUser().getId());

        send(message);
    }
    public void unbanUser(Chat chat, int userId){
        if(chat == null) {
            System.out.println("'chat' is null");
            return;
        }
        Message message = new Message();
        message.setDestId(chat.getId());
        message.setCmd("groupManagement");

        message.setSubCmd("unbanUser");
        message.setDestUserId(userId);
        message.setExecId(getActualUser().getId());

        send(message);
    }

    public void updateUser(String name, String pass, String bio, String avatarSrc){
        Message message = new Message();
        message.setUserData(name,pass,bio,avatarSrc);
        message.setCmd("updateUser");
        message.setUserId(getActualUser().getId());
        send(message);
    }
    public DataBase getDataBase() {
        return dataBase;
    }

    public Event getEvent() {
        return event;
    }

    public String getMainDir() {
        return mainDir;
    }

    public static void main(String[] args) {
        Client client = new Client(System.getProperty("user.home") + File.separator + "ClientData","localhost",2700);
        client.startTransmission();

//        FileContainer fileContainer = new FileContainer("C:\\Users\\Dell\\Pictures\\PE\\ak.png");
//        fileContainer.setDestId(1);
//        client.send(fileContainer);

        //client.signUp("q","Michal","1234","afk",null);
        //client.signIn("q","1234");
       // System.out.println(client.getActualUser().getSubscribedChats());
       // client.sendMessage(new Message("text",2,2));
     //   client.createGroup("Group Name","bio","src\\Icons\\wolf.jpg","group");
      //  Chat chat = client.dataBase.getChat(3);
       // client.leaveChat(chat);

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
       // client.sendFile("src\\Icons\\wolf.jpg",3);
        //client.saveDataBase();
        //System.out.println(client.getActualUser().getSubscribedChats());
    }
}
