package breakout;

import java.awt.*;

public class BallPhysics {

    private double x, y;
    private double dx, dy; // 移動速度
    private final int radius = 10;
    private final double initialSpeed;
    private double currentSpeed; //現在の速度
    private double prevX, prevY;
    private int bounceCount = 0;
    private SpeedChangeListener speedChangeListener;

    public BallPhysics(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.initialSpeed = speed;
        this.currentSpeed = speed;
        this.dx = 0;
        this.dy = speed;
    }

    public void update() {
        prevX = x;
        prevY = y;
        x += dx;
        y += dy;

        // ボール反射数に応じてスピードを増加（10の倍数、最大5回まで）
        if (bounceCount > 0 && bounceCount % 10 == 0 && bounceCount <= 50) {
            double multiplier = 1.0 + (bounceCount / 5) * 0.2;// 10回で+0.1ずつ → 最大2倍（超える場合も考慮
            if (multiplier > 2)
                multiplier = 2;// 上限制限
            normalizeSpeed(multiplier);
            if (speedChangeListener != null)
                speedChangeListener.onSpeedUp();
        }
    }

    public void normalizeSpeed(double multiplier) {
        double targetSpeed = initialSpeed * multiplier;
        currentSpeed = targetSpeed; // 現在の速度を記録
        double speed = Math.sqrt(dx * dx + dy * dy);
        if (speed == 0)
            return;
        dx = dx / speed * targetSpeed;
        dy = dy / speed * targetSpeed;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, radius * 2, radius * 2);
    }

    public int getBounceCount() {
        return bounceCount;
    }

    public void incrementBounce() {
        bounceCount++;
    }

    public void resetBounceCount() {
        bounceCount = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    // 位置情報
    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getBottom() {
        return (int) y + radius * 2;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // 移動速度（オプション）
    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setSpeedChangeListener(SpeedChangeListener listener) {
        this.speedChangeListener = listener;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }
}
