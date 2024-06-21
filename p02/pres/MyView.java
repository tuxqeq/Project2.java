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
