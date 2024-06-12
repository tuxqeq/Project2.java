package p02.pres;

import p02.game.TickEvent;
import p02.game.TickEventListener;

import javax.swing.*;
import java.awt.*;

public class ScoreCounter extends JPanel implements TickEventListener {
    private SevenSegmentDigit hundreds;
    private SevenSegmentDigit tens;
    private SevenSegmentDigit ones;

    public ScoreCounter() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        Dimension digitSize = new Dimension(25, 50);
        hundreds = new SevenSegmentDigit(digitSize);
        tens = new SevenSegmentDigit(digitSize);
        ones = new SevenSegmentDigit(digitSize);
        ones.setNextDigit(tens);
        tens.setNextDigit(hundreds);
        this.add(hundreds);
        this.add(tens);
        this.add(ones);
    }

    @Override
    public void tickEventOccurred(TickEvent event) {
        ones.increment();
    }

    public int countForObstacles() {
        int count = 0;
        if (hundreds.getValue() == 0) {
            count++;
            if (tens.getValue() == 0) {
                count++;
                if (ones.getValue() == 0) count++;
            }
        }
        return count;
    }

    public boolean isMaxScoreReached() {
        return hundreds.getValue() == 9 && tens.getValue() == 9 && ones.getValue() == 9;
    }
}
