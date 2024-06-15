package p02.pres;
import p02.game.StartEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public
class MyView extends JPanel implements TableCellRenderer {
    /*private static int sidelines = 0;*/
    //private StartEvent.StartEventListener startEventListener = this::start;
    private Image img;
    private Image car;
    private Image right;
    private Image left;
    //private MyPanel jp;
    static double[][] forRows = {
            {21.5,22.5,23.5}, //first row
            {18.7,20.5,22.2}, //second
            {15.6, 18.1, 20.8}, //third
            {12.4, 15.9, 19.4},
            {9.5, 13.9, 18},
            {6.5, 11.9, 16.9},
            {4, 10, 16}
    };
    int row = 0;
    int value = 1;


    public MyView() {
        //this.jp = new MyPanel();
        setOpaque(false);
        //StartEvent.addStartEventListener(startEventListener);
        //this.add(jp);
        try {
            img = ImageIO.read(new File("./assets/obstacle12.png"));
            car = ImageIO.read(new File("./assets/caroff.png"));
            /*right = ImageIO.read(new File("./assets/right.png"));
            left = ImageIO.read(new File("./assets/left.png"));*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        /*jp.setVal(new Pair(row, (int)value));*/
        //System.out.println(";");
        this.value = (int)value;
        this.row = row;
        return this;
    }
    public void paintComponent(Graphics g){
        /*sidelines++;*/
        super.paintComponent(g);

        /*for (double i : forRows[row]){
            g.drawImage(img, (int)(i * 32), row == 0 ? 10 : 0, 45 * (row + 1) / 2, 15 * (row + 1) /2,null);
        }*/
        /*if(sidelines%2 == 0) {
            g.drawImage(left, (int) (forRows[row][0] * 32) - 23 * row, row == 0 ? 10 : 0, 30 * (row + 1) / 2, 25 * (row + 1) / 2, null);
            g.drawImage(right, (int) (forRows[row][2] * 32) + 20 * row, row == 0 ? 10 : 0, 15 * (row + 1) / 2, 20 * (row + 1) / 2, null);
        }*/
        if(row != 6){
            if (value > 0 && value < 4){
                g.drawImage(img, (int)(forRows[row][value-1] * 32), row == 0 ? 10 : 0, 45 * (row + 1) / 2, 15 * (row + 1) / 2,null);
            }
            if (value > 3) {
                for (int j = 0; j < 3; j++) {
                    if (j != value - 4) {
                        g.drawImage(img, (int)(forRows[row][j] * 32), 0, 45 * (row + 1) / 2, 15 * (row + 1) / 2,null);
                    }
                }
            }

        }else if(row == 6){
            g.drawImage(car, (int)(forRows[row][value] * 32) , 0, 100, 75, this);
        }
    }

    /*public void start(){
        repaint();
    }*/
}
