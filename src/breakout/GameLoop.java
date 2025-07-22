package breakout;

import javax.swing.*;
import java.util.List;

public class GameLoop {
    private final GamePanel gamePanel;
    private final Timer timer;

    public GameLoop(GamePanel panel) {
        this.gamePanel = panel;
        this.timer = new Timer(16, e -> update());
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void update() {
        if (!gamePanel.getStateManager().is(GameState.RUNNING)) {
            return;
        }

        ClearPanel clearPanel = gamePanel.getClearPanel();
        if (clearPanel != null) {
            clearPanel.updateScoreLabel();
        }

        try {
            List<GameObject> gameObjects = gamePanel.getGameObjects();
            Ball ball = gamePanel.getBall();

            // 移動
            for (GameObject obj : gameObjects) {
                if (obj instanceof Movable m) {
                    m.update();
                }
            }

            // 衝突判定
            CollisionManager.handleCollisions(ball, gameObjects);

            // 壁との反射チェック
            GameLogic.handleWallCollision(ball, gamePanel.getWidth(), gamePanel.getHeight());

            // ゲームクリア判定
            if (GameLogic.isGameCleared(gameObjects)) {
                stop();
                // nullチェックしてからスコア更新＋画面遷移
                if (clearPanel != null) {
                    clearPanel.updateScoreLabel();
                    gamePanel.getStateManager().transitionTo(GameState.CLEAR);
                    gamePanel.getLayoutCard().show(gamePanel.getParentPanel(), "Clear");
                }
            }

            // ゲームオーバー判定：画面の下に出たら終了
            if (ball.getY() >= gamePanel.getHeight()) {
                stop(); // ゲーム停止
                gamePanel.getStateManager().transitionTo(GameState.GAMEOVER);
                int option = JOptionPane.showOptionDialog(
                        gamePanel,
                        "ゲームオーバー\nスコア: " + GameManager.getTotalScore() + "\nもう一度？",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"もう一度(Enter)", "終了(ESC)"},
                        "もう一度(Enter)"
                );

                if (option == 0) {
                    GameManager.resetScore();
                    gamePanel.resetGame(); // 初期化（ボール位置やブロック再配置など）
                    gamePanel.setMessage("Enterキーで再開します");
                    gamePanel.getStateManager().transitionTo(GameState.READY);
                    gamePanel.repaint();
                } else {
                    GameManager.resetScore();
                    System.exit(0); // 終了
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // コンソールに出力
        }

        gamePanel.repaint(); // 描画
    }
}
