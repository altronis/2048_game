import javax.swing.*;
import java.awt.*;

public class Block extends JComponent {
    private int value;
    private int x;
    private int y;
    private Color color;
    private boolean isNew;

    Block(int value, int x, int y, boolean isNew) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.isNew = isNew;
        switch (value) {
            case 0:
                color = new Color(205, 192, 180);
                break;
            case 2:
                color = new Color(238, 228, 218);
                break;
            case 4:
                color = new Color(237, 224, 200);
                break;
            case 8:
                color = new Color(242, 177, 121);
                break;
            case 16:
                color = new Color(245, 149, 99);
                break;
            case 32:
                color = new Color(246, 124, 95);
                break;
            case 64:
                color = new Color(246, 94, 59);
                break;
            case 128:
                color = new Color(237, 207, 114);
                break;
            case 256:
                color = new Color(237, 204, 97);
                break;
            default:
                color = new Color(237, 200, 80);
                break;
        }
    }

    protected void paintComponent(Graphics g) {
        final int LENGTH = 100;
        final int GAP = 10;
        int xpos = 50 + (LENGTH + GAP) * x;
        int ypos = 50 + (LENGTH + GAP) * y;
        int size = Integer.toString(value).length() - 1;
        if (isNew) {
            g.setColor(new Color(237, 204, 97));
            g.fillRect(xpos, ypos, LENGTH, LENGTH);
        }
        g.setColor(color);
        if (isNew)
            g.fillRect(xpos + GAP, ypos + GAP, LENGTH - 2 * GAP, LENGTH - 2 * GAP);
        else
            g.fillRect(xpos, ypos, LENGTH, LENGTH);

        g.setFont(new Font("Tahoma", Font.BOLD, 45 - 4 * size));

        // Center the number inside the tile
        FontMetrics fm = g.getFontMetrics();
        xpos += (LENGTH - fm.stringWidth(Integer.toString(value))) / 2;
        ypos += ((LENGTH - fm.getHeight()) / 2) + fm.getAscent();
        if (value > 4)
            g.setColor(Color.WHITE);
        else
            g.setColor(new Color(119, 110, 101));
        if (value > 0)
            g.drawString(String.valueOf(value), xpos, ypos);
    }
}
