package breakout.ui;

import javax.swing.*;

import java.awt.*;

public class GameUIManager {
    private JLabel messageLabel;
    private JLabel scoreLabel;
    private SpeedUpNotice speedUpNotice;

    public GameUIManager(JPanel parentPanel) {
        setupMessageLabel(parentPanel);
        setupScoreLabel(parentPanel);
        speedUpNotice = new SpeedUpNotice(parentPanel);
    }

    private void setupMessageLabel(JPanel panel) {
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        messageLabel.setBounds(100, 260, 200, 30);
        messageLabel.setVisible(false);
        panel.add(messageLabel);
    }

    private void setupScoreLabel(JPanel panel) {
        scoreLabel = new JLabel("SCORE: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setBounds(0, 0, 100, 30);
        panel.add(scoreLabel);
    }

    public void updateScore(int score) {
        scoreLabel.setText("スコア: " + score);
    }

    public void showSpeedUpNotice() {
        speedUpNotice.show();
    }

    public String getMessage() {
        return messageLabel.getText();
    }

}
