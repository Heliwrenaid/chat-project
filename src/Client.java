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
    private String mainDir = null;
    private String dataBasePath = null;
    private String host = "localhost";
    private int port = 2300;

    public Client(String mainDir){
        try {
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
        clientThread = new ClientThread(socket,dataBase);
    }
    public void signUp(String email,String name, String pass, String bio, String avatarSrc){
        User user = new User(email,name,pass,bio,avatarSrc);
        user.setCmd("signUp");
        if (clientThread != null)
            send(user);
        else
            System.out.println("In Client.signUp(): 'clientThread' is null");

    }
    public void signIn(String email, String password){
        Message message = new Message();
        message.setEmail(email);
        message.setPassword(password);
        message.setCmd("signIn");
        send(message);
    }
    public void send(Object obj){
        if(obj != null) clientThread.send(obj);
        else System.out.println("Client.send(): 'obj' is null");
    }
    public static void main(String[] args) {
        Client client = new Client(System.getProperty("user.home") + File.separator + "ClientData");
        client.startTransmission();

        FileContainer fileContainer = new FileContainer("C:\\Users\\Dell\\Pictures\\PE\\ak.png");
        fileContainer.setDestId(1);
        client.send(fileContainer);

        client.send(new Message("a","b","c","d"));
        client.signUp("mk@o.pl","Michal","1234","afk",null);

        client.signIn("mk@o.pl","13234");

        client.saveDataBase();
    }
}
