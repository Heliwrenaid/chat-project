import java.util.ArrayList;
import java.util.HashMap;

public class UpdateCenter extends Thread{
    private DataBase dataBase;
    private ServerThreadManager stm;
    private HashMap<Integer,Integer> actualUsers; // [userId, threadId] (from stm)
    private HashMap<Integer,ArrayList<Integer>> tasks = new HashMap<>(); // [taskId=userId,destIds], destIds -> [users,groups]
    public UpdateCenter(DataBase dataBase, ServerThreadManager stm) {
        this.dataBase = dataBase;
        this.stm = stm;
        this.actualUsers = stm.getActualUsers();
        start();
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public ServerThreadManager getStm() {
        return stm;
    }
    public void addActualUser(int userId,int threadId){
        if(actualUsers == null) return;
        if(actualUsers.containsKey(userId)) return;
        actualUsers.put(userId,threadId);
    }
    public void removeActualUser(int userId){
        if(actualUsers == null) return;
        if(!actualUsers.containsKey(userId)) return;
        actualUsers.remove(userId);
    }
    public void addUpdate(Message message){
        switch (message.getCmd()){
            case "updateGroup:true":{
                Group chat = (Group)dataBase.getChat(message.getDestId());
                addTasks(chat.getSubscribers(),message.getDestId());
            }
        }
    }
    public void addTasks(ArrayList<Integer> ids, int destId){
        if (ids == null) return;
        for(int id: ids){
            if(tasks.containsKey(id)) tasks.get(id).add(destId);
            else {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(destId);
                tasks.put(id,arr);
            }
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int id:tasks.keySet()){
                if(actualUsers.containsKey(id)){
                    sendUpdate(id,tasks.get(id));
                }
            }
        }
    }
    public void sendUpdate(int userId,ArrayList<Integer> arr){
        if (arr == null) return;
        for (int i:arr){
            Chat chat = dataBase.getChat(i);
            if(chat != null) {
              //  chat.setCmd("updateChat");
                stm.getServerThread(userId).send(chat);
            }

        }
        tasks.remove(userId);

    }
}
