package breakout.interfaces;

import java.awt.Rectangle;

public interface Collidable {
    Rectangle getBounds();
    void onCollision(GameObject other);
}
