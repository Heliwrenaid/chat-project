import java.io.*;

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
        }

    }
    public void saveFileMetadata(){
        fileData = null;
        try {
            FileOutputStream file = new FileOutputStream(destDir+File.separator+fileName+".metadata");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(this);
            output.close();
        } catch (Exception e) {
            System.out.println("In FileContainer.saveFileMetadata() error occurred: "+ e.getMessage());
        }
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

    public boolean isValid(){
        if (fileData == null) return false;
        if (fileSize == 0) return false;
        if (destId == 0) return false;
        if (fileName == null) return false;
        return true;
    }
}