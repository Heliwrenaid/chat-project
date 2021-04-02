import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Te {
    public static void main(String[] args){

        Message chat = new Message();
        chat.setUserData("name","sfjskjff","adjkjadadjahejgadhajgahdagdjajdadg","src\\Icons\\tiger.jpg");

        FileContainer fileContainer = new FileContainer("src\\Icons\\tiger.jpg");
        Functions.save(chat,"C:\\Users\\Dell\\ServerData\\tt");

        String filename = "C:\\Users\\Dell\\ServerData\\ee";
        Message lo;
            if (Files.exists(Paths.get(filename))) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(filename);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                   lo = (Message)objectInputStream.readObject();
                    objectInputStream.close();
                    System.out.println(lo.getMessage());
                } catch (Exception e) {
                    System.out.println("Error in DataBase.loadDataBase(): " + e.getMessage());

                }
            }




    }
}
