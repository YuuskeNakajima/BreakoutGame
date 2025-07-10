package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Paddle {
    private int x, y;
    private final int width;
    private final int height;
    private int speed;
    private final int gameWidth; // パネルの横幅

    public Paddle(int startX, int startY, int gameWidth) {
        this.x = startX;
        this.y = startY;
        this.gameWidth = gameWidth;
        this.width = 80;
        this.height = 15;
        this.speed = 5;
    }

    public void moveLeft() {
        if (x - speed >= 0) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + speed <= gameWidth - width) {
            x += speed;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // ゲッター・セッター
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // スピード調整用
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
