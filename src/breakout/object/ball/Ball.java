package breakout.object.ball;

import java.awt.*;
import java.util.function.Consumer;

import breakout.interfaces.Collidable;
import breakout.interfaces.GameObject;
import breakout.interfaces.Movable;
import breakout.interfaces.SpeedChangeListener;

public class Ball implements GameObject, Movable, Collidable {

    private final BallPhysics physics;
    private final BallRenderer renderer;
    private final BallCollider collider;

    public Ball(double startX, double startY, double speed) {
        this.physics = new BallPhysics(startX, startY, speed);
        this.renderer = new BallRenderer(physics);
        this.collider = new BallCollider(physics);
    }

    public Ball(double startX, double startY) {
        this(startX, startY, 5.0);
    }

    @Override
    public void update() {
        physics.update();
    }

    @Override
    public void draw(Graphics g) {
        renderer.draw(g);
    }

    @Override
    public void onCollision(GameObject other) {
        collider.handleCollision(other);
    }

    @Override
    public Rectangle getBounds() {
        return physics.getBounds();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public void setScoreConsumer(Consumer<Integer> consumer) {
        collider.setScoreConsumer(consumer);
    }

    public void setSpeedChangeListener(SpeedChangeListener listener) {
        physics.setSpeedChangeListener(listener);
    }

    // 状態取得用（必要なら）
    public int getBounceCount() {
        return physics.getBounceCount();
    }

    public void resetBounceCount() {
        physics.resetBounceCount();
    }

    public void setX(double x) {
        physics.setX(x);
    }

    public void setY(double y) {
        physics.setY(y);
    }

    public double getDx() {
        return physics.getDx();
    }

    public double getDy() {
        return physics.getDy();
    }

    public int getRadius() {
        return physics.getRadius();
    }

    public int getBottom() {
        return physics.getBottom();
    }

    public double getX() {
        return physics.getX();
    }

    public double getY() {
        return physics.getY();
    }

    public void reverseX() {
        physics.reverseX();
    }

    public void reverseY() {
        physics.reverseY();
    }
}
