import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.KeyEvent.*;

class Game extends JFrame {
    static int[][] grid;
    static int score;
    static int newX;
    static int newY;
    private KeyAdapter keyAdapter;

    private Game() {
        grid = new int[4][4];
        grid[0][0] = 2;
        score = 0;
        setTitle("2048");
        setSize(550, 700);
        setLocation(200, 200);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        keyAdapter = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                move(e);
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        addKeyListener(keyAdapter);

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int xpos = e.getX() - 10;
                int ypos = e.getY() - 30;
                if (e.getButton() == MouseEvent.BUTTON1 && Math.abs(430 - xpos) <= 60 && Math.abs(535 - ypos) < 20) {
                    newGame();
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void move(KeyEvent e) {
        boolean changed = false;
        int key = e.getKeyCode();
        switch (key) {
            case VK_RIGHT:
                changed = moveRight();
                break;
            case VK_LEFT:
                changed = moveLeft();
                break;
            case VK_UP:
                changed = moveUp();
                break;
            case VK_DOWN:
                changed = moveDown();
                break;
        }
        if (changed)
            generate();
        else {
            newX = -1;
            newY = -1;
        }
        refresh();
    }

    private void rotate(int degrees) {
        int n = degrees / 90;
        int[][] result = new int[4][4];
        for (int count = 0; count < n; count++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    result[i][j] = grid[j][3 - i];
                }
            }
            for (int i = 0; i < 4; i++) {
                System.arraycopy(result[i], 0, grid[i], 0, 4);
            }
        }
    }

    private boolean moveRight() {
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >= 1; j--) {
                if (grid[i][j] == 0) {
                    for (int k = j - 1; k >= 0; k--) {
                        if (grid[i][k] != 0) {
                            grid[i][j] = grid[i][k];
                            grid[i][k] = 0;
                            changed = true;
                            break;
                        }
                    }
                }
                for (int k = j - 1; k >= 0; k--) {
                    if (grid[i][k] == grid[i][j] & grid[i][k] != 0) {
                        score += grid[i][j] / 2;
                        grid[i][j] *= 2;
                        grid[i][k] = 0;
                        changed = true;
                        break;
                    }
                    if (grid[i][k] != grid[i][j] && grid[i][k] != 0) {
                        grid[i][j - 1] = grid[i][k];
                        if (k != j - 1) {
                            grid[i][k] = 0;
                            changed = true;
                        }
                        break;
                    }
                }
            }
        }
        return changed;
    }

    private boolean moveLeft() {
        boolean changed;
        rotate(180);
        changed = moveRight();
        rotate(180);
        return changed;
    }

    private boolean moveUp() {
        boolean changed;
        rotate(270);
        changed = moveRight();
        rotate(90);
        return changed;
    }

    private boolean moveDown() {
        boolean changed;
        rotate(90);
        changed = moveRight();
        rotate(270);
        return changed;
    }

    private void generate() {
        while (true) {
            int pos = (int) (Math.random() * 16);
            int x = pos / 4;
            int y = pos % 4;
            if (grid[x][y] == 0) {
                int rand = (int) (Math.random() * 5);
                if (rand == 0)
                    grid[x][y] = 4;
                else
                    grid[x][y] = 2;
                newX = x;
                newY = y;
                break;
            }
        }
    }

    private boolean gameLost() {
        boolean lost = true;
        for (int[] i : grid) {
            for (int j : i) {
                if (j == 0)
                    lost = false;
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == grid[i][j + 1])
                    lost = false;
            }
        }
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 3; i++) {
                if (grid[i][j] == grid[i + 1][j])
                    lost = false;
            }
        }
        return lost;
    }

    private boolean gameWon() {
        for (int[] i : grid) {
            for (int j : i) {
                if (j == 2048)
                    return true;
            }
        }
        return false;
    }

    private void newGame() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid[i][j] = 0;
        grid[0][0] = 2;
        score = 0;
        if (gameLost() || gameWon())
            addKeyListener(keyAdapter);
        newX = -1;
        newY = -1;
        refresh();
    }

    private void refresh() {
        int status = 0;
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.revalidate();
        if (gameLost()) {
            status = 1;
            removeKeyListener(keyAdapter);
        } else if (gameWon()) {
            status = 2;
            removeKeyListener(keyAdapter);
        }
        contentPane.add(new Board(status));
    }

    public static void main(String[] args) {
        Game f = new Game();
        f.refresh();
        f.show();
    }
}
