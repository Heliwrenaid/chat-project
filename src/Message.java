import java.io.Serializable;

public class Message implements Serializable {
    private int data;
    Message(int data){
        this.data = data;
    }
    int getData(){
        return data;
    }
}
