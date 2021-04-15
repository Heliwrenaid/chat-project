import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UpdateCenter{
    private DataBase dataBase;
    private ServerThreadManager stm;
    private HashMap<Integer,Integer> actualUsers; // [userId, threadId] (from stm)

    private HashMap<Integer,ArrayList<Integer>> chats = new HashMap<>(); // [userId,destIds], destIds -> [users,groups]
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
    public boolean addActualUser(int userId,int threadId){
        if(actualUsers == null) return false;
        if(actualUsers.containsKey(userId)) return false;
        actualUsers.put(userId,threadId);
        return true;
    }
    public void removeActualUser(int userId){
        if(actualUsers == null) return;
        if(!actualUsers.containsKey(userId)) return;
        actualUsers.remove(userId);
    }
    public void addUpdate(Message message){
        switch (message.getCmd()){
            case "updateGroup:true":{
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                addChat(chat.getSubscribers(),message.getDestId());
            } break;
            case "messageResponse":{
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                String messagePath = chat.getMessageDir() +
                        File.separator + message.getInfo(); //getInfo -> newNextId
                addMessage(chat.getSubscribers(),messagePath);
            } break;
            case "groupManagement:true":{
                switch (message.getSubCmd()){
                    case "join":{
                        Chat chat = dataBase.getChat(message.getDestId());
                        if(chat == null) return;
                        // send joined User to all members of Chat
                        addChat(chat.getSubscribers(),message.getUserId());

                        //send Chat to all members of this Chat
                        addChat(chat.getSubscribers(),chat.getId());

                        // send all messages from Chat to joined User
                        if(chat instanceof User) return;
                        ArrayList <Integer> messageIds = chat.getMessages();
                        if(messageIds != null){
                            String messageDir = chat.getMessageDir();
                            ArrayList <String> messagePaths = new ArrayList<>();
                            for(int i : messageIds){
                                messagePaths.add(messageDir + File.separator + i);
                            }
                            addMessages(message.getUserId(),messagePaths);
                        }
                    }
                    break;
                }
            }
                break;
//            case "downloadUserData":{
//                User user = dataBase.getUser(message.getUserId());
//                if(user == null) return;
//                // send all subscribed Chats to User
//                addChats(user.getId(),user.getSubscribedChats());
//
//                // send all messages from all subscribed Chats to User
//                for (int chatId : user.getSubscribedChats()){
//                    Chat chat1 = dataBase.getChat(chatId);
//
//                    ArrayList <Integer> messageIds = new ArrayList<>();
//                    messageIds.addAll(chat1.getMessages());
//                    if(messageIds != null){
//                        String messageDir = chat1.getMessageDir();
//                        ArrayList <String> messagePaths = new ArrayList<>();
//                        for(int i : messageIds){
//                            messagePaths.add(messageDir + File.separator + i);
//                        }
//                        addMessages(user.getId(),messagePaths);
//                    }
//                }
//            }
//            break;
        }
    }
    public void addUpdate(FileContainer fileContainer){
        Message message = new Message();
        message.setFileContainer(fileContainer);
        message.setUserId(fileContainer.getUserId());
        message.setDestId(fileContainer.getDestId());
        message.setInfo(Integer.parseInt(fileContainer.getFilename()));
        message.setCmd("messageResponse");
        message.setSubCmd("FileMetaData");
        addUpdate(message);
    }
    public void addChat(ArrayList<Integer> ids, int destId){
        if (ids == null) return;
        for(int id: ids){
            if(chats.containsKey(id)) chats.get(id).add(destId);
            else {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(destId);
                chats.put(id,arr);
            }
        }
    }
    public void addChats(int userId, ArrayList<Integer> chatIds){
        if (chatIds == null) return;
        if(!chats.containsKey(userId)){
            chats.put(userId,chatIds);
        } else {
            for(int id : chatIds){
                if(!chats.get(userId).contains(id)) chats.get(userId).add(id);
            }
        }
    }
    public void addMessage(ArrayList<Integer> ids, String messagePath){
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
    public void addMessages(int userId, ArrayList<String> messagePaths){
        if (messagePaths == null) return;
        if(!messages.containsKey(userId)){
            messages.put(userId,messagePaths);
        } else {
            for(String path : messagePaths){
                if(!messages.get(userId).contains(path)) messages.get(userId).add(path);
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
                        iterator = chats.keySet().iterator();
                        while (iterator.hasNext()){
                            id = iterator.next();
                            if(actualUsers.containsKey(id)){
                                sendChatsUpdate(id, chats.get(id));
                                iterator.remove();
                            }
                        }
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
                        iterator = messages.keySet().iterator();
                        while (iterator.hasNext()){
                            id = iterator.next();
                            if(actualUsers.containsKey(id)){
                                sendMessagesUpdate(id,messages.get(id));
                                iterator.remove();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(listener).start();
    }
    public void sendChatsUpdate(int userId, ArrayList<Integer> arr){
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
            Object obj =  Functions.getObject(path);
            updateContainer.add(obj);
        }
        updateContainer.calculateAmount();
        stm.getServerThread(userId).send(updateContainer);
    }
    public UpdateContainer genUpdateContainer(boolean safeMode, ArrayList<Integer> chats, ArrayList<Integer> chatsForMessagesSync){
        if(chats == null && chatsForMessagesSync == null) return null;
        UpdateContainer updateContainer = new UpdateContainer();
        if(chats != null){
            for(int chatId : chats){
                Chat chat = dataBase.getChat(chatId);
                updateContainer.add(chat);
            }
        }
        if(chatsForMessagesSync != null){
            for(int chatId : chatsForMessagesSync){
                Chat chat = dataBase.getChat(chatId);
                if(chat != null){
                    ArrayList <Integer> messageIds = chat.getMessages();
                    if(messageIds != null){
                        String messageDir = chat.getMessageDir();
                        String messagePath;
                        for(int i : messageIds){
                            messagePath = messageDir + File.separator + i;
                            Object obj = Functions.getObject(messagePath);
                            updateContainer.add(obj);
                        }
                    }
                    if(safeMode) updateContainer.add(chat);
                }
            }
        }

        updateContainer.calculateAmount();
        if (updateContainer.getAmount() == 0) return null;
        return updateContainer;
    }
}
