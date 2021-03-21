import java.io.File;
import java.net.Socket;

public class ClientThread extends FileTransferManager{
    protected DataBase dataBase;
    protected User actualUser = null;
    private Event event = null;

    public ClientThread(Socket socket, DataBase dataBase,Event event) {
        this.socket = socket;
        this.dataBase = dataBase;
        this.event = event;
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
            case "signUp:false":{
                System.out.println("Error: Account linked with " + message.getEmail() + " was not created");
                return;
            }
            case "signIn:false":{
                System.out.println("Error: " + message.getEmail() + " is not sign in");
                event.unblock();
                return;
            }
            case "messageResponse":{
                if(!message.isValid()) return;
                //if(!dataBase.verify(message)) return;
                if(message.getUserId() != actualUser.getId()) return; //TODO: dobre ??
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                chat.addMessageClient(message);
                return;
            }
        }
    }

    public void takeAction(User user){
        switch (user.getCmd()){
            case "signUp:true":{
                System.out.println("Account linked with " + user.getEmail() + " was created");
                dataBase.createUser(user);
                return;
            }
            case "signIn:true":{
                System.out.println("ClientThread.takeAction(User): " + user.getEmail() + " is sign in");
                actualUser = user;
                event.unblock();
                return;
            }
        }
    }

    public User getActualUser() {
        return actualUser;
    }

}
