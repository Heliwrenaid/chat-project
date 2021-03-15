import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientThread extends FileTransferManager{
    protected DataBase dataBase;
    protected User user;

    public ClientThread(Socket socket, DataBase dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
        startReading();
    }
    public ClientThread(){}

    User signUp(){
        //TODO: return User on null
        user = dataBase.createUser("Jan","1234","sth",null);
        return null;
    }
    public void send(String filePath){
        FileContainer fileContainer = new FileContainer(filePath);
        fileContainer.setDestinationDirectory(System.getProperty("user.home") + File.separator + "ServerData");
        sendFileContainer(fileContainer);
    }
    @Override
    public void takeAction(FileContainer fileContainer){
        fileContainer.setDestinationDirectory(dataBase.getMainDir());
        saveFileContiner(fileContainer);
    }


}
