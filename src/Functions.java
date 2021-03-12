import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Functions {
    static void save(Object object, String filepath){
        System.out.print("Saving database ... ");
        try {
            FileOutputStream file = new FileOutputStream(filepath);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(object);
            output.close();
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("In Functions.save() error occurred: "+ e.getMessage());
        }
    }

    static int nextId(ArrayList<Integer> arr){
        if (arr.size() == 0) return 1;
        if (!arr.contains(1)) return 1;
        for(int i =0;i<arr.size()-1;i++){
            if(arr.get(i) != arr.get(i+1)-1){
                return arr.get(i)+1;
            }
        }
        return arr.get(arr.size()-1)+1;
    }
}
