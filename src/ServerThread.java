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
        message.print();
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
                    dataBase.createUser(user.getEmail(),user.getName(),user.getPassword(),user.getBio(),user.getAvatarSrc());
                    send(new Message("signUp:true",email,null,null));
                }
                return;
            }
        }
    }
}