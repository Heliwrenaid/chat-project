public class Event {
    private int blocker = 0;

    public void block(){
        //TODO: set timeout
        while(true){
            if(blocker > 0) break;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        blocker = 0;
    }
    public void unblock(){
        blocker += 1;
    }
}
