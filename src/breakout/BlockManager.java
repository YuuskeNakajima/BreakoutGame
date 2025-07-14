package breakout;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class BlockManager {

    public static List<Block> createStage(int stageNumber) {
        List<Block> blocks = new ArrayList<>();

        int blockWidth = 50;
        int blockHeight = 20;
        int rows = 3;
        int cols = 7;
        int startX = 20;
        int startY = 40;

        // ステージ番号によって配置変化も将来的に実装可能
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (blockWidth + 5);
                int y = startY + row * (blockHeight + 5);
                blocks.add(new Block(x, y, blockWidth, blockHeight, Color.CYAN));
            }
        }
        return blocks;
    }
}
