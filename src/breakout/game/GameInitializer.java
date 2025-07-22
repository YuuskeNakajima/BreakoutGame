package breakout.game;

import java.util.*;

import breakout.interfaces.GameObject;
import breakout.object.ball.Ball;
import breakout.object.block.Block;
import breakout.object.block.BlockManager;
import breakout.object.paddle.Paddle;
import breakout.ui.GamePanel;

public class GameInitializer {
    private static final int INITIAL_BALL_X = 180;
    private static final int INITIAL_BALL_Y = 160;

    public static List<GameObject> createObjects(GamePanel panel, double initialSpeed) {
        List<GameObject> objects = new ArrayList<>();
        List<Block> blocks = BlockManager.createStage(1);
        Ball ball = new Ball((double) INITIAL_BALL_X, (double) INITIAL_BALL_Y, initialSpeed);
        ball.setSpeedChangeListener(() -> panel.showSpeedUpNotice());
        ball.setScoreConsumer(score -> GameManager.addScore(score));
        Paddle paddle = new Paddle(160, 260, 400);

        objects.addAll(blocks);
        objects.add(paddle);
        objects.add(ball);

        panel.setBall(ball);
        panel.setPaddle(paddle);
        return objects;
    }
}
