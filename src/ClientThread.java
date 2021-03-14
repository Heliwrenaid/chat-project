import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientThread extends Thread{
    private Socket socket = null;
    private DataBase dataBase;
    private TransferManager tm;
    private User user;

    public ClientThread(Socket socket, DataBase dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
        tm = new FileTransferManager(socket);
        start();
    }
    public void run() {
            Object obj;
            while (socket.isConnected()) {
                System.out.println("loooooping");
                //Thread.sleep();
                obj = tm.read();
               // doAction(obj);
            }
    }
    public void send(String file){
        tm.send(file);
    }
    User signUp(){
        //TODO: return User on null
        user = dataBase.createUser("Jan","1234","sth",null);
        return null;
    }
    void doAction(Object obj){
        if(obj != null){
            if(obj instanceof FileEvent){
                FileEvent fileEvent = (FileEvent) obj;
                Functions.save(fileEvent.getFileData(),dataBase.getMainDir() + File.separator + fileEvent.getFilename());
            }
        }
    }


}
