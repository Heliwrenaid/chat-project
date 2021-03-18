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
        if(fileContainer.isForGroup()){
            Group group = dataBase.getGroup(fileContainer.getGroupId());
            if (group == null){
                System.out.println("ServerThread.takeAction(): 'group' doesn't exist");
            }
            fileContainer.setDestinationDirectory(group.getFileDir());
            fileContainer.setFilename(Integer.toString(group.nextMessageId()));
            saveFileData(fileContainer);
            saveFileMetadata(fileContainer);
        }
        else if (fileContainer.isForChannel()){
            Channel channel = dataBase.getChannel(fileContainer.getChannelId());
            if (channel == null){
                System.out.println("ServerThread.takeAction(): 'channel' doesn't exist");
            }
            fileContainer.setDestinationDirectory(channel.getFileDir());
            fileContainer.setFilename(Integer.toString(channel.nextMessageId()));
            saveFileData(fileContainer);
            saveFileMetadata(fileContainer);
        }
        else if (fileContainer.isForUser()){
            User user = dataBase.getUser(fileContainer.getUserChatId());
            if (user == null){
                System.out.println("ServerThread.takeAction(): 'user' doesn't exist");
            }
            fileContainer.setDestinationDirectory(user.getFileDir());
            fileContainer.setFilename(Integer.toString(user.nextMessageId()));
            saveFileData(fileContainer);
            saveFileMetadata(fileContainer);
        }
    }

}