package breakout;

import java.util.List;

public class CollisionManager {
    public static void handleCollisions(Ball ball, List<GameObject> objects) {
        for (GameObject obj : objects) {
            if (obj == ball)
                continue; // 自分自身とは衝突しない

            if (obj instanceof Block block && block.isDestroyed()) {
                continue; // 破壊済みブロックはスキップ
            }

            if (obj instanceof Collidable collidable) {
                if (ball.getBounds().intersects(collidable.getBounds())) {
                    // 双方向の onCollision 呼び出し
                    ball.onCollision(obj);
                }
            }
        }
    }
}
