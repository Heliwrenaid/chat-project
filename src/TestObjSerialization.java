import java.io.File;

public class TestObjSerialization {
    public static void main(String[] args){
        int loops = 10000;
        DataBase db = new DataBase(System.getProperty("user.home") + File.separator + "TestObjSerialization");
        db.createUser("jan","null",null,null) ;
        long start = System.currentTimeMillis();
        for (int i=1;i<loops;i++ ){
            db = DataBase.loadData(System.getProperty("user.home") + File.separator + "TestObjSerialization\\db");
            db.save();
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);


    }
}
