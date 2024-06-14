import p02.game.Board;
import p02.game.GameTimer;
import p02.game.ResetEvent;
import p02.pres.ScoreCounter;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set up the JFrame
        JFrame frame = new JFrame("Autoslalom Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the score counter
        ScoreCounter scoreCounter = new ScoreCounter();
        frame.add(scoreCounter, BorderLayout.NORTH);

        // Set up the game board
        Board board = new Board(scoreCounter);
        frame.add(board, BorderLayout.CENTER);


        // Set up the game timer (singleton)
        //GameTimer gameTimer = GameTimer.getInstance();
        /*gameTimer.addTickEventListener(scoreCounter);*/
        //gameTimer.addTickEventListener(board);
        //ResetEvent.addResetEventListener(scoreCounter);

        // Final frame adjustments
        frame.setSize(400, 600);
        frame.setVisible(true);

        // Add the KeyListener to the frame
        frame.addKeyListener(board);
    }
}
