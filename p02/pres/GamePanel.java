package p02.pres;

import p02.game.Board;
import p02.game.StartEvent;
import p02.game.TickEvent;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
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
