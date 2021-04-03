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
                break;
            case "Group": takeAction((Group) obj);
                break;
            case "Channel": takeAction((Channel) obj);
                break;
        }
    }
    public void takeAction(FileContainer fileContainer){
        System.out.println("testyyyy");
        fileContainer.setDestinationDirectory(actualUser.getFileDir());
        fileContainer.saveFileData();
    }

    public void takeAction(Group group){
        switch (group.getCmd()){
            case "createGroup:true":{
                dataBase.createGroup(group);
                actualUser.subscribeChat(group.getId());
                System.out.println("Group '" + group.getName() + "' was created");
                return;
            }
        }
    }
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
                if(message.getUserId() != actualUser.getId()) return; //TODO: dobre ?? dobre
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                chat.addMessageClient(message);
                return;
            }
            case "createGroup:false":{
                System.out.println(message.getMessage());
                return;
            }
            case "groupManagement:true":{
                //if(!message.isValid()) return;
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                boolean status = chat.takeAction(message);
                if (status) {
                    reloadActualUser();
                    System.out.println("ClientThread: GroupManagement: operation successful");
                }
                else
                    System.out.println("ERROR: ClientThread: GroupManagement: operation successful only on Server ");
                message.print();
                return;
            }
            case "groupManagement:false":{
                System.out.println("ClientThread : GroupManagement: operation failed");
                message.print();
                return;
            }
            case "updateUser:true":{
                if (message.getUserId() == 0) return;
                if(actualUser.getId() != message.getUserId()) return;

                if(message.getName() != null) actualUser.setName(message.getName());
                if(message.getPassword() != null) actualUser.setPassword(message.getPassword());
                if(message.getBio() != null) actualUser.setBio(message.getBio());
                if(message.getFile() != null) actualUser.setAvatar(message.getFile());
                actualUser.save();
                System.out.println("User data was updated !!!");
                return;
            }
            case "updateGroup:true":{
                boolean status = false;
                Group chat = (Group)dataBase.getChat(message.getDestId());
                if(chat != null){
                    status = chat.updateGroup(message);
                }
                if(status){
                   System.out.println("Group info was updated");
                }
                else {
                    System.out.println("Group info wasn't updated only on Client side");
                }
                return;
            }
            case "updateGroup:false":{
                System.out.println("Group info wasn't updated");
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
                dataBase.updateUser(user);
                actualUser = dataBase.getUser(user.getId());
                //TODO: will it delete messages & files ??
                event.unblock();
                return;
            }
        }
    }

    public User getActualUser() {
        return actualUser;
    }
    public void reloadActualUser(){
        actualUser = User.loadUser(actualUser.getSavePath());
    }

}
