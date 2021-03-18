import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

public class FileContainer implements Serializable{
    private String destDir;
    private String srcFilePath;
    private String fileName;
    private String originalFileName;
    private long fileSize;
    private byte[] fileData;
    private boolean status; //true = success
    private int groupId=0;
    private int channelId=0;
    private int userId=0;
    private int userChatId=0;

    public FileContainer(String filePath) {
        srcFilePath = filePath;
        fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        destDir = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
        originalFileName = fileName;

        File file = new File(srcFilePath);
        if (file.isFile()) {
            try {
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileSize = len;
                fileData = fileBytes;
                status = true;
            } catch (Exception e) {
                e.printStackTrace();
                status = false;
            }
        } else {
            System.out.println("Specified path not point to a file");
            status = false;
        }
    }
    //create metadata for file
    public FileContainer(FileContainer fileContainer){
        destDir = fileContainer.getDestinationDirectory();
        srcFilePath = fileContainer.srcFilePath;
        fileName = fileContainer.fileName;
        originalFileName = fileContainer.originalFileName;
        fileSize = fileContainer.fileSize;
        status = fileContainer.status;
        groupId = fileContainer.groupId;
        channelId = fileContainer.channelId;
        userId = fileContainer.userId;
        userChatId = fileContainer.userChatId;
    }

    public String getDestinationDirectory() {
        return destDir;
    }

    public String getSrcFilePath() {
        return srcFilePath;
    }

    public void setDestinationDirectory(String destinationDirectory) {
        this.destDir = destinationDirectory;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String filename) {
        this.fileName = filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getUserChatId() {
        return userChatId;
    }

    public void setUserChatId(int userChatId) {
        this.userChatId = userChatId;
    }
    public boolean isForUser(){
        if(userChatId !=0) return true;
        else return false;
    }
    public boolean isForGroup(){
        if(groupId !=0) return true;
        else return false;
    }
    public boolean isForChannel(){
        if(channelId !=0) return true;
        else return false;
    }
    public boolean isValid(){
        return true;
    }
}