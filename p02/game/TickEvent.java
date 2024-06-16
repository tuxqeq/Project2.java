package p02.game;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class TickEvent {

    public TickEvent() {

    }

    public interface TickEventListener {
        void tickEventOccurred();
    }

    private static List<TickEventListener> listeners = new ArrayList<>();

    public static void addTickEventListener(TickEventListener listener) {
        listeners.add(listener);
    }

    public void removeTickEventListener(TickEventListener listener) {
        listeners.remove(listener);
    }

    public void notifyTickEventListeners() {
        for (TickEventListener listener : listeners)
            listener.tickEventOccurred();
    }

}