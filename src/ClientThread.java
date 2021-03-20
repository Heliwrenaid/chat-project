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
    public void send(FileContainer fileContainer){
        if(fileContainer.getStatus() == false){
            System.out.println("in Client/Server Thread.send(): fileContainer has status: false");
            return;
        }
        send(fileContainer);
    }
    @Override
    public void takeAction(Object obj){
        switch (obj.getClass().getName()){
            case "FileContainer": takeAction((FileContainer) obj);
                break;
            case "Message": takeAction((Message) obj);
                break;
            case "User": takeAction((User) obj);
        }
    }
    public void takeAction(FileContainer fileContainer){}
    public void takeAction(Message message){
        switch (message.getCmd()){
            case "signUp:true":{
                System.out.println("Account linked with " + message.getEmail() + " was created");
                return;
            }
            case "signUp:false":{
                System.out.println("Error: Account linked with " + message.getEmail() + " was not created");
                return;
            }
        }
    }
    public void takeAction(User user){
    }



}
