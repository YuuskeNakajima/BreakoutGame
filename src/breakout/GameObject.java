package breakout;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface GameObject {
    void update();               // ゲームオブジェクトの更新処理
    void draw(Graphics g);            // 描画処理
    Rectangle getBounds();           // 衝突判定のための矩形
    boolean isActive();               // 表示・更新対象かどうか
}