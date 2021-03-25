import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Te {
    public static void main(String[] args){

        Chat chat = new User("a","dfdf","sdsd","adad",null);
        Object obj = chat;
        Functions.save(chat,"C:\\Users\\Dell\\ServerData\\ee");
        String filename = "C:\\Users\\Dell\\ServerData\\ee";
        Chat lo;
            if (Files.exists(Paths.get(filename))) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(filename);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                   lo = (Chat)objectInputStream.readObject();
                    objectInputStream.close();
                    System.out.println(lo.getClass().getName());
                } catch (Exception e) {
                    System.out.println("Error in DataBase.loadDataBase(): " + e.getMessage());

                }
            }




    }
}
