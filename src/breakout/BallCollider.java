package breakout;

import java.awt.*;
import java.util.function.Consumer;

public class BallCollider {
    private final BallPhysics physics;
    private Consumer<Integer> scoreConsumer = s -> {
    };

    public BallCollider(BallPhysics physics) {
        this.physics = physics;
    }

    public void handleCollision(GameObject other) {
        if (other instanceof Paddle paddle) {
            Rectangle ballRect = physics.getBounds();
            Rectangle paddleRect = paddle.getBounds();
            // 前の位置より下から上に衝突したときだけ反転
            if (physics.getDy() > 0 &&
                    (ballRect.intersects(paddleRect)
                            || physics.getPrevY() + physics.getRadius() * 2 <= paddle.getY())) {

                // パドル中央との相対距離（-1.0〜1.0の範囲）
                double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
                double ballCenter = physics.getX() + physics.getRadius();
                double relativeIntersect = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
                relativeIntersect = Math.max(-1.0, Math.min(1.0, relativeIntersect)); // 安全な範囲に制限
                double angle = Math.toRadians(relativeIntersect * 45);

                if (Math.abs(relativeIntersect) < 0.05) {
                    angle = Math.toRadians(physics.getDx() > 0 ? -5 : 5);
                }

                // 来た方向によって反射角の符号を調整（反対向きに返す
                if (physics.getDx() > 0 && relativeIntersect < 0)
                    angle = -angle; // 右から来て左に当たった → 右に返す
                else if (physics.getDx() < 0 && relativeIntersect > 0)
                    angle = -angle; // 左から来て右に当たった → 左に返す

                double speed = physics.getCurrentSpeed();
                physics.setDx(speed * Math.sin(angle));
                physics.setDy(-speed * Math.cos(angle));

                if (Math.abs(physics.getDx()) < 0.3)
                    physics.setDx(physics.getDx() < 0 ? -0.3 : 0.3);

                physics.setY(paddle.getY() - physics.getRadius()); // パドル上に配置してめり込み防止
                physics.incrementBounce();
            }

        } else if (other instanceof Block block && !block.isDestroyed()) {
            Rectangle ballRect = physics.getBounds();
            Rectangle blockRect = block.getBounds();

            block.destroy();
            scoreConsumer.accept(100); // スコア加算

            int ballBottom = ballRect.y + ballRect.height;
            int ballTop = ballRect.y;
            int blockTop = blockRect.y;
            int blockBottom = blockRect.y + blockRect.height;
            int ballCenterX = ballRect.x + ballRect.width / 2;
            int blockLeft = blockRect.x;
            int blockRight = blockRect.x + blockRect.width;

            if (ballBottom - 1 <= blockTop) {
                physics.reverseY();
                physics.setY(blockTop - physics.getRadius());
            } else if (ballTop >= blockBottom - 1) {
                physics.reverseY();
                physics.setY(blockBottom);
            } else if (ballCenterX < blockLeft) {
                physics.reverseX();
                physics.setX(blockLeft - physics.getRadius());
            } else if (ballCenterX > blockRight) {
                physics.reverseX();
                physics.setX(blockRight);
            } else {
                physics.reverseY(); // 万が一
            }
        }
    }

    public void setScoreConsumer(Consumer<Integer> consumer) {
        this.scoreConsumer = consumer;
    }
}
