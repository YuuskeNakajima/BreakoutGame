package breakout;

import javax.swing.*;
import java.awt.*;

public class BreakoutGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        ClearPanel clearPanel = new ClearPanel(cardLayout, mainPanel, null);
        GamePanel gamePanel = new GamePanel(cardLayout, mainPanel, clearPanel);
        clearPanel.setGamePanel(gamePanel);
        TitlePanel titlePanel = new TitlePanel(cardLayout, mainPanel);

        mainPanel.add(titlePanel, "Title");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(clearPanel, "Clear");

        frame.setResizable(false);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(titlePanel.getStartButton());
       
        cardLayout.show(mainPanel, "Title");

        // スタートボタンにフォーカスを与える
        SwingUtilities.invokeLater(() -> {
            titlePanel.requestSliderFocus(); // ここでスライダーに明示的にフォーカスを渡す
        });
    }
}
