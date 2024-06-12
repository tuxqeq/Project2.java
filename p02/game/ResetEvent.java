package p02.game;

import java.util.EventObject;

public class ResetEvent extends EventObject {
    public ResetEvent(Object source) {
        super(source);
    }
    public interface ResetEventListener {

        void resetEventOccurred(ResetEvent event);
    }

}
