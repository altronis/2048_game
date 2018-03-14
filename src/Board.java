import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private int status;

    Board(int status) {
        this.status = status;
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
        if (status == 0) {
            g.drawString("Join the numbers and get to the 2048 tile!", 40, 540);
            g.drawString("HOW TO PLAY: Use your arrow keys to move the tiles. When", 40, 590);
            g.drawString("two tiles with the same number touch, they merge into one!", 40, 615);
        }
        else if (status == 1) {
            g.drawString("Game Over!", 40, 540);
            g.drawString("You scored " +Integer.toString(Game.score) + " points.", 40, 590);
        }
        else
            g.drawString("You won!", 40, 540);

        g.fillRect(370, 515, 120, 40);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Tahoma", Font.BOLD, 18));
        g.drawString("New Game", 380, 540);
    }
}
