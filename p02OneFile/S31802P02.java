package p02OneFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class S31802P02 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Autoslalom Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Image backgroundImage = new ImageIcon("./assets/board.png").getImage();
        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImage);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));

        backgroundPanel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        ScoreCounter scoreCounter = new ScoreCounter();
        scoreCounter.setBounds(100, 50, 100, 75);
        layeredPane.add(scoreCounter, JLayeredPane.MODAL_LAYER);

        Board board = new Board(scoreCounter);
        board.setBounds(500, -50, 1, 1);
        layeredPane.add(board, JLayeredPane.MODAL_LAYER);

        GamePanel gamePanel = new GamePanel(board);
        gamePanel.setBounds(0, 0, 800, 600);
        layeredPane.add(gamePanel, JLayeredPane.MODAL_LAYER);

        frame.add(layeredPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);

        frame.addKeyListener(board);
    }
}
class Board extends JPanel implements KeyListener {
    private static final int boardSize = 3;
    private int[] board;
    private Random random;
    private ScoreCounter scoreCounter;
    private  boolean canMove;
    private int tickCounter;
    private  boolean running = false;
    private int forCollision = 0;
    private int forIncrement = 0;
    private TickEvent.TickEventListener tickEvent = this::tickEvent;


    public Board(ScoreCounter scoreCounter) {
        this.board = new int[7];
        board[0] = 1;
        this.scoreCounter = scoreCounter;
        this.random = new Random();
        this.tickCounter = -1;
        setOpaque(false);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new FlowLayout());
        addKeyListener(this);
        setFocusable(true);
        TickEvent.addTickEventListener(tickEvent);
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            moveLeft();
        } else if (key == KeyEvent.VK_D) {
            moveRight();
        } else if (key == KeyEvent.VK_S && !running) {
            new StartEvent().notifyStartEventListeners();
            start();
            running = true;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    private void moveLeft() {
        if (board[0] > 0 && canMove) {
            board[0]--;
            canMove = false;
        }
    }

    private void moveRight() {
        if (board[0] < boardSize - 1 && canMove) {
            board[0]++;
            canMove = false;
        }
    }


    private void generateObstacles() {
        for (int i = 1; i < 6; i++) {
            board[i] = board[i + 1];
        }
        int previous = 0;
        for (int i = 6; i > 0; i--) {
            if(board[i] != 0) {
                previous = board[i];
                break;
            }
        }
        if(previous <= 3 && previous > 0) {
            for (int j = 1; j < 4; j++) {
                if(j != previous && random.nextBoolean()) {
                    board[6] = j;
                    break;
                }else{
                    board[6] = previous + 3;
                }
            }

        }else if (previous > 3) {
            board[6] = previous - 3;
        }else{
            board[6] = random.nextInt(6) + 1;
        }
    }


    public void tickEvent() {
        forIncrement = board[1];
        forCollision = board[1];
        canMove = true;
        if(scoreCounter.isMaxScoreReached()){
            new ResetEvent().notifyResetEvent();
            running = false;
        }
        tickCounter++;
        if (tickCounter == 9) tickCounter++;
        int zeroCount = scoreCounter.countForObstacles();
        int frequency = zeroCount == 0 ? 1 : zeroCount + 1;
        if (tickCounter % frequency == 0) {
            generateObstacles();
        }else{
            generateBlank();
        }
        checkCollision();
        increment();
    }

    private void increment(){
        if(forIncrement != 0){
            new PlusOneEvent().notifyPlusOneListeners();
            forIncrement = 0;
        }

    }

    private void checkCollision() {
        if (((1 << board[0]) & getObstacleBits(forCollision)) != 0) {
            new ResetEvent().notifyResetEvent();
            GameTimer.getInstance().resetTimer();
            running = false;
            forCollision = 0;
        }
    }

    private int getObstacleBits(int obstacle) {
        return switch (obstacle) {
            case 1 -> 0b001; // single obstacle in the right column
            case 2 -> 0b010; // single obstacle in the middle column
            case 3 -> 0b100; // single obstacle in the left column
            case 4 -> 0b110; // two obstacles in the middle and right columns
            case 5 -> 0b101; // two obstacles in the left and right columns
            case 6 -> 0b011; // two obstacles in the left and middle columns
            default -> 0b000; // no obstacles
        };
    }

    public void generateBlank(){
        for (int i = 1; i < 6; i++) {
            board[i] = board[i + 1];
        }
        board[6] = 0;
    }

    public void start(){
        board = new int[7];
        board[0] = 1;
        tickCounter = -1;
    }

    public int[] getArr(){
        return board;
    }

}
class GameTimer extends Thread implements Runnable {
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

