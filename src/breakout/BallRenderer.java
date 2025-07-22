package breakout;

import java.awt.*;

public class BallRenderer {
    private final BallPhysics physics;

    public BallRenderer(BallPhysics physics) {
        this.physics = physics;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) physics.getX(), (int) physics.getY(),
                   physics.getRadius() * 2, physics.getRadius() * 2);
    }
}