package breakout;

import javax.swing.*;
import java.awt.*;

public class BreakoutGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Breakout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        GamePanel gamePanel = new GamePanel(cardLayout, mainPanel);
        TitlePanel titlePanel = new TitlePanel(cardLayout, mainPanel);
        ClearPanel clearPanel = new ClearPanel(cardLayout, mainPanel, gamePanel);

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

        // ゲームクリアパネルを追加
        mainPanel.add(clearPanel, "Clear");

        // スタートボタンにフォーカスを与える
        SwingUtilities.invokeLater(() -> {
            titlePanel.requestSliderFocus(); // ここでスライダーに明示的にフォーカスを渡す
        });
    }
}
