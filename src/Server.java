import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Server extends Thread{

    private String host;
    private int port;
    private boolean isEnabled = false;
    private DataBase dataBase;
    private String mainDir;
    private String dataBasePath;

    public Server(String host, int port, String mainDir) {
        this.mainDir = mainDir;
        this.host = host;
        this.port = port;
        this.dataBasePath = mainDir + File.separator + "db";
        loadDataBase(dataBasePath);
    }

    public void startListening(){
        Runnable listener = new Runnable() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Server listens at: " + port);
                    isEnabled = true;

                    while(isEnabled){
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Connected to new client");
                        new ServerThread(clientSocket,dataBase);
                    }
                } catch (IOException e) {
                    System.out.println("Error in startListening(): " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        new Thread(listener).start();
    }

    public void saveDataBase() {
       dataBase.save();
    }
    public void loadDataBase(String dataBasePath) {
        dataBase = DataBase.loadData(dataBasePath);
        if(dataBase != null){
            System.out.println("dataBase was successfully loaded");
        }
        else {
            System.out.println("dataBase data was not found .. creating new one ...");
            dataBase = new DataBase(mainDir);
            System.out.println("Done");
        }
    }

    public static void main(String args[]){
        if (args.length < 2) return;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Server server = new Server(host,port,System.getProperty("user.home") + File.separator + "ServerData");
        server.dataBase.createUser("Jan","12",null,null);
        server.startListening();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String p = null;
            try {
                p = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (p.equals("x")) break;
            if(p.equals("s")) server.saveDataBase();
        }
        server.saveDataBase();
    }

}
