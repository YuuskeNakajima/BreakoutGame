package breakout;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class GameRenderer {
    public void render(Graphics g, GamePanel panel) {
        GameStateManager stateManager = panel.getStateManager();
        List<GameObject> objects = panel.getGameObjects();

        if (stateManager.is(GameState.READY) || stateManager.is(GameState.GAMEOVER)) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString(panel.getMessage(), panel.getWidth() / 2 - 60, panel.getHeight() / 2);
        }

        for (GameObject obj : objects) {
            obj.draw(g);
        }

        g.setColor(Color.WHITE);
        if (stateManager.is(GameState.PAUSE)) {
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("PAUSED", panel.getWidth() / 2 - 40, panel.getHeight() / 2);
        } else {
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("P：PAUSE", panel.getWidth() / 2 + 110, 20);
        }
    }
}
