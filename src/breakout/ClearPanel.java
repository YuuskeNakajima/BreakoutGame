package breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ClearPanel extends JPanel {
    public ClearPanel(CardLayout layout, JPanel parent, GamePanel gamePanel) {
        setLayout(new BorderLayout());

        JLabel message = new JLabel("🎉 ゲームクリア！", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton retryButton = new JButton("もう一度プレイ(Enter)");
        JButton exitButton = new JButton("終了(ESC)");

        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---- ボタンアクション ----
        retryButton.addActionListener(e -> {
            gamePanel.resetGame(); // ゲームパネルを初期状態に戻す
            layout.show(parent, "Game");
            gamePanel.startGame(); // ゲーム再開
            gamePanel.requestFocusInWindow();
        });

        exitButton.addActionListener(e -> System.exit(0));

        // ---- キーバインド設定 ----
        // Enter → もう一度プレイ
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "retry");
        getActionMap().put("retry", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retryButton.doClick();
            }
        });

        // Esc → 終了
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
        getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitButton.doClick();
            }
        });
    }
}
