package p02.game;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ResetEvent {
    public ResetEvent() {
        GameTimer.getInstance().resetTimer();
    }
    public interface ResetEventListener {

        void resetEventOccurred();
    }

    private static List<ResetEventListener> listeners = new ArrayList<>();

    public static void addResetEventListener(ResetEventListener listener) {
        listeners.add(listener);
    }

    public void notifyResetEvent() {
        for (ResetEventListener l : listeners)
            l.resetEventOccurred();
    }

}
