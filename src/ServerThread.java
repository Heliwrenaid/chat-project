import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ServerThread extends ClientThread implements Runnable{
    private UpdateCenter updateCenter;
    private int threadId;
    public ServerThread(Socket socket,UpdateCenter updateCenter,int threadId) {
        this.socket = socket;
        this.updateCenter = updateCenter;
        this.dataBase = updateCenter.getDataBase();
        this.threadId = threadId;
    }

    @Override
    public void run() {
        //TODO: wątek w wątku -> moze przeznaczyć na send() ??
        startReading();
    }

    @Override
    public void takeAction(FileContainer fileContainer){
        if(!fileContainer.isValid()) return;
//        if(!dataBase.verify(fileContainer)) return;

        Chat chat= dataBase.getChat(fileContainer.getDestId());
        if(chat == null) {
            System.out.println("In ServerThread.takeAction(): chat doesn't exist");
            return;
        }
        fileContainer.setDestinationDirectory(chat.getFileDir());
        fileContainer.setFilename(Integer.toString(chat.nextMessageId()));
        fileContainer.saveFileData();
        fileContainer.saveFileMetadata();


    }

    @Override
    public void takeAction(Message message){
        switch (message.getCmd()){
            case "signIn":{

                boolean login;
                User user = dataBase.getUser(message.getEmail());
                if (user == null)
                    login = false;
                else if (user.getEmail().equals(message.getEmail())
                        && user.getPassword().equals(message.getPassword()))
                    login = true;
                else
                    login = false;

                if(login){
                    System.out.println(message.getEmail() + " is signed in");
                    updateCenter.addActualUser(user.getId(),threadId);
                    actualUser = user;
                    user.setCmd("signIn:true");
                    send(user);
                }
                else {
                    System.out.println("Error: " + message.getEmail() + " is not signed in");
                    send(new Message("signIn:false",message.getEmail(),null,null));
                }
                return;
            }
            case "messageRequest":{
                if(!message.isValid()) return;
                if(!dataBase.verify(message)) return;
                if(message.getUserId() != actualUser.getId()) return;
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;

                if(chat.verify(message)){
                    int newId = chat.addMessage(message);
                    if (newId == 0) return;
                    message.setInfo(newId);
                    message.setCmd("messageResponse");
                    send(message);
                }
            }

            case "groupManagement": {
               // if(!message.isValid()) return; TODO: sprawdza dla roznyych cmd?
                //if(!dataBase.verify(message)) return;
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                boolean status = chat.takeAction(message);

                // only for joining User
                boolean status2 = true;

                if(chat instanceof User){
                    int destId = message.getDestId();
                    int destUserId = message.getDestUserId();
                    message.setDestId(destUserId);
                    message.setDestUserId(destId);

                    Chat chat2 = dataBase.getChat(message.getDestId());
                    if(chat2 == null) return;
                    status2 = chat2.takeAction(message);
                }
                if(status && status2){
                    reloadActualUser();
                    message.setCmd("groupManagement:true");
                }
                else {
                    message.setCmd("groupManagement:false");
                }
                send(message);
                return;
            }
            case "updateUser":{
                if (message.getUserId() == 0) return;
                if(actualUser.getId() != message.getUserId()) return;

                if(message.getName() != null) actualUser.setName(message.getName());
                if(message.getPassword() != null) actualUser.setPassword(message.getPassword());
                if(message.getBio() != null) actualUser.setBio(message.getBio());
                if(message.getFile() != null)  actualUser.setAvatar(message.getFile());
                actualUser.save();
                message.setCmd("updateUser:true");
                System.out.println("User data was updated !!!");
                send(message);
                return;
                // not send if false
            }
            case "updateGroup":{
                boolean status = false;
                Group chat = (Group)dataBase.getChat(message.getDestId());
                if(chat != null){
                    status = chat.updateGroup(message);
                }
                if(status){
                    message.setCmd("updateGroup:true");
                    updateCenter.addUpdate(message);
                   // send(message);
                }
                else {
                    message.setCmd("updateGroup:false");
                    send(message);
                }
                return;
            }
            case "getChats":{
                if(dataBase.getIdSet() == null) return;
                Set<Integer> ids = dataBase.getIdSet().keySet();
                updateCenter.sendUpdate(actualUser.getId(),new ArrayList<>(ids));
            }
                break;
        }
    }
    @Override
    public void takeAction(User user) {
        switch (user.getCmd()){
            case "signUp":{
                String email = user.getEmail();
                if(dataBase.getEmails().containsKey(email)){
                    System.out.println("ServerThread.takeAction(): user account linked with " + email + " was not created");
                    send(new Message("signUp:false",email,null,null));
                }
                else {
                    System.out.println("ServerThread.takeAction(): user account linked with " + email + " was created");
                    User newUser = dataBase.createUser(user.getEmail(),user.getName(),user.getPassword(),user.getBio(),user.getAvatarSrc());
                    if(newUser != null){
                        newUser.setCmd("signUp:true");
                        send(newUser);
                    }

                }
                return;
            }
        }
    }
    @Override
    public void takeAction(Group group){
        switch (group.getCmd()){
            case "createGroup":{
               Group newGroup =  dataBase.createGroup(group,actualUser.getId());
               if (newGroup == null){
                   System.out.println("ServerThread.takeAction(Group): 'newGroup' is null");
                   Message message = new Message();
                   message.setCmd("createGroup:false");
                   message.setMessage("Group '" + group.getName() + "' was not created");
                   send(message);
               }
               else{
                   System.out.println("Group '" + group.getName() + "' was created");
                   newGroup.setCmd("createGroup:true");
                   send(newGroup);
               }
               return;
            }
        }
    }
    @Override
    public void stop(){
        try {
            updateCenter.removeActualUser(actualUser.getId());
            isRunning = false;
            output.close();
            socket.close();
            System.out.println("Socket is closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getThreadId() {
        return threadId;
    }
}