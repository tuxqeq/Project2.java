import p02.game.Board;
import p02.game.GameTimer;
import p02.game.ResetEvent;
import p02.pres.BackgroundPanel;
import p02.pres.GamePanel;
import p02.pres.ScoreCounter;

import javax.swing.*;
import java.awt.*;

public class Main {
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
        scoreCounter.setBounds(25, 25, 100, 75);
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
