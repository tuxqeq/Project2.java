package p02.pres;

import p02.game.TickEvent;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private TickEvent.TickEventListener tickEventListener = this::tickEvent;
    private ScoreCounter scoreCounter;

    public GamePanel(ScoreCounter scoreCounter) {
        this.scoreCounter = scoreCounter;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(500, 500));
        TickEvent.addTickEventListener(tickEventListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void tickEvent(){

    }
}
