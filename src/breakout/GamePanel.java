package breakout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.util.*;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Timer timer;
    private Paddle paddle;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private static final int INITIAL_BALL_X = 180;
    private static final int INITIAL_BALL_Y = 160;

    private String message = null;

    private CardLayout layout;
    private JPanel parent;

    private int bounceCount = 0;
    private GameState gameState = GameState.READY;

    private List<Block> blocks;

    public GamePanel(CardLayout layout, JPanel parent) {
        this.layout = layout;
        this.parent = parent;

        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setLayout(null); // ボタンの位置を自由に設定するため

        ball = new Ball(INITIAL_BALL_X, INITIAL_BALL_Y);
        paddle = new Paddle(160, 260, 400); // 例：x=160, y=260
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, e -> {
            if (gameState != GameState.RUNNING)
                return;

            // ボール移動
            ball.move();

            // キーによるパドル移動
            if (leftPressed) {
                paddle.moveLeft();
            }
            if (rightPressed) {
                paddle.moveRight();
            }

            // 壁との反射チェック
            if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getRadius()) {
                ball.reverseX();
            }
            if (ball.getY() <= 0) {
                ball.reverseY();
            }

            // パドルとの衝突
            if (ball.getBounds().intersects(paddle.getBounds())) {
                ball.reverseY();
                ball.setY(paddle.getY() - ball.getRadius()); // めり込み防止
                bounceCount++;

                // 30回跳ね返したらクリア(仮)
                if (bounceCount >= 30) {
                    timer.stop();
                    layout.show(parent, "Clear");
                    return; // 以降の処理を止める
                }
            }

            // ブロックとの衝突
            for (Block block : blocks) {
                if (!block.isDestroyed()) {
                    Rectangle ballRect = ball.getBounds();
                    Rectangle blockRect = block.getBounds();

                    if (ballRect.intersects(blockRect)) {
                        block.destroy();

                        int ballBottom = ballRect.y + ballRect.height;
                        int ballTop = ballRect.y;
                        int blockTop = blockRect.y;
                        int blockBottom = blockRect.y + blockRect.height;
                        int ballCenterX = ballRect.x + ballRect.width / 2;
                        int blockLeft = blockRect.x;
                        int blockRight = blockRect.x + blockRect.width;

                        if (ballBottom - 1 <= blockTop) {
                            // 上から衝突
                            ball.reverseY();
                            ball.setY(blockTop - ball.getRadius());
                        } else if (ballTop >= blockBottom - 1) {
                            // 下から衝突
                            ball.reverseY();
                            ball.setY(blockBottom);
                        } else if (ballCenterX < blockLeft) {
                            // 左から衝突
                            ball.reverseX();
                            ball.setX(blockLeft - ball.getRadius());
                        } else if (ballCenterX > blockRight) {
                            // 右から衝突
                            ball.reverseX();
                            ball.setX(blockRight);
                        } else {
                            // 万が一、判断できないときは Y反転
                            ball.reverseY();
                        }
                        break; // ブロックを破壊したらループを抜ける
                    }

                }
            }

            if (blocks != null && blocks.stream().allMatch(Block::isDestroyed)) {
                timer.stop();
                layout.show(parent, "Clear");
            }

            // ゲームオーバー判定：画面の下に出たら終了
            if (ball.getY() >= getHeight()) {
                timer.stop(); // ゲーム停止
                gameState = GameState.GAMEOVER;
                String[] options = { "もう一度(Enter)", "終了(ESC)" };
                int option = JOptionPane.showOptionDialog(
                        this, "ゲームオーバー\nもう一度？", "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (option == 0) {
                    // 入力状態リセット
                    leftPressed = false;
                    rightPressed = false;

                    resetGame(); // 初期化（ボール位置やブロック再配置など）

                    message = "Enterキーで再開します";
                    gameState = GameState.READY;
                    repaint();

                } else {
                    System.exit(0); // 終了
                }
            }
            repaint(); // 描画
        });
    }

    private void createBlocks() {
        blocks = new ArrayList<>();
        int blockWidth = 50;
        int blockHeight = 20;
        int rows = 3;
        int cols = 7;
        int startX = 20;
        int startY = 40;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (blockWidth + 5);
                int y = startY + row * (blockHeight + 5);
                blocks.add(new Block(x, y, blockWidth, blockHeight, Color.CYAN));
            }
        }
    }

    public void startGame() {
        resetGame();
        gameState = GameState.READY;
        requestFocusInWindow();
    }

    public void resetGame() {
        timer.stop();
        createBlocks(); // ブロック先に作る
        requestFocusInWindow();
        ball = new Ball(INITIAL_BALL_X, INITIAL_BALL_Y); // 安全な位置（中央より下側）に置く
        paddle = new Paddle(160, 260, 400);
        bounceCount = 0;
        leftPressed = false;
        rightPressed = false;

        gameState = GameState.READY;
        message = "Enterキーで開始";

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState == GameState.READY || gameState == GameState.GAMEOVER) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString(message, getWidth() / 2 - 60, getHeight() / 2);
        }

        for (Block block : blocks) {
            if (!block.isDestroyed()) {
                block.draw(g);
            }
        }
        paddle.draw(g);
        ball.draw(g);

        g.setColor(Color.WHITE);

        // ポーズ状態の表示
        if (gameState == GameState.PAUSE) {
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
            if (gameState == GameState.READY || gameState == GameState.GAMEOVER) {
                gameState = GameState.RUNNING;
                message = null;
                timer.start();
                return;
            }
        }
        // ポーズ切り替え
        if (gameState == GameState.RUNNING && key == KeyEvent.VK_P) {
            gameState = GameState.PAUSE;
            changed = true;
            return; // Pキー以外の入力は無視
        } else if (key == KeyEvent.VK_P && gameState == GameState.PAUSE) {
            gameState = GameState.RUNNING;
            changed = true;
            return;
        }
        // 移動キー処理
        if (gameState == GameState.RUNNING) {
            if (key == KeyEvent.VK_LEFT) {
                if (!leftPressed) {
                    changed = true;
                }
                leftPressed = true;
            } else if (key == KeyEvent.VK_RIGHT) {
                if (!rightPressed) {
                    changed = true;
                }
                rightPressed = true;
            }
            if (changed) {
                repaint(); // 表示を更新
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameState != GameState.RUNNING)
            return;

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // キータイプイベントは使用しないので空実装
    }

}
