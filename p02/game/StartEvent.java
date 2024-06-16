package p02.game;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class StartEvent{
    public StartEvent() {
        GameTimer.getInstance().startTimer();
    }

    public interface StartEventListener {
        void startEventOccurred();
    }

    private static List<StartEventListener> listeners = new ArrayList<>();

    public static void addStartEventListener(StartEventListener listener) {
        listeners.add(listener);
    }

    public void notifyStartEventListeners() {
        for (StartEventListener listener : listeners) {
            listener.startEventOccurred();
        }
    }


}
