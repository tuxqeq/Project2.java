package p02.game;

public class GameTimer extends Thread implements Runnable {
    private static GameTimer instance;
    private Thread thread;
    private boolean running;
    private int interval;
    private static final int gameinterval = 1000;

    private GameTimer() {
        this.running = false;
        this.interval = gameinterval;
    }

    public static synchronized GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(interval);
                new TickEvent().notifyTickEventListeners();
                decreaseInterval();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void decreaseInterval() {
        if (interval > 500) {
            interval -= 5;
        }
    }

    public void startTimer() {
        if (!running) {
            interval = gameinterval;
            thread = new Thread(this);
            thread.start();
        }
    }
    public void resetTimer() {
        running = false;
        thread = null;
        interval = gameinterval;
    }
}