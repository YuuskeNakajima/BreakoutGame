package breakout;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.function.Consumer;
import java.awt.Graphics;

public class Ball implements GameObject, Movable, Collidable {

    private double x, y;
    private double dx = 2, dy = 3;// 移動速度
    private double initialSpeed;
    final int radius = 10;
    private double prevX, prevY;
    private int bounceCount = 0;
    private Consumer<Integer> scoreConsumer = s -> {
    }; // デフォルトは何もしない

    // Ball クラスに listener 登録機能
    private SpeedChangeListener speedChangeListener;
    private double currentSpeed; //現在の速度

    @Override
    public void onCollision(GameObject other) {
        if (other instanceof Paddle paddle) {
            Rectangle ballRect = getBounds();
            Rectangle paddleRect = paddle.getBounds();
            // 前の位置より下から上に衝突したときだけ反転
            if (dy > 0 && (ballRect.intersects(paddleRect) ||
                    (prevY + radius * 2 <= paddle.getY() && y + radius * 2 >= paddle.getY()))) {

                // パドル中央との相対距離（-1.0〜1.0の範囲）
                double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
                double ballCenter = getX() + getRadius();
                double relativeIntersect = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);
                relativeIntersect = Math.max(-1.0, Math.min(1.0, relativeIntersect)); // 安全な範囲に制限

                double angle = Math.toRadians(relativeIntersect * 45);

                if (Math.abs(relativeIntersect) < 0.05) {
                    angle = Math.toRadians(dx > 0 ? -5 : 5);
                }

                // 来た方向によって反射角の符号を調整（反対向きに返す）
                if (dx > 0 && relativeIntersect < 0) {
                    angle = -angle; // 右から来て左に当たった → 右に返す
                } else if (dx < 0 && relativeIntersect > 0) {
                    angle = -angle; // 左から来て右に当たった → 左に返す
                }

                double speed = this.currentSpeed;
                dx = speed * Math.sin(angle);
                dy = -speed * Math.cos(angle);

                if (Math.abs(dx) < 0.3) {
                    dx = dx < 0 ? -0.3 : 0.3;
                }

                setY(paddle.getY() - radius); // パドル上に配置してめり込み防止
                bounceCount++;
            }

        } else if (other instanceof Block block) {
            if (!block.isDestroyed()) {
                Rectangle ballRect = getBounds();
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
                    reverseY();
                    setY(blockTop - getRadius());
                } else if (ballTop >= blockBottom - 1) {
                    reverseY();
                    setY(blockBottom);
                } else if (ballCenterX < blockLeft) {
                    reverseX();
                    setX(blockLeft - getRadius());
                } else if (ballCenterX > blockRight) {
                    reverseX();
                    setX(blockRight);
                } else {
                    reverseY(); // 万が一
                }
            }
        }
    }

    @Override
    public void update() {
        prevX = x;
        prevY = y;
        x += dx;
        y += dy;

        // ボール反射数に応じてスピードを増加（10の倍数、最大5回まで）
        int bounce = getBounceCount();
        if (bounce > 0 && bounce % 10 == 0 && bounce <= 50) {
            double multiplier = 1.0 + (bounce / 5) * 0.2; // 10回で+0.1ずつ → 最大2倍（超える場合も考慮）
            if (multiplier > 2)
                multiplier = 2; // 上限制限
            normalizeSpeed(multiplier);

            if (speedChangeListener != null) {
                speedChangeListener.onSpeedUp();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) x, (int) y, radius * 2, radius * 2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, radius * 2, radius * 2);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public Ball(double startX, double startY, double speed) {
        this.x = startX;
        this.y = startY;
        this.initialSpeed = speed;
        this.currentSpeed = speed; //初期速度を現在の速度にセット
        this.dx = 0;
        this.dy = speed;
    }

    public Ball(double startX, double startY) {
        this(startX, startY, 5.0);
    }

    public int getBounceCount() {
        return bounceCount;
    }

    public void resetBounceCount() {
        bounceCount = 0;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    private void normalizeSpeed(double multiplier) {
        double targetSpeed = this.initialSpeed * multiplier;
        this.currentSpeed = targetSpeed;// 現在の速度を記録
        double speed = Math.sqrt(dx * dx + dy * dy);

        if (speed == 0)
            return;

        dx = dx / speed * targetSpeed;
        dy = dy / speed * targetSpeed;
    }

    // 位置情報
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public int getRightX() {
        return (int) (x + radius * 2);
    }

    public int getCenterX() {
        return (int) (x + radius);
    }

    public int getCenterY() {
        return (int) (y + radius);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setCenterX(int centerX) {
        this.x = centerX - radius / 2;
    }

    public void setCenterY(int centerY) {
        this.y = centerY - radius / 2;
    }

    // 移動速度（オプション）
    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getRadius() {
        return radius;
    }

    public int getBottom() {
        return (int) y + radius * 2;
    }

    public void setScoreConsumer(Consumer<Integer> consumer) {
        this.scoreConsumer = consumer;
    }

    public void setSpeedChangeListener(SpeedChangeListener listener) {
        this.speedChangeListener = listener;
    }

}
