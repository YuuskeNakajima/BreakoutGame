package breakout.object.paddle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import breakout.interfaces.Collidable;
import breakout.interfaces.GameObject;
import breakout.interfaces.Movable;

public class Paddle implements GameObject, Movable, Collidable {
    private int x, y;
    private final int width;
    private final int height;
    private int speed;
    private final int gameWidth; // パネルの横幅

    // キー入力状態
    private boolean moveLeft = false;
    private boolean moveRight = false;

    @Override
    public void update() {
        if (moveLeft && x - speed >= 0) {
            x -= speed;
        }
        if (moveRight && x + speed <= gameWidth - width) {
            x += speed;
        }
    }

    public void setMoveLeft(boolean move) {
        this.moveLeft = move;
    }

    public void setMoveRight(boolean move) {
        this.moveRight = move;
    }

    @Override
    public boolean isActive() {
        return true; // パドルは常にアクティブ
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    @Override
    public void onCollision(GameObject other) {
        // Paddle では受け身のまま空実装
    }

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

    public boolean isMovingLeft() {
        return moveLeft;
    }

    public boolean isMovingRight() {
        return moveRight;
    }

}
