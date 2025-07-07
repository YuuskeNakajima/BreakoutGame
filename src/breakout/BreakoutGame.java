package breakout;

import javax.swing.*;
import java.awt.*;

public class BreakoutGame{
    public static void main(String[] args) { 
        JFrame frame = new JFrame("Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel gamePanel = new GamePanel();
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        TitlePanel titlePanel = new TitlePanel(cardLayout, mainPanel);

        mainPanel.add(titlePanel, "Title");
        mainPanel.add(gamePanel, "Game");

        frame.setResizable(false);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(titlePanel.getStartButton());

        // 最初にタイトル画面を表示
        cardLayout.show(mainPanel, "Title");

        // スタートボタンにフォーカスを与える
        SwingUtilities.invokeLater(() -> {
        titlePanel.getStartButton().requestFocusInWindow();
    });
    }
}

