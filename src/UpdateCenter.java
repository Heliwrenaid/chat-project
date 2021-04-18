import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UpdateCenter{
    private final DataBase dataBase;
    private final ServerThreadManager stm;
    private HashMap<Integer,Integer> actualUsers; // [userId, threadId] (from stm)

    private HashMap<Integer,ArrayList<Integer>> chats = new HashMap<>(); // [userId,destIds], destIds -> [users,groups]
    private HashMap<Integer,ArrayList<String>> messages = new HashMap<>(); // [userId, paths to messages]
    private HashMap<Integer,ArrayList<Message>> storedMessages = new HashMap<>(); // [userId,Message array]
    public UpdateCenter(DataBase dataBase, ServerThreadManager stm) {
        this.dataBase = dataBase;
        this.stm = stm;
        this.actualUsers = stm.getActualUsers();
        startGroupSynchronization();
        startMessageSynchronization();
        startStoredMessageSynchronization();
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
        if(message == null) return;
        switch (message.getCmd()){
            case "updateUser:true":{
                Chat chat = dataBase.getChat(message.getUserId());
                if(chat == null) return;
                addChat(chat.getSubscribers(),message.getUserId());
                if(chat.getUsers() != null){
                    ArrayList<Integer> ids = new ArrayList<>(chat.getUsers().keySet());
                    addChat(ids,message.getUserId());
                }
            }break;
            case "updateGroup:true":{
                Chat chat = dataBase.getChat(message.getDestId());
                if(chat == null) return;
                addChat(chat.getSubscribers(),message.getDestId());
            }break;
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

                        if(chat instanceof User){
                            // send private Message to second User
                            addStoredMessage(message.getDestId(),message);

                            // send User data to second User
                            Chat user = dataBase.getChat(message.getDestUserId());
                            if(user == null) return;
                            addChat(message.getDestId(),message.getDestUserId());
                            return;
                        }

                        // send joined User to all members of Chat
                        addChat(chat.getSubscribers(),message.getUserId());

                        //send Chat to all members of this Chat
                        addChat(chat.getSubscribers(),chat.getId());

                        // send all messages from Chat to joined User
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
                    case "banUser":
                    case "leave":{
                        Chat chat = dataBase.getChat(message.getDestId());
                        if(chat == null) return;

                        if (chat instanceof User){
                            // send private Message to second User
                            addStoredMessage(message.getDestId(),message);

                            // send User data to second User
                            Chat user = dataBase.getChat(message.getDestUserId());
                            if(user == null) return;
                            addChat(message.getDestId(),message.getDestUserId());
                            return;
                        }

                        // send message to user that was kicked
                        addStoredMessage(message.getInfo(),message);
                        // send Message to all member of Chat
                        addStoredMessage(chat.getSubscribers(),message);

                    }
                        break;
                    default:{
                        Chat chat = dataBase.getChat(message.getDestId());
                        if(chat == null) return;

                        if(chat instanceof User){
                            // send private Message to second User
                            addStoredMessage(message.getDestId(),message);

                            // send User data to second User
                            Chat user = dataBase.getChat(message.getDestUserId());
                            if(user == null) return;
                            addChat(message.getDestId(),message.getDestUserId());
                            return;
                        }

                        // send Message to all members of Chat
                        addStoredMessage(chat.getSubscribers(),message);
                    }
                }
            }
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
    public void addChat(int userId, int destId){
            if(chats.containsKey(userId)) chats.get(userId).add(destId);
            else {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(destId);
                chats.put(userId,arr);
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
    public void addStoredMessage(ArrayList<Integer> ids, Message message){
        if (ids == null) return;
        for(int id: ids){
            if(storedMessages.containsKey(id)) storedMessages.get(id).add(message);
            else {
                ArrayList<Message> arr = new ArrayList<>();
                arr.add(message);
                storedMessages.put(id,arr);
            }
        }
    }
    public void addStoredMessage(int userId, Message message){
        if(storedMessages.containsKey(userId)){
            storedMessages.get(userId).add(message);
        }
        else {
            ArrayList<Message> arr = new ArrayList<>();
            arr.add(message);
            storedMessages.put(userId,arr);
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
                        Thread.sleep(50);
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
                        Thread.sleep(50);
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
    public void startStoredMessageSynchronization(){
        Runnable listener = new Runnable() {
            public void run() {
                Iterator<Integer> iterator;
                int id;
                while (true){
                    try {
                        Thread.sleep(50);
                        iterator = storedMessages.keySet().iterator();
                        while (iterator.hasNext()){
                            id = iterator.next();
                            if(actualUsers.containsKey(id)){
                                sendStoredMessagesUpdate(id,storedMessages.get(id));
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
    public void sendStoredMessagesUpdate(int userId, ArrayList<Message> arr){
        if (arr == null) return;
        UpdateContainer updateContainer = new UpdateContainer();
        for (Message message:arr){
            updateContainer.add(message);
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
