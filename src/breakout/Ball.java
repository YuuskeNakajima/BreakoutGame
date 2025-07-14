package breakout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;

public class Ball implements GameObject, Movable, Collidable {

    private int x, y;
    private int dx = 2, dy = 3;// 移動速度
    final int radius = 10;
    private int prevX, prevY;
    private int bounceCount = 0;

    @Override
    public void onCollision(GameObject other) {
        if (other instanceof Paddle paddle) {
            Rectangle ballRect = getBounds();
            Rectangle paddleRect = paddle.getBounds();
            // 前の位置より下から上に衝突したときだけ反転
            if (ballRect.intersects(paddleRect) && prevY + radius * 2 <= paddle.getY()) {
                reverseY();
                setY(paddle.getY() - radius); // パドル上に配置してめり込み防止
                bounceCount++;
            }


        } else if (other instanceof Block block) {
            if (!block.isDestroyed()) {
                Rectangle ballRect = getBounds();
                Rectangle blockRect = block.getBounds();

                block.destroy();

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
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, radius * 2, radius * 2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, radius * 2, radius * 2);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    public Ball(int startX, int startY) {
        this.x = startX;
        this.y = startY;
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

    public int getBottom() {
        return y + radius * 2;
    }
}
