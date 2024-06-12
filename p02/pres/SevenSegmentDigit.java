package p02.pres;

import p02.game.PlusOneEvent;
import p02.game.SevenSegmentDigitListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SevenSegmentDigit extends JPanel {
    private int value;
    private List<SevenSegmentDigitListener> listeners;
    private SevenSegmentDigit nextDigit;

    public SevenSegmentDigit(Dimension size) {
        this.value = 0;
        this.listeners = new ArrayList<>();
        this.setPreferredSize(size);
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
                {5, 5, 15, 5}, // Top
                {20, 10, 5, 15}, // Top right
                {20, 30, 5, 15}, // Bottom right
                {5, 45, 15, 5}, // Bottom
                {0, 30, 5, 15},  // Bottom left
                {0, 10, 5, 15},  // Top left
                {5, 25, 15, 5}  // Middle
        };

        g.setColor(Color.RED);
        for (int i = 0; i < segments[value].length; i++) {
            if (segments[value][i]) {
                int[] pos = positions[i];
                g.fillRect(pos[0], pos[1], pos[2], pos[3]);
            }
        }
    }

    public void setNextDigit(SevenSegmentDigit nextDigit) {
        this.nextDigit = nextDigit;
    }

    public void increment() {
        if (value == 9) {
            value = 0;
            notifyPlusOneEvent();
        } else {
            value++;
        }
        repaint();
    }

    public void addSevenSegmentDigitListener(SevenSegmentDigitListener listener) {
        listeners.add(listener);
    }

    public void removeSevenSegmentDigitListener(SevenSegmentDigitListener listener) {
        listeners.remove(listener);
    }

    private void notifyPlusOneEvent() {
        PlusOneEvent event = new PlusOneEvent(this);
        for (SevenSegmentDigitListener listener : listeners) {
            listener.plusOneEventOccurred(event);
        }
        if (nextDigit != null) {
            nextDigit.increment();
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        repaint();
    }
}
