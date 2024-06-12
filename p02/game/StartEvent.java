package p02.game;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class StartEvent extends EventObject {
    public StartEvent(Object source) {
        super(source);
    }

    public interface StartEventListener {
        void startEventOccurred();
    }

    private List<StartEventListener> listeners = new ArrayList<>();

    public void addCustomEventListener(StartEventListener listener) {
        listeners.add(listener);
    }

    public void removeCustomEventListener(StartEventListener listener) {
        listeners.remove(listener);
    }

    public void fireCustomEvent() {
        for (StartEventListener listener : listeners) {
            listener.startEventOccurred();
        }
    }


}
