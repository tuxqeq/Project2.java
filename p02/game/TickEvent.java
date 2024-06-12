package p02.game;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/*public class TickEvent extends EventObject {
    public TickEvent(Object source) {
        super(source);
    }
}*/

public class TickEvent extends EventObject {

    public TickEvent(Object source) {
        super(source);
    }

    public interface TickEventListener {
        void tickEventOccurred(TickEvent event);
    }

    private List<TickEventListener> listeners = new ArrayList<>();

    public void addTickEventListener(TickEventListener listener) {
        listeners.add(listener);
    }

    public void removeTickEventListener(TickEventListener listener) {
        listeners.remove(listener);
    }

//    public void fireTickEvent() {
//        for (TickEventListener listener : listeners)
//            listener.tickEventOccurred();
//    }

}