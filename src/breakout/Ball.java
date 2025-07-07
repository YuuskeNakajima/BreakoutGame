package breakout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;


public class Ball {

    private int x , y;
    private int dx = 2, dy = 3;
    protected int radius = 10;

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

    public int getradius() {
         return radius; 
    }
}
