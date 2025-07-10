package breakout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;

public class Ball {

    private int x, y;
    private int dx = 2, dy = 3;
    final int radius = 10;

    public Ball(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, radius, radius);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, radius, radius);
    }

    // 位置情報
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRightX() {
        return x + radius;
    }

    public int getCenterX() {
        return x + radius / 2;
    } 

    public int getCenterY() {
        return y + radius / 2;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCenterX(int centerX) {
        this.x = centerX - radius / 2;
    }

    public void setCenterY(int centerY) {
        this.y = centerY - radius / 2;
    }

    // 移動速度（オプション）
    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getRadius() {
        return radius;
    }
}
