package p02.game;

import p02.pres.ScoreCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener{
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
