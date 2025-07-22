package breakout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.util.*;

public class GamePanel extends JPanel implements KeyListener {
    private Ball ball;
    private Paddle paddle;

    private double initialBallSpeed = 5.0;
    private String message;

    private CardLayout layout;
    private JPanel parent;
    private ClearPanel clearPanel;

    private GameStateManager stateManager = new GameStateManager();
    private GameLoop gameLoop;
    private GameRenderer renderer = new GameRenderer();
    private InputHandler inputHandler;
    private SpeedUpNotice speedUpNotice;
    private GameUIManager uiManager;

    private List<GameObject> gameObjects;

    public GamePanel(CardLayout layout, JPanel parent, ClearPanel clearPanel) {
        this.layout = layout;
        this.parent = parent;
        this.clearPanel = clearPanel;

        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.BLACK);
        setLayout(null); // ボタンの位置を自由に設定するため

        setFocusable(true);
        addKeyListener(this);

        this.inputHandler = new InputHandler(this);
        this.speedUpNotice = new SpeedUpNotice(this);
        this.uiManager = new GameUIManager(this);

        gameLoop = new GameLoop(this); // ← GameLoopを初期化
    }

    public void startGame() {
        resetGame();
        if (!stateManager.is(GameState.READY)) {
            stateManager.transitionTo(GameState.READY);
        }
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    public void startGame(double speed) {
        this.initialBallSpeed = speed;
        startGame(); // 通常のスタート処理へ
    }

    public void resetGame() {
        gameLoop.stop(); // ← ゲームループを停止
        requestFocusInWindow();
        gameObjects = GameInitializer.createObjects(this, initialBallSpeed);

        // 生成済みのボールを取得
        this.ball = gameObjects.stream()
                .filter(obj -> obj instanceof Ball)
                .map(obj -> (Ball) obj)
                .findFirst()
                .orElse(null);

        if (ball != null) {
            // スコア加算の処理をセット
            ball.setScoreConsumer(points -> {
                GameManager.addScore(points);
                updateScore(); // UI更新
            });
            ball.resetBounceCount();
        }

        ball.resetBounceCount(); // ボールのバウンドカウントをリセット
        setMessage("Enterキーで開始");
        updateScore();
        repaint();
    }

    // ==== Getter類（GameLoopからアクセスするため） ====

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Ball getBall() {
        return ball;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    public ClearPanel getClearPanel() {
        return clearPanel;
    }

    public CardLayout getLayoutCard() {
        return layout;
    }

    public JPanel getParentPanel() {
        return parent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
        repaint(); // メッセージ変わったら再描画する
    }

    public void clearMessage() {
        setMessage(null);
    }

    // スコア更新メソッド
    public void updateScore() {
        uiManager.updateScore(GameManager.getTotalScore());
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    // ========================

    public void showSpeedUpNotice() {
        speedUpNotice.show();
    }

    public void setClearPanel(ClearPanel clearPanel) {
        this.clearPanel = clearPanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render(g, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        inputHandler.handleKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        inputHandler.handleKeyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // キータイプイベントは使用しないので空実装
    }
}
