import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class FileTransferManager{
    protected Socket socket = null;
    protected ObjectOutputStream output = null;
    protected boolean isRunning = true;
    public void send(Object obj) {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(obj);
            output.flush();
            //output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object read() {
        Object obj;
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            obj = input.readObject();
            if (obj != null) {
                return obj;
            }
        } catch (EOFException e){
            if(isRunning) stop();
        } catch (SocketException e){
           if(isRunning) stop();
        } catch (StreamCorruptedException e){
            e.printStackTrace();
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
                while (!socket.isClosed()) {
                    try {
                        Thread.sleep(180);
                        obj = read();
                        if(obj != null){
                            takeAction(obj);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }

    public void takeAction(Object obj){}

    public void stop(){
        try {
            isRunning = false;
            output.close();
            socket.close();
            System.out.println("Socket is closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}