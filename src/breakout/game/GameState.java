package breakout.game;

public enum GameState {
    READY,      // 開始待ち（"Enterキーで開始"）
    RUNNING,    // プレイ中
    PAUSE,     // 一時停止中
    GAMEOVER,   // ゲームオーバー状態（"Enterキーで再開"）
    CLEAR       // クリア画面へ遷移（GamePanelでは使用しない）
}
