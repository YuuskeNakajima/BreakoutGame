package breakout;

public class GameStateManager {
    private GameState currentState;

    public GameStateManager() {
        this.currentState = GameState.READY;
    }

    public GameState getState() {
        return currentState;
    }

    public boolean transitionTo(GameState nextState) {
        boolean allowed = false;

        // 状態遷移のルールチェック（例：不正な遷移を防ぐ）
        switch (currentState) {
            case READY -> allowed = nextState == GameState.RUNNING;
            case RUNNING -> allowed = nextState == GameState.PAUSE || nextState == GameState.GAMEOVER;
            case PAUSE -> allowed = nextState == GameState.RUNNING;
            case GAMEOVER -> allowed = nextState == GameState.READY || nextState == GameState.RUNNING;
            case CLEAR -> allowed = nextState == GameState.READY;
        }

        if (allowed) {
            currentState = nextState;
            return true;
        } else {
            System.err.println("⚠ 無効な状態遷移: " + currentState + " → " + nextState);
            return false;
        }
    }

    public boolean is(GameState state) {
        return currentState == state;
    }
}
