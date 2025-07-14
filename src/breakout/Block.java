package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block implements GameObject , Collidable, Movable {

    private final int x, y;
    private final int width, height;
    private final Color color;
    private boolean destroyed = false;

    @Override
    public boolean isActive() {
        return !destroyed;
    }

    @Override
    public void draw(Graphics g) {
        if (destroyed) return; //  描画しない
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void onCollision(GameObject other) {
        // 今は Ball 側がすべて処理するので、ここは空
    }

    @Override
    public void update() {
        // ブロックは動かないので空実装
    }

    public Block(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        destroyed = true;
    }

    public void setDestroyed(boolean value) {
        this.destroyed = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
