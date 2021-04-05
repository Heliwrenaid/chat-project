import java.io.File;

public class GenMes {
    public static void main(String[] args){
        int userId = 2;
        int groupId = 3;

        String groupDir = System.getProperty("user.home") + File.separator + "ClientData" + File.separator +
                "groups" + File.separator + groupId;
        String messageDir = groupDir + File.separator + "messages";
        String fileDir = groupDir + File.separator + "files";

        String filePath = groupDir + File.separator + "avatar.png";


        Message message = new Message("cmd","message",
                Integer.toString(userId), Integer.toString(groupId));

        int start = 1;
        int end = 3;
        for (int i=start; i <= end;i++){
            message.setMessage("Message: " + i);
            Functions.save(message,messageDir+File.separator+i);

            FileContainer file = new FileContainer(filePath);
            file.setDestId(groupId);
            file.setUserId(userId);
            file.setFilename(Integer.toString(i+end-start+1));
            file.setOriginalFileName(file.getOriginalFileName() + (i+ end-start+1));

            file.setDestinationDirectory(fileDir);
            file.saveFileData();

            file.setDestinationDirectory(messageDir);
            file.saveFileMetadata();
        }
    }
}
