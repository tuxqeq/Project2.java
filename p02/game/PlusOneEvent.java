package p02.game;

import java.util.ArrayList;
import java.util.List;

public class PlusOneEvent {
    public PlusOneEvent() {

    }

    public interface PlusOneEventListener {
        void plusOneEventOccurred();
    }

    private static List<PlusOneEventListener> listeners = new ArrayList<>();

    public static void addPlusOneEventListener(PlusOneEventListener listener) {listeners.add(listener);}

    public void notifyPlusOneListeners() {
        for(PlusOneEventListener l : listeners){
            l.plusOneEventOccurred();
        }
    }

}