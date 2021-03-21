import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Functions {
    static void save(Object object, String filepath){
        System.out.print("Saving "+ object.getClass().getName() + " to " + filepath + " ...");
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
// 1 2 3 4 6 7 8
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
    static int freeId(HashMap<Integer,String> map){
        if (map.size() == 0) return 1;
        if (!map.containsKey(1)) return 1;
       int min = 1;
       int max = Collections.max(map.keySet());
        for(int i = min; i <= max; i++){
            if(!map.containsKey(i)){
                return i;
            }
        }
        return max+1;
    }

    static Object getObject(String filePath){
        FileInputStream fileStream = null;
        Object object = null;

        try {
            fileStream = new FileInputStream(filePath);
            ObjectInputStream objStream = new ObjectInputStream(fileStream);
            object = (Object) objStream.readObject();
            objStream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return object;
    }

}