    public void stopTimer() {
        running = false;
        thread = null;
    }

    public void resetTimer() {
        stopTimer();
        interval = gameinterval;
    }

}

class PlusOneEvent {
    public PlusOneEvent() {}

    public interface PlusOneEventListener {
        void plusOneEventOccurred();
    }

    private static List<PlusOneEvent.PlusOneEventListener> listeners = new ArrayList<>();

    public static void addPlusOneEventListener(PlusOneEvent.PlusOneEventListener listener) {listeners.add(listener);}

    public void notifyPlusOneListeners() {
        for(PlusOneEvent.PlusOneEventListener l : listeners){
            l.plusOneEventOccurred();
        }
    }

}
class ResetEvent {
    public ResetEvent() {
        GameTimer.getInstance().resetTimer();
    }
    public interface ResetEventListener {
        void resetEventOccurred();
    }

    private static List<ResetEvent.ResetEventListener> listeners = new ArrayList<>();

    public static void addResetEventListener(ResetEvent.ResetEventListener listener) {
        listeners.add(listener);
    }

    public void notifyResetEvent() {
        for (ResetEvent.ResetEventListener l : listeners)
            l.resetEventOccurred();
    }

}
class StartEvent{
    public StartEvent() {
        GameTimer.getInstance().startTimer();
    }

    public interface StartEventListener {
        void startEventOccurred();
    }

    private static List<StartEvent.StartEventListener> listeners = new ArrayList<>();

    public static void addStartEventListener(StartEvent.StartEventListener listener) {
        listeners.add(listener);
    }

    public void notifyStartEventListeners() {
        for (StartEvent.StartEventListener listener : listeners) {
            listener.startEventOccurred();
        }
    }
}
class TickEvent {

    public TickEvent() {}

    public interface TickEventListener {
        void tickEventOccurred();
    }

    private static List<TickEvent.TickEventListener> listeners = new ArrayList<>();

    public static void addTickEventListener(TickEvent.TickEventListener listener) {
        listeners.add(listener);
    }

    public void notifyTickEventListeners() {
        for (TickEvent.TickEventListener listener : listeners)
            listener.tickEventOccurred();
    }

}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}

class GamePanel extends JPanel {
    private TickEvent.TickEventListener tickEventListener = this::tickEvent;
    private ScoreCounter scoreCounter;
    private JTable jTable;
    private MyData data;
    private Board board;

    public GamePanel(Board board) {
        data = new MyData();
        this.board = board;

        setLayout(new BorderLayout());
        setOpaque(false);

        jTable = new JTable();
        jTable.setOpaque(false);
        jTable.setModel(data);
        jTable.setDefaultRenderer(Integer.class, new MyView());
        jTable.setRowMargin(0);
        jTable.setRowHeight(80);
        jTable.setPreferredSize(new Dimension(800, 600));
        add(jTable);
        jTable.setGridColor(new Color(255, 0, 0, 0));
        setPreferredSize(new Dimension(800, 600));


        TickEvent.addTickEventListener(tickEventListener);
    }


    public void tickEvent(){
        data.setData(board.getArr());
        jTable.setModel(data);
        repaint();
    }
}

class MyData extends AbstractTableModel {
    private static int rowCount;

    private int[] data = new int[7];


    public void setData(int[] data) {
        rowCount++;
        this.data = data;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex >= 0)
            return Integer.class;
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[6-rowIndex];
    }

    @Override
    public int getRowCount() {
        return 7;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }


    public static int getCount(){
        return rowCount;
    }
}
class MyView extends JPanel implements TableCellRenderer {
    private Image img;
    private Image car;
    private Image right;
    private Image left;
    static double[][] forRows = {
            {21.8,22.5,23.3}, //first row
            {18.7,20.5,22.2}, //second
            {15.6, 18.1, 20.8}, //third
            {12.4, 15.9, 19.4}, //fourth
            {9.5, 13.9, 18}, //fifth
            {6.5, 11.9, 16.9}, // sixth
            {4, 10, 16} // car
    };
    int row = 0;
    int value = 1;


