import java.net.Socket;

public class ServerThread extends ClientThread{
    public ServerThread(Socket socket, DataBase dataBase) {
        this.socket = socket;
        this.dataBase = dataBase;
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
                if(status){
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
                    send(message);
                }
                else {
                    message.setCmd("updateGroup:false");
                    send(message);
                }
                return;
            }
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
}