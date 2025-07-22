package breakout.ui;

import javax.swing.*;

import breakout.game.GameManager;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ClearPanel extends JPanel {
    @SuppressWarnings("unused")
    private final CardLayout layout;
    @SuppressWarnings("unused")
    private final JPanel     parent;
    private JLabel scoreLabel;
    private GamePanel gamePanel;

    public ClearPanel(CardLayout layout, JPanel parent, GamePanel gp) {
        this.layout = layout;
        this.parent = parent;
        this.gamePanel = gp;
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

        scoreLabel = new JLabel("スコア: " + GameManager.getTotalScore(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        scoreLabel.setForeground(Color.black);
        scoreLabel.setBounds(100, 100, 200, 50);
        add(scoreLabel, BorderLayout.NORTH);

        // ---- ボタンアクション ----
        retryButton.addActionListener(e -> {
            layout.show(parent, "Game");
            if (gamePanel != null) {
                gamePanel.startGame(); // ゲーム再開
                gamePanel.requestFocusInWindow();
            }
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
                GameManager.resetScore();
            }
        });
    }

    public void setGamePanel(GamePanel gp) {
        this.gamePanel = gp;
    }

    public void updateScoreLabel() {
        scoreLabel.setText("スコア: " + GameManager.getTotalScore());
    }
}