    public MyView() {
        setOpaque(false);
        try {
            img = ImageIO.read(new File("./assets/obstacle.png"));
            car = ImageIO.read(new File("./assets/car.png"));
            right = ImageIO.read(new File("./assets/right.png"));
            left = ImageIO.read(new File("./assets/left.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.value = (int)value;
        this.row = row;
        return this;
    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        if(MyData.getCount()%2 == row%2 && row != 6 && MyData.getCount() != 0) {
            g.drawImage(left, (int) (forRows[row][0] * 32) - 40 * (row + 1)/2, row == 0 ? 10 : 0, 38 * (row + 1) / 2, 29 * (row + 1) / 2, null);
            g.drawImage(right, (int) (forRows[row][2] * 32) + 40 * (row+1)/2, row == 0 ? 10 : 0, 15 * (row + 1) / 2, 20 * (row + 1) / 2, null);
        }
        if(row != 6){
            if (value > 0 && value < 4){
                g.drawImage(img, (int)(forRows[row][value-1] * 32), row == 0 ? 10 : 0, 45 * (row + 1) / 2, 15 * (row + 1) / 2,null);
            }
            if (value > 3) {
                for (int j = 0; j < 3; j++) {
                    if (j != value - 4) {
                        g.drawImage(img, (int)(forRows[row][j] * 32), row == 0 ? 10 : 0, 45 * (row + 1) / 2, 15 * (row + 1) / 2,null);
                    }
                }
            }

        }else if(row == 6){
            g.drawImage(car, (int)(forRows[row][value] * 32) , 0, 100, 75, this);
        }
    }

}

class ScoreCounter extends JPanel {
    private SevenSegmentDigit hundreds;
    private SevenSegmentDigit tens;
    private SevenSegmentDigit ones;

    public ScoreCounter() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        Dimension digitSize = new Dimension(25, 50);
        hundreds = new SevenSegmentDigit(digitSize, null);
        tens = new SevenSegmentDigit(digitSize, hundreds.getListener());
        ones = new SevenSegmentDigit(digitSize, tens.getListener());
        add(hundreds);
        add(tens);
        add(ones);
        setOpaque(false);
        ResetEvent.ResetEventListener resetEventListener = this::reset;
        ResetEvent.addResetEventListener(resetEventListener);
        StartEvent.StartEventListener startEventListener = this::start;
        StartEvent.addStartEventListener(startEventListener);
        PlusOneEvent.PlusOneEventListener plusOneEventListener = () -> {
            ones.getListener().plusOneEventOccurred();
        };
        PlusOneEvent.addPlusOneEventListener(plusOneEventListener);
    }



    public int countForObstacles() {
        int count = 0;
        if (hundreds.getValue() == 0) {
            count++;
            if (tens.getValue() == 0) {
                count++;
            }
        }
        return count;
    }

    public boolean isMaxScoreReached() {
        return hundreds.getValue() == 9 && tens.getValue() == 9 && ones.getValue() == 9;
    }

    public void reset() {
        this.setVisible(false);
    }

    public void start(){
        this.setVisible(true);
        ones.setValue(0);
        tens.setValue(0);
        hundreds.setValue(0);
    }
}

class SevenSegmentDigit extends JPanel {
    private int value;
    private PlusOneEvent.PlusOneEventListener plusOneEventListener = this::increment;
    private PlusOneEvent.PlusOneEventListener next;

    public SevenSegmentDigit(Dimension size, PlusOneEvent.PlusOneEventListener next) {
        this.value = 0;
        setOpaque(false);
        this.setPreferredSize(size);
        this.next = next;
    }

    public PlusOneEvent.PlusOneEventListener getListener(){
        return this.plusOneEventListener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDigit(g, value);
    }

    private void drawDigit(Graphics g, int value) {
        boolean[][] segments = {
                {true, true, true, true, true, true, false},   // 0
                {false, true, true, false, false, false, false}, // 1
                {true, true, false, true, true, false, true},   // 2
                {true, true, true, true, false, false, true},   // 3
                {false, true, true, false, false, true, true},  // 4
                {true, false, true, true, false, true, true},   // 5
                {true, false, true, true, true, true, true},    // 6
                {true, true, true, false, false, false, false}, // 7
                {true, true, true, true, true, true, true},     // 8
                {true, true, true, true, false, true, true}     // 9
        };

        int[][] positions = {
                {5, 5, 15, 5}, // top
                {20, 10, 5, 15}, // top right
                {20, 30, 5, 15}, // bottom right
                {5, 45, 15, 5}, // bottom
                {0, 30, 5, 15},  // bottom left
                {0, 10, 5, 15},  // top left
                {5, 25, 15, 5}  // middle
        };

        g.setColor(Color.RED);
        for (int i = 0; i < segments[value].length; i++) {
            if (segments[value][i]) {
                int[] pos = positions[i];
                g.fillRect(pos[0], pos[1], pos[2], pos[3]);
            }
        }
    }

    public void increment() {
        if (value == 9) {
            value = 0;
            next.plusOneEventOccurred();
        } else {
            value++;
        }
        repaint();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }
}


