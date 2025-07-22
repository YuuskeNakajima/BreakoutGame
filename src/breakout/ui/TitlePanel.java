package breakout.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class TitlePanel extends JPanel {

    private JButton startButton;
    private JSlider speedSlider;

    public TitlePanel(CardLayout layout, JPanel parent) {
        setLayout(new BorderLayout());

        // --- 中央パネル（タイトル + スライダー） ---
        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("ブロック崩し", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        centerPanel.add(title, BorderLayout.NORTH);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // これで上に50pxの余白が入る

        JPanel sliderPanel = new JPanel(new BorderLayout());
        JLabel sliderLabel = new JLabel("ボール速度：", SwingConstants.CENTER);
        sliderPanel.add(sliderLabel, BorderLayout.NORTH);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        speedSlider = new JSlider(1, 8, 5); // スライダーを 1〜8 に設定
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(300, 50));

        // スライダーを左右に余白ありで中央寄せ
        JPanel sliderWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        sliderWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 上に20px余白
        sliderWrapper.add(speedSlider);

        sliderPanel.add(sliderLabel, BorderLayout.NORTH);
        sliderPanel.add(sliderWrapper, BorderLayout.CENTER); // ← 直でspeedSliderを置かない

        centerPanel.add(sliderPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- スタートボタン ---
        this.startButton = new JButton("スタート(Enter)");
        add(startButton, BorderLayout.SOUTH);

        // --- ボタンのアクション ---
        startButton.addActionListener(e -> startGame(layout, parent));

        // --- Enterキーでスタート（どこにフォーカスがあっても有効） ---
        InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "startGame");
        am.put("startGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(layout, parent);
            }
        });

        // 初期フォーカスはスライダーにしておく
        SwingUtilities.invokeLater(() -> speedSlider.requestFocusInWindow());
    }

    private void startGame(CardLayout layout, JPanel parent) {
        double selectedSpeed = speedSlider.getValue();

        // 安全な GamePanel 取得
        GamePanel gp = findGamePanel(parent); // "Game"パネルのインデックス
        if (gp != null) {
            gp.startGame(selectedSpeed);
            SwingUtilities.invokeLater(gp::requestFocusInWindow); // 安全なフォーカス渡し
        }

        layout.show(parent, "Game");
    }

    public JButton getStartButton() {
        return startButton;
    }

    public int getSelectedSpeed() {
        return speedSlider.getValue();
    }

    public void requestSliderFocus() {
        SwingUtilities.invokeLater(() -> speedSlider.requestFocusInWindow());
    }

    private GamePanel findGamePanel(JPanel parent) {
        for (Component comp : parent.getComponents()) {
            if (comp instanceof GamePanel) {
                return (GamePanel) comp;
            }
        }
        return null;
    }
}
