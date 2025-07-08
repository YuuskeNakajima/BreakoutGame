package breakout;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends JPanel {

    private JButton startButton;

    public TitlePanel(CardLayout layout, JPanel parent) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("ブロック崩し", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(title, BorderLayout.CENTER);

        this.startButton = new JButton("スタート(Enter)");
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            layout.show(parent, "Game");
            parent.requestFocusInWindow(); // キー入力有効にする
            // GamePanelを取得してゲームを開始する
            Component comp = parent.getComponent(1); // "Game"パネルのインデックス
            if (comp instanceof GamePanel gp) {
                gp.startGame();
            }
        });
    }

    public JButton getStartButton() {
        return startButton;
    }
}
