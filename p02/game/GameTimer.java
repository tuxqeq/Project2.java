package p02.game;

import java.util.ArrayList;
import java.util.List;

public class GameTimer extends Thread implements Runnable {
    private static GameTimer instance;
    private List<TickEventListener> listeners;
    private Thread thread;
    private boolean running;
    private int interval;
    private static final int INITIAL_INTERVAL = 1000; // Initial interval in milliseconds

    private GameTimer() {
        this.listeners = new ArrayList<>();
        this.running = false;
        this.interval = INITIAL_INTERVAL;
    }

    public static synchronized GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

    public void addTickEventListener(TickEventListener listener) {
        listeners.add(listener);
    }

    public void removeTickEventListener(TickEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyTickEvent() {
        TickEvent event = new TickEvent(this);
        for (TickEventListener listener : listeners) {
            listener.tickEventOccurred(event);
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(interval);
                notifyTickEvent();
                decreaseInterval();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void decreaseInterval() {
        if (interval > 500) { // Minimum interval to prevent excessive speed
            interval -= 5; // Decrease interval to simulate acceleration
        }
    }

    public void startTimer() {
        if (!running) {
            interval = INITIAL_INTERVAL;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopTimer() {
        running = false;
        thread = null;
    }

    public void resetTimer() {
        stopTimer();
        interval = INITIAL_INTERVAL;
    }
}