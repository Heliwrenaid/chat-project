import java.io.Serializable;

public class Message implements Serializable {
    private String cmd;
    private String message;
    private String userId;
    private String destId;

    public Message(String cmd, String message, String userId, String destId) {
        this.cmd = cmd;
        this.message = message;
        this.userId = userId;
        this.destId = destId;
    }
    public Message(){}

    public void print(){
        System.out.println("-------Print Message-------");
        System.out.println("cmd: " + cmd);
        System.out.println("message: " + message);
        System.out.println("userId: " + userId);
        System.out.println("destId: " + destId);
        System.out.println("---------------------------");
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
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
}
