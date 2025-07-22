package breakout;

import java.util.*;

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
