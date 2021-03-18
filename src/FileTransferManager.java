import java.io.*;
import java.net.Socket;

public class FileTransferManager{
    protected Socket socket = null;
    protected ObjectOutputStream output = null;

    public void sendFileContainer(FileContainer fileContainer) {
        if (fileContainer.getStatus() == false) {
            System.out.println("FileContiner has status: false");
            return;
        }
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(fileContainer);
            output.flush();
            //output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileContainer read() {
        FileContainer fileContainer;
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            fileContainer = (FileContainer) input.readObject();
            if (fileContainer != null) {
                return fileContainer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void startReading() {
        Runnable listener = new Runnable() {
            public void run() {
                Object obj;
                while (socket.isConnected()) {
                    System.out.println("loooooping");
                    //Thread.sleep();
                    obj = read();
                    if(obj != null){
                        takeAction((FileContainer)obj);
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    public void saveFileData(FileContainer fileContainer){
        String outputFilePath = fileContainer.getDestinationDirectory() + File.separator + fileContainer.getFilename();
        if (!new File(fileContainer.getDestinationDirectory()).exists()) {
            new File(fileContainer.getDestinationDirectory()).mkdirs();
        }

        File file = new File(outputFilePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileContainer.getFileData());
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
    public void saveFileMetadata(FileContainer fileContainer){
        FileContainer metadata = new FileContainer(fileContainer);
        try {
            FileOutputStream file = new FileOutputStream(metadata.getDestinationDirectory()+File.separator+metadata.getFilename()+".metadata");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(metadata);
            output.close();
        } catch (Exception e) {
            System.out.println("In FileTransferManager.saveFileMetadata() error occurred: "+ e.getMessage());
        }
    }
    public void takeAction(FileContainer fileContainer){}
}