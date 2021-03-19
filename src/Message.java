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
    public void print(){
        System.out.println("-------Print Message-------");
        System.out.println("cmd: " + cmd);
        System.out.println("message: " + message);
        System.out.println("userId: " + userId);
        System.out.println("destId: " + destId);
        System.out.println("---------------------------");
    }
}
