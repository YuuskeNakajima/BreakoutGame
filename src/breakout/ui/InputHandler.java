package breakout.ui;

import java.awt.event.KeyEvent;

import breakout.game.GameState;
import breakout.game.GameStateManager;
import breakout.object.paddle.Paddle;

public class InputHandler {
    private final GamePanel panel;

    public InputHandler(GamePanel panel) {
        this.panel = panel;
    }

    public void handleKeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        GameStateManager stateManager = panel.getStateManager();

        // Enterキーでゲームスタート
        if (key == KeyEvent.VK_ENTER) {
            if (stateManager.is(GameState.READY) || stateManager.is(GameState.GAMEOVER)) {
                stateManager.transitionTo(GameState.RUNNING);
                panel.setMessage(null);
                panel.getGameLoop().start(); // ← GameLoop開始
                return;
            }
        }

        // ポーズ切り替え
        if (stateManager.is(GameState.RUNNING) && key == KeyEvent.VK_P) {
            stateManager.transitionTo(GameState.PAUSE);
            panel.repaint();
        } else if (key == KeyEvent.VK_P && stateManager.is(GameState.PAUSE)) {
            stateManager.transitionTo(GameState.RUNNING);
        }

        // 移動キー処理
        if (stateManager.is(GameState.RUNNING)) {
            Paddle paddle = panel.getPaddle();
            if (key == KeyEvent.VK_LEFT) {
                paddle.setMoveLeft(true);
            } else if (key == KeyEvent.VK_RIGHT) {
                paddle.setMoveRight(true);
            }
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (panel.getStateManager().is(GameState.RUNNING)) {
            Paddle paddle = panel.getPaddle();
            if (key == KeyEvent.VK_LEFT) {
                paddle.setMoveLeft(false);
            } else if (key == KeyEvent.VK_RIGHT) {
                paddle.setMoveRight(false);
            }
        }
    }
}
