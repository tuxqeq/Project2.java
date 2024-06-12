package p02.game;

import java.util.EventObject;

public class PlusOneEvent extends EventObject {
    public PlusOneEvent(Object source) {
        super(source);
    }

    public interface PlusOneEventListener {
        void plusOneEventOccurred(PlusOneEvent event);
    }

}