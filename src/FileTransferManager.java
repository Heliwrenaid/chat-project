import java.io.*;
import java.net.Socket;

public class FileTransferManager implements TransferManager{
    private Socket socket = null;
    private ObjectOutputStream output = null;

    FileTransferManager(Socket socket){
        this.socket = socket;

    }

    @Override
    public void send(Object obj) {
        // obj is file path (String)
        FileEvent fileEvent = new FileEvent((String)obj);
        fileEvent.setDestinationDirectory(System.getProperty("user.home") + File.separator + "ServerData");
        try {
           output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(fileEvent);
            output.flush();
            //output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object read() {
        FileEvent fileEvent;
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            fileEvent = (FileEvent) input.readObject();
            if (fileEvent != null) {

                if (fileEvent.getStatus() == false) {
                    System.out.println("Error occur ..So exiting");
                    return null;
                }
                String outputFile = fileEvent.getDestinationDirectory() + File.separator + fileEvent.getFilename();
                if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                    new File(fileEvent.getDestinationDirectory()).mkdirs();
                }
                FileEvent dstFile = new FileEvent(outputFile);
                dstFile.setFileData(fileEvent.getFileData());
                dstFile.setStatus(true);

                File file = new File(outputFile);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(fileEvent.getFileData());
                fileOutputStream.flush();
                fileOutputStream.close();
                System.out.println("Output file : " + outputFile + " is successfully saved ");
                return dstFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}