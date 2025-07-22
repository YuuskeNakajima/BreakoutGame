package breakout.ui;

import javax.swing.*;
import java.awt.*;

public class SpeedUpNotice {
    private JLabel label;

    public SpeedUpNotice(JPanel parentPanel) {
        label = new JLabel("スピードアップ！", SwingConstants.CENTER);
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBounds(100, 10, 200, 30);
        label.setVisible(false);
        parentPanel.add(label);
    }

    public void show() {
        label.setVisible(true);
        Timer timer = new Timer(1000, e -> label.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
}





