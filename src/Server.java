import java.io.*;
import java.net.*;

public class Server extends Thread{

    private final int port;
    private boolean isEnabled = false;
    private DataBase dataBase;
    private final String mainDir;
    private final String dataBasePath;
    private ServerThreadManager stm;
    private UpdateCenter updateCenter;

    public Server(int port, String mainDir) {
        this.mainDir = mainDir;
        this.port = port;
        this.dataBasePath = mainDir + File.separator + "db";
        loadDataBase(dataBasePath);
    }

    public void startListening(int nThreads){
        stm = new ServerThreadManager(nThreads);
        updateCenter = new UpdateCenter(dataBase,stm);
        Runnable listener = new Runnable() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Server listens at: " + port);
                    isEnabled = true;

                    while(isEnabled){
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Connected to new client");
                        createServerThread(clientSocket);
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
    public void createServerThread(Socket clientSocket){
        stm.createThread(clientSocket,updateCenter);
    }

    public static void main(String[] args){
        if (args.length < 1){
            System.out.println("Please specify port");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            System.out.println("Server.main: Given port is not 'int'");
            return;
        }

        Server server = new Server(port,System.getProperty("user.home") + File.separator + "ServerData");
        server.dataBase.createUser("admin","admin","admin","im dumb admin",null);
        server.startListening(100);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String input = null;
            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (input.equals("x"))
                break;
            if (input.equals("d"))
                continue; //debug here
            if(input.equals("s")) server.saveDataBase();
        }
        server.saveDataBase();
    }

    public DataBase getDataBase() {
        return dataBase;
    }
}
