package breakout;

public class GameManager {
    private static int totalScore = 0;

    public static void addScore(int s) {
        totalScore += s;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static void resetScore() {
        totalScore = 0;
    }
}