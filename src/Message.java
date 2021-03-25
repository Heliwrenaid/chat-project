import java.io.Serializable;

public class Message implements Serializable {
    private String cmd;
    private String message;
    private String userId;
    private String destId;
    private String info;
   // private String info2;

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
        System.out.println("---------------------------");
    }
    public boolean isValid(){
        if(userId == null) return false;
        if(destId == null) return false;
        if(cmd == null) return false;
        if(message == null) return false;
        return true;
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
        return Integer.valueOf(userId);
    }

    public void setUserId(int userId) {
        this.userId = Integer.toString(userId);
    }

    public int getDestId() {
       return Integer.valueOf(destId);
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

    // -------------- extra getters & setters ---------------
    public String getEmail(){
        return message;
    }
    public void setEmail(String message) {
        this.message = message;
    }
    public String getPassword() {
        return userId;
    }
    public void setPassword(String userId) {
        this.userId = userId;
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
}
