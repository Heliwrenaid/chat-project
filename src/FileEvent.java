import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

public class FileEvent implements Serializable {

    public FileEvent(String filePath) {
        srcFilePath = filePath;
        fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        destDir = filePath.substring(0, filePath.lastIndexOf("\\") + 1);

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

    private static final long serialVersionUID = 1L;

    private String destDir;

    public String getSrcFilePath() {
        return srcFilePath;
    }

    private String srcFilePath;
    private String fileName;
    private long fileSize;
    private byte[] fileData;
    private boolean status; //true = success

    public String getDestinationDirectory() {
        return destDir;
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
}