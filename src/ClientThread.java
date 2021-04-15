import java.net.Socket;

public class ClientThread extends FileTransferManager{
    protected DataBase dataBase;
    protected User actualUser = null;
    private Event event = null;
    private boolean debug = true;

    public ClientThread(Socket socket, DataBase dataBase,Event event) {
        this.socket = socket;
        this.dataBase = dataBase;
        this.event = event;
        startReading();
    }
    public ClientThread(){}

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
            case "UpdateContainer": takeAction((UpdateContainer) obj);
                break;
        }
    }
    public void takeAction(FileContainer fileContainer){
        switch (fileContainer.getCmd()) {
            case "sendFile:true": {
                Chat chat = dataBase.getChat(fileContainer.getDestId());
                if (chat == null) {
                    System.out.println("ClientThread.takeAction(FileContainer): 'chat' is null");
                    return;
                }
                chat.addFileClient(fileContainer);
                System.out.println(fileContainer.getOriginalFileName() + " was sent");
                return;
            }
            case "getFile:true":{
                Chat chat = dataBase.getChat(fileContainer.getDestId());
                if (chat == null) {
                    System.out.println("ClientThread.takeAction(FileContainer): 'chat' is null");
                    return;
                }
                fileContainer.setDestinationDirectory(chat.getFileDir());
                fileContainer.setFilename(fileContainer.getOriginalFileName());
                fileContainer.saveFileData();
                System.out.println(fileContainer.getOriginalFileName() + " is downloaded");
                return;
            }
        }
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
                //if(!message.isValid()) return;
                //if(!dataBase.verify(message)) return;
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

                // only for joining User
//                boolean status2 = true;
//                if(chat instanceof User){
//                    int destId = message.getDestId();
//                    int destUserId = message.getDestUserId();
//                    message.setDestId(destUserId);
//                    message.setDestUserId(destId);
//
//                    Chat chat2 = dataBase.getChat(message.getDestId());
//                    if(chat == null) return;
//                    status2 = chat.takeAction(message);
//                }

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
                if(!dataBase.checkIfUserExists(user.getEmail())){
                    System.out.println("ClientThread.takeAction(User): " + "Downloading all user data");
                    Message message = new Message();
                    message.setCmd("downloadUserData");
                    message.setUserId(user.getId());
                    message.setPassword(user.getPassword());
                    send(message);
                }
                dataBase.updateUser(user);
                actualUser = dataBase.getUser(user.getId());
                event.unblock();
                return;
            }
        }
    }
    public void takeAction(UpdateContainer updateContainer){
        if (updateContainer.getAmount() == 0) return;
        System.out.println("\n-------- UpdateContainer is received (" + updateContainer.getAmount()+ ") ---------");
        if (updateContainer.hasChats()){
            for(Chat chat : updateContainer.getChats()){
                if(debug) System.out.println("Received: chat with id: " + chat.getId());
                Chat oldChat = dataBase.getChat(chat.getId());
                if(oldChat != null){
                    oldChat.updateChat(chat);
                }
                else {
                    if(chat instanceof Group)
                        dataBase.createGroup((Group)chat);
                    if(chat instanceof User)
                        dataBase.updateUser((User) chat);
                }
            }
        }
        if (updateContainer.hasMessages()){
            for(Message message : updateContainer.getMessages()){
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                chat.addMessageClient(message);
                if(debug) message.print();
            }
        }
        if(updateContainer.hasFiles()){
            for (FileContainer file : updateContainer.getFiles()){
                Chat chat = dataBase.getChat(file.getDestId());
                if(chat == null) return;
                chat.addFileClient(file);
                if(debug) file.print();
            }
        }
        System.out.println("----------------------------------------------\n");
    }

    public User getActualUser() {
        return actualUser;
    }
    public void reloadActualUser() {
        actualUser = User.loadUser(actualUser.getSavePath());
    }

    public void updateChat(Chat chat){
        Chat oldChat = dataBase.getChat(chat.getId());
        oldChat.setName(chat.getName());
        oldChat.setBio(chat.getBio());
        //oldChat.setMessages(chat.getMessages());
        oldChat.setUsers(chat.getUsers());

    }
}

