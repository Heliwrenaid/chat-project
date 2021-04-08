import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UpdateCenter{
    private DataBase dataBase;
    private ServerThreadManager stm;
    private HashMap<Integer,Integer> actualUsers; // [userId, threadId] (from stm)

    private HashMap<Integer,ArrayList<Integer>> tasks = new HashMap<>(); // [taskId=userId,destIds], destIds -> [users,groups]
    private HashMap<Integer,ArrayList<String>> messages = new HashMap<>(); // [userId, paths to messages]

    public UpdateCenter(DataBase dataBase, ServerThreadManager stm) {
        this.dataBase = dataBase;
        this.stm = stm;
        this.actualUsers = stm.getActualUsers();
        startGroupSynchronization();
        startMessageSynchronization();
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
        Chat chat = dataBase.getChat(message.getDestId());
        if(chat == null) return;
        switch (message.getCmd()){
            case "updateGroup:true":{
                addTasks(chat.getSubscribers(),message.getDestId());
            } break;
            case "messageResponse":{
                String messagePath = chat.getMessageDir() +
                        File.separator + message.getInfo(); //getInfo -> newNextId
                addMessages(chat.getSubscribers(),messagePath);
            } break;
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
    public void addMessages(ArrayList<Integer> ids, String messagePath){
        if (ids == null) return;
        for(int id: ids){
            if(messages.containsKey(id)) messages.get(id).add(messagePath);
            else {
                ArrayList<String> arr = new ArrayList<>();
                arr.add(messagePath);
                messages.put(id,arr);
            }
        }
    }
    public void startGroupSynchronization(){
        Runnable listener = new Runnable() {
            public void run() {
                Iterator <Integer> iterator;
                int id;
                while (true){
                    try {
                        Thread.sleep(200);
                        iterator = tasks.keySet().iterator();
                        while (iterator.hasNext()){
                            id = iterator.next();
                            if(actualUsers.containsKey(id)){
                                sendGroupsUpdate(id,tasks.get(id));
                                iterator.remove();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    public void startMessageSynchronization(){
        Runnable listener = new Runnable() {
            public void run() {
                Iterator<Integer> iterator;
                int id;
                while (true){
                    try {
                        Thread.sleep(200);
                        iterator = messages.keySet().iterator();
                        while (iterator.hasNext()){
                            id = iterator.next();
                            if(actualUsers.containsKey(id)){
                                sendMessagesUpdate(id,messages.get(id));
                                iterator.remove();
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    public void sendGroupsUpdate(int userId, ArrayList<Integer> arr){
        if (arr == null) return;
        UpdateContainer updateContainer = new UpdateContainer();
        for (int i:arr){
            Chat chat = dataBase.getChat(i);
            updateContainer.add(chat);
        }
        updateContainer.calculateAmount();
        stm.getServerThread(userId).send(updateContainer);
    }
    public void sendMessagesUpdate(int userId, ArrayList<String> arr){
        if (arr == null) return;
        UpdateContainer updateContainer = new UpdateContainer();
        for (String path:arr){
            Message message = (Message) Functions.getObject(path);
            updateContainer.add(message);
        }
        updateContainer.calculateAmount();
        stm.getServerThread(userId).send(updateContainer);
    }
}
