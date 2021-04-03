import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerThreadManager{
    private HashMap<Integer,Integer> actualUsers = new HashMap<>(); // [userId, threadId]
    private HashMap<Integer,ServerThread> serverThreads = new HashMap<>(); // [threadId, ServerThread]
    private ThreadPoolExecutor executor;
    public ServerThreadManager(int nThreads) {
       executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
    }
    public void createThread(Socket socket, DataBase dataBase){
        int newId = Functions.freeId(serverThreads.keySet());
        ServerThread serverThread = new ServerThread(socket,dataBase,actualUsers,newId);
        executor.execute(serverThread);
        serverThreads.put(newId,serverThread);
    }
    public ServerThread getServerThread(int userId){
        if (!actualUsers.containsKey(userId)) return null;
        return serverThreads.get(actualUsers.get(userId));
    }
}
