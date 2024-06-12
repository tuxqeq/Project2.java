package p02.game;

import java.util.EventObject;

public class StartEvent extends EventObject {
    public StartEvent(Object source) {
    super(source);
    }
    public interface StartEventListener {
        void startEventOccurred(StartEvent event);
    }

}
