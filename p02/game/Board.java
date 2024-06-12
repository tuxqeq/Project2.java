package p02.game;

import p02.pres.ScoreCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Board extends JPanel implements KeyListener, TickEventListener, StartEvent.StartEventListener, PlusOneEvent.PlusOneEventListener, ResetEvent.ResetEventListener {
    private static final int boardSize = 3;
    private int[] board;
    private Random random;
    private ScoreCounter scoreCounter;
    private  boolean canMove;
    private int tickCounter;

    public Board(ScoreCounter scoreCounter) {
        this.board = new int[7];
        this.scoreCounter = scoreCounter;
        this.random = new Random();
        this.tickCounter = -1;
        setPreferredSize(new Dimension(400, 400));
        setLayout(new GridLayout(3, 3));
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(board[0] * 50, getHeight() - 50, 50, 50);
        for (int i = 1; i < 7; i++) {
            if (board[i] > 0 && board[i] < 4) {
                g.setColor(Color.RED);
                g.fillRect((board[i] - 1) * 50, getHeight() - (i+1) * 50, 50, 50);
            }if (board[i] > 3) {
                for (int j = 0; j < 3; j++) {
                    if(j != board[i] - 4) {
                        g.setColor(Color.RED);
                        g.fillRect(j * 50, getHeight() - (i+1) * 50, 50, 50);
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            moveLeft();
        } else if (key == KeyEvent.VK_D) {
            moveRight();
        } else if (key == KeyEvent.VK_S) {
            GameTimer.getInstance().startTimer();
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
        repaint();
    }

    private void moveRight() {
        if (board[0] < boardSize - 1 && canMove) {
            board[0]++;
            canMove = false;
        }
        repaint();
    }

    private void generateObstacles() {
        //moving forward
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
        //generating new
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
        /*System.out.println(board[6]);*/
    }

    @Override
    public void tickEventOccurred(TickEvent event) {
        canMove = true;
        if(scoreCounter.isMaxScoreReached()){
            GameTimer.getInstance().resetTimer();
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
        repaint();
    }

    public void generateBlank(){
        for (int i = 1; i < 6; i++) {
            board[i] = board[i + 1];
        }
        board[6] = 0;
        /*System.out.println(board[6]);*/
    }

    @Override
    public void startEventOccurred(StartEvent event) {
        GameTimer.getInstance().resetTimer();
    }

    @Override
    public void plusOneEventOccurred(PlusOneEvent event) {
        GameTimer.getInstance().stopTimer();
    }

    @Override
    public void resetEventOccurred(ResetEvent event) {
        GameTimer.getInstance().resetTimer();
    }
}
