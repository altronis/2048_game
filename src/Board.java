import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Game game;

    Board(Game game) {
        this.game = game;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(187, 173, 160));
        g.fillRoundRect(40, 40, 450, 450, 4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Block b = new Block(game.grid[i][j], j, i, game.newX == i && game.newY == j);
                b.paintComponent(g);
            }
        }
        g.setColor(new Color(119, 110, 101));
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString(Integer.toString(game.score), 260, 30);
        g.setFont(new Font("Arial", Font.BOLD, 16));
    }
}
