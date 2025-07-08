package breakout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Timer timer;
    private Paddle paddle;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private String message = null;
    private boolean showMessage = false;

    private CardLayout layout;
    private JPanel parent;

    private int bounceCount = 0;

    public GamePanel(CardLayout layout, JPanel parent) {
        this.layout = layout;
        this.parent = parent;

        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setLayout(null); // ボタンの位置を自由に設定するため

        ball = new Ball(100, 100);
        paddle = new Paddle(160, 260, 400); // 例：x=160, y=260
        setFocusable(true);
        addKeyListener(this);
        setFocusable(true);

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
                bounceCount++;

                // 3回跳ね返したらクリア(仮)
                if (bounceCount >= 3) {
                    timer.stop();
                    layout.show(parent, "Clear");
                    return; // 以降の処理を止める
                }
            }

            // ゲームオーバー判定：画面の下に出たら終了
            if (ball.getY() >= getHeight()) {
                timer.stop(); // ゲーム停止
                String[] options = { "もう一度(Enter)", "終了(ESC)" };
                int option = JOptionPane.showOptionDialog(
                        this, "ゲームオーバー\nもう一度？", "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

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
    }

    public void startGame() {
        requestFocusInWindow();
        ball = new Ball(100, 100);
        paddle = new Paddle(160, 260, 400);
        leftPressed = false;
        rightPressed = false;
        bounceCount = 0; // ← 忘れずにリセット
        repaint();
        timer.start();
    }

    public void resetGame() {
        ball = new Ball(100, 100);
        paddle = new Paddle(160, 260, 400);
        leftPressed = false;
        rightPressed = false;
        repaint();
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
