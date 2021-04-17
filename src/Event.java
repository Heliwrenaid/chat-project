public class Event {
    private int blocker = 0;
    private String actionName = null;
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
    public void triggerAction(String actionName){
        this.actionName = actionName;
    }
    public boolean isActionTriggered(String actionType){
       if (actionName == null) return false;
       if (!actionName.equals(actionType)) return false;
       actionName = null;
       return true;
    }
}
