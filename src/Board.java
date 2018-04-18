import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    Board() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(187, 173, 160));
        g.fillRoundRect(40, 40, 450, 450, 4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Block b = new Block(Game.grid[i][j], j, i, Game.newX == i && Game.newY == j);
                b.paintComponent(g);
            }
        }
        g.setColor(new Color(119, 110, 101));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(Integer.toString(Game.score), 260, 30);
        g.setFont(new Font("Arial", Font.BOLD, 16));
    }
}
