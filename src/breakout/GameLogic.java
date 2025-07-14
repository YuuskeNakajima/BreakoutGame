package breakout;

import java.util.List;

public class GameLogic {

    public static void handleWallCollision(Ball ball, int panelWidth, int panelHeight) {
        // X方向判定
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.reverseX();
        } else if (ball.getX() >= panelWidth - ball.getRadius() * 2) {
            ball.setX(panelWidth - ball.getRadius() * 2);
            ball.reverseX();
        }

        // Y方向判定（上端のみ反射）
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.reverseY();
        }
    }

    public static boolean isGameCleared(List<GameObject> objects) {
        return objects.stream()
                .filter(o -> o instanceof Block)
                .map(o -> (Block) o)
                .allMatch(Block::isDestroyed);
    }
}
