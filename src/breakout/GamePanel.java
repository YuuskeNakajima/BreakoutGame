package breakout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.util.*;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Timer timer;
    private Paddle paddle;

    private static final int INITIAL_BALL_X = 180;
    private static final int INITIAL_BALL_Y = 160;

    private String message = null;
    private int score = 0;
    private double initialBallSpeed = 5.0;
    private JLabel speedUpLabel;

    private CardLayout layout;
    private JPanel parent;

    private GameStateManager stateManager = new GameStateManager();
    private List<Block> blocks;
    private List<GameObject> gameObjects;

    public GamePanel(CardLayout layout, JPanel parent) {
        this.layout = layout;
        this.parent = parent;

        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setLayout(null); // ボタンの位置を自由に設定するため

        setFocusable(true);
        addKeyListener(this);

        setupSpeedUpLabel();

        timer = new Timer(16, e -> {
            if (!stateManager.is(GameState.RUNNING))
                return;

            // 移動
            for (GameObject obj : gameObjects) {
                if (obj instanceof Movable m) {
                    m.update();
                }
            }
            // 衝突判定
            CollisionManager.handleCollisions(ball, gameObjects);

            // 壁との反射チェック
            GameLogic.handleWallCollision(ball, getWidth(), getHeight());

            // === ゲームクリア判定 ===
            if (GameLogic.isGameCleared(gameObjects)) {
                timer.stop();
                stateManager.transitionTo(GameState.CLEAR); // ←追加検討
                layout.show(parent, "Clear");
            }

            // ゲームオーバー判定：画面の下に出たら終了
            if (ball.getY() >= getHeight()) {
                timer.stop(); // ゲーム停止
                stateManager.transitionTo(GameState.GAMEOVER);
                String[] options = { "もう一度(Enter)", "終了(ESC)" };
                int option = JOptionPane.showOptionDialog(
                        this, "ゲームオーバー\nもう一度？", "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (option == 0) {
                    resetGame(); // 初期化（ボール位置やブロック再配置など）

                    message = "Enterキーで再開します";
                    stateManager.transitionTo(GameState.READY);
                    repaint();

                } else {
                    System.exit(0); // 終了
                }
            }
            repaint(); // 描画
        });
    }

    public void startGame() {
        resetGame();
        stateManager.transitionTo(GameState.READY);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    public void startGame(double speed) {
        this.initialBallSpeed = speed;
        startGame(); // 通常のスタート処理へ
    }

    public void resetGame() {
        timer.stop();
        blocks = new ArrayList<>(BlockManager.createStage(1));
        requestFocusInWindow();
        // 安全な位置（中央より下側）に置く
        ball = new Ball((double) INITIAL_BALL_X, (double) INITIAL_BALL_Y, initialBallSpeed);
        // スコア加算用のラムダ式を設定
        ball.setScoreConsumer(points -> score += points);
        ball.setSpeedChangeListener(() -> showSpeedUpNotice());
        paddle = new Paddle(160, 260, 400);
        score = 0;

        gameObjects = new ArrayList<>();
        gameObjects.addAll(blocks);
        gameObjects.add(paddle);
        gameObjects.add(ball);

        ball.resetBounceCount(); // ボールのバウンドカウントをリセット

        stateManager.transitionTo(GameState.READY);
        message = "Enterキーで開始";

        repaint();
    }

    private void setupSpeedUpLabel() {
        speedUpLabel = new JLabel("スピードアップ！", SwingConstants.CENTER);
        speedUpLabel.setForeground(Color.YELLOW);
        speedUpLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        speedUpLabel.setVisible(false);
        speedUpLabel.setBounds(100, 10, 200, 30);
        this.add(speedUpLabel);
    }

    public void showSpeedUpNotice() {
        speedUpLabel.setVisible(true);
        Timer timer = new Timer(1000, e -> speedUpLabel.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("SCORE: " + score, 20, 20);

        if (stateManager.is(GameState.READY) || stateManager.is(GameState.GAMEOVER)) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString(message, getWidth() / 2 - 60, getHeight() / 2);
        }

        for (GameObject obj : gameObjects) {
            obj.draw(g);
        }

        g.setColor(Color.WHITE);

        // ポーズ状態の表示
        if (stateManager.is(GameState.PAUSE)) {
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("PAUSED", getWidth() / 2 - 40, getHeight() / 2);
        } else {
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("P：PAUSE", getWidth() / 2 + 110, 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        boolean changed = false;

        // Enterキーでゲームスタート
        if (key == KeyEvent.VK_ENTER) {
            if (stateManager.is(GameState.READY) || stateManager.is(GameState.GAMEOVER)) {
                stateManager.transitionTo(GameState.RUNNING);
                message = null;
                timer.start();
                return;
            }
        }
        // ポーズ切り替え
        if (stateManager.is(GameState.RUNNING) && key == KeyEvent.VK_P) {
            stateManager.transitionTo(GameState.PAUSE);
            changed = true;
            return; // Pキー以外の入力は無視
        } else if (key == KeyEvent.VK_P && stateManager.is(GameState.PAUSE)) {
            stateManager.transitionTo(GameState.RUNNING);
            changed = true;
            return;
        }
        // 移動キー処理
        if (stateManager.is(GameState.RUNNING)) {
            if (key == KeyEvent.VK_LEFT) {
                paddle.setMoveLeft(true);
            } else if (key == KeyEvent.VK_RIGHT) {
                paddle.setMoveRight(true);
            }
            if (changed) {
                repaint(); // 表示を更新
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (stateManager.is(GameState.RUNNING)) {
            if (key == KeyEvent.VK_LEFT) {
                paddle.setMoveLeft(false);
            } else if (key == KeyEvent.VK_RIGHT) {
                paddle.setMoveRight(false);
            }
        } else {
            return;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // キータイプイベントは使用しないので空実装
    }
}
