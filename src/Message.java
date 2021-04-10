import java.io.Serializable;

public class Message implements Serializable {
    private String cmd;
    private String message;
    private String userId;
    private String destId;
    private String info;
    private FileContainer file;
    private String info1;

    public Message(String cmd, String message, String userId, String destId) {
        this.cmd = cmd;
        this.message = message;
        this.userId = userId;
        this.destId = destId;
    }
    public Message(String message, int userId, int destId){
        this.message = message;
        this.userId = Integer.toString(userId);
        this.destId = Integer.toString(destId);
    }
    public Message(){}

    public void print(){
        System.out.println("-------Print Message-------");
        System.out.println("cmd: " + cmd);
        System.out.println("message: " + message);
        System.out.println("userId: " + userId);
        System.out.println("destId: " + destId);
        System.out.println("info: " + info);
        System.out.println("info1: " + info1);
        System.out.println("---------------------------");
    }
    public boolean isValid(){
        if(userId == null) return false;
        if(destId == null) return false;
        if(cmd == null) return false;
        if(message == null) return false;
        return true;
    }
    public void setUserData(String name, String pass, String bio, String avatarSrc){
        message = name;
        destId = pass;
        info = bio;
        if(avatarSrc != null)
            file = new FileContainer(avatarSrc);
    }
    public void setGroupData(int groupId,String name, String bio, String avatarSrc){
        message = name;
        info = bio;
        setDestId(groupId);
        if(avatarSrc != null)
            file = new FileContainer(avatarSrc);
    }

    // ---------------- getters & setters -----------------

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        if (userId == null) return 0;
        return Integer.valueOf(userId);
    }

    public void setUserId(int userId) {
        this.userId = Integer.toString(userId);
    }

    public int getDestId() {
        int a;
        try {
            a = Integer.parseInt(destId);
        } catch (NumberFormatException e){
            return 0;
        }
       return a;
    }

    public void setDestId(int destId) {
        this.destId = Integer.toString(destId);
    }

    public int getInfo() {
        return Integer.valueOf(info);
    }

    public void setInfo(int info) {
        this.info = Integer.toString(info);
    }


    public int getInfo1() {
        return Integer.valueOf(info1);
    }

    public void setInfo1(int info) {
        this.info1 = Integer.toString(info);
    }


    public FileContainer getFile() {
        return file;
    }

    public void setFile(String filePath) {
        this.file = new FileContainer(filePath);
    }

    // -------------- extra getters & setters ---------------
    public String getEmail(){
        return message;
    }
    public void setEmail(String message) {
        this.message = message;
    }
    public String getPassword() { return destId; }
    public void setPassword(String message) {
        this.destId = message;
    }
    public void setSubCmd(String cmd){
        //for managing groups
        this.message = cmd;
    }
    public String getSubCmd(){
        return message;
    }
    public int getExecId(){
        //for managing groups
        return Integer.valueOf(userId);
    }
    public void setExecId(int execId){
        this.userId = Integer.toString(execId);
    }
    public int getDestUserId(){
        //for managing groups
        return Integer.valueOf(info);
    }
    public void setDestUserId(int userId){
        this.info = Integer.toString(userId);
    }
    public String getName(){
        return message;
    }
    public String getBio(){
        return info;
    }

    @Override
    public String toString() {
        return message;
    }
}
