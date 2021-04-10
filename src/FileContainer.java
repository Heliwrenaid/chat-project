import java.io.*;
import java.util.Arrays;

public class FileContainer implements Serializable{
    private String destDir;
    private String srcFilePath;
    private String fileName;
    private String originalFileName;
    private long fileSize;
    private byte[] fileData;
    private boolean status; //true = success
    private int userId=0;
    private int destId=0;
    private String metadataExt = "";
    private String cmd;
    private String info;
    private String info1; // when chating with User

    public FileContainer(String filePath) {
        srcFilePath = filePath;
        fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        destDir = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
        originalFileName = fileName;
        writeFileData(srcFilePath);
    }

    // copy without fileData -> for saving metadata
    public FileContainer(FileContainer file) {
        this.destDir = file.getDestinationDirectory();
        this.srcFilePath = file.getSrcFilePath();
        this.fileName = file.getFilename();
        this.originalFileName = file.getOriginalFileName();
        this.fileSize = file.getFileSize();
        this.status = file.getStatus();
        this.userId = file.getUserId();
        this.destId = file.getDestId();
        this.metadataExt = file.getMetadataExt();
        this.info = file.getInfo();
        this.info1 = file.getInfo1();
    }
    public void print(){
        System.out.println("-------Print FileContainer-------");
        System.out.println("cmd: " + cmd);
        System.out.println("fileName: " + fileName);
        System.out.println("originalFileName: " + originalFileName);
        System.out.println("userId: " + userId);
        System.out.println("destId: " + destId);
        System.out.println("info: " + info);
        System.out.println("info1: " + info1);
        System.out.println("---------------------------");
    }

    public void saveFileData(){
        String outputFilePath = destDir + File.separator + fileName;
        if (!new File(destDir).exists()) {
            new File(destDir).mkdirs();
        }

        File file = new File(outputFilePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileData);
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println(outputFilePath + " was successfully saved");

        } catch (FileNotFoundException e) {
            System.out.println(outputFilePath + " was not saved");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(outputFilePath + " was not saved");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println(outputFilePath + " was not saved");
            e.printStackTrace();
        }

    }
    public void saveFileMetadata(){
        try {
            FileOutputStream file = new FileOutputStream(destDir+File.separator+fileName+ metadataExt);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(new FileContainer(this));
            output.close();
        } catch (Exception e) {
            System.out.println("In FileContainer.saveFileMetadata() error occurred: "+ e.getMessage());
        }
    }
    public void writeFileData(String filePath){
        File file = new File(filePath);
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
    public boolean isValid(){
        if (fileData == null) return false;
        if (fileSize == 0) return false;
        if (destId == 0) return false;
        if (fileName == null) return false;
        return true;
    }


    @Override
    public String toString() {
        return originalFileName;
    }

    // Getters and setters ---------------------------------------------------------------
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

    public String getMetadataExt() {
        return metadataExt;
    }

    public void setMetadataExt(String metadataExt) {
        this.metadataExt = metadataExt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDestId() {
        return destId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }
}