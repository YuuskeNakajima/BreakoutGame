package breakout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Timer timer;
    private Paddle paddle;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private String message = null;
    private boolean showMessage = false;

    public GamePanel() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setLayout(null); // ボタンの位置を自由に設定するため

        ball = new Ball(100, 100);
        paddle = new Paddle(160, 260, 400); // 例：x=160, y=260
        setFocusable(true);
        addKeyListener(this);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, e -> {

            // ボール移動
            ball.move();

            // キーによるパドル移動
            if (leftPressed) {
                paddle.leftmove();
            }
            if (rightPressed) {
                paddle.rightmove();
            }

            // 壁との反射チェック
            if (ball.getX() <= 0 || ball.getX() >= getWidth() - ball.getradius()) {
                ball.reverseX();
            }

            if (ball.getY() <= 0) {
                ball.reverseY();
            }

            // パドルとの衝突
            if (ball.getBounds().intersects(paddle.getBounds())) {
                ball.reverseY();
                ball.setY(paddle.getY() - ball.getradius()); // めり込み防止
            }

            // ゲームオーバー判定：画面の下に出たら終了
            if (ball.getY() >= getHeight()) {
                timer.stop(); // ゲーム停止
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Game Over\nもう一度プレイしますか？",
                        "ゲームオーバー",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    // 入力状態リセット（❷）
                    leftPressed = false;
                    rightPressed = false;

                    // 初期化（❶）
                    ball = new Ball(100, 100);
                    paddle = new Paddle(160, 260, 400);

                    // メッセージを設定して表示
                    message = "1秒後に再開します";
                    showMessage = true;
                    repaint();

                    // 再開タイミングを遅らせる：1秒後に開始
                    Timer restartTimer = new Timer(1000, ev -> {
                        showMessage = false; // メッセージ非表示
                        message = null;
                        timer.start();
                    });
                    restartTimer.setRepeats(false); // 一度だけ実行
                    restartTimer.start();
                } else {
                    System.exit(0); // 終了
                }
            }

            repaint(); // 描画
        });
        timer.start();
    }

    public void startGame() {
        requestFocusInWindow();
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paddle.draw(g);
        ball.draw(g);
        if (showMessage && message != null) {
            g.setColor(Color.WHITE);
            g.drawString(message, getWidth() / 2 - 50, 20);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
