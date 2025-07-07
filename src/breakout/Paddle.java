package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Paddle {

    int x, y, width, height, speed, panelWidth;

    public Paddle(int startX, int startY, int panelWidth) {
        this.x = startX;
        this.y = startY;
        this.panelWidth = panelWidth;
        this.width = 80;
        this.height = 15;
        this.speed = 5;
    }

    public void leftmove() {
        if (x - speed >= 0) {
            x -= speed;
        }
    }

    public void rightmove() {
        if (x + width + speed <= panelWidth) {
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
}

    

