import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;

class Game extends JFrame {
    static int[][] grid; // The 4x4 grid of the tiles
    static int score; // Current game score
    static int newX; // Coordinates of the newly spawned tile
    static int newY;
    private int status; // Status of the game. 0 = normal, 1 = Game over, 2 = Game won
    private KeyAdapter keyAdapter;

    private Game() {
        setFocusable(true);

        grid = new int[4][4]; // Initialize the grid
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
    }

    private void move(KeyEvent e) {
        boolean changed = false; // If the board is changed after pressing the key

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

        if (changed) // If board is changed, generate a new tile
            generate();
        else {
            newX = -1;
            newY = -1;
        }
        refresh();
    }

    private void rotate(int degrees) { // Rotate the entire grid by increments of 90 degrees
        int n = degrees / 90;
        int[][] result = new int[4][4];
        for (int count = 0; count < n; count++) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    result[i][j] = grid[j][3 - i];

            for (int i = 0; i < 4; i++)
                System.arraycopy(result[i], 0, grid[i], 0, 4);
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

    private void generate() { // Generate a new tile
        while (true) {
            int pos = (int) (Math.random() * 16); // Pick a random position
            int x = pos / 4;
            int y = pos % 4;

            if (grid[x][y] == 0) {
                int rand = (int) (Math.random() * 5); // Generate a 2 with 80% chance, and generate a 4 with 20% chance
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

    private boolean gameLost() { // Game is lost if there are no blank spaces and no two adjacent tiles are the same number
        boolean lost = true;
        for (int[] i : grid) // Check if there are blank spaces
            for (int j : i)
                if (j == 0)
                    lost = false;

        for (int i = 0; i < 4; i++) // Check the rows
            for (int j = 0; j < 3; j++)
                if (grid[i][j] == grid[i][j + 1])
                    lost = false;

        for (int j = 0; j < 4; j++) // Check the columns
            for (int i = 0; i < 3; i++)
                if (grid[i][j] == grid[i + 1][j])
                    lost = false;

        return lost;
    }

    private boolean gameWon() { // Game is won if 2048 tile is made
        for (int[] i : grid)
            for (int j : i)
                if (j == 2048)
                    return true;
        return false;
    }

    private void newGame() { // Re-initialize the board
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid[i][j] = 0;
        grid[0][0] = 2;
        score = 0;

        if (status != 0) // If game was lost or won, re-add the keyListener
            addKeyListener(keyAdapter);
        newX = -1;
        newY = -1;
        refresh();
    }

    private void refresh() {
        status = 0;
        Container contentPane = getContentPane();
        contentPane.removeAll();
        contentPane.revalidate();

        if (gameLost()) { // If game is lost or won, the program should not respond to moves
            status = 1;
            removeKeyListener(keyAdapter);
        } else if (gameWon()) {
            status = 2;
            removeKeyListener(keyAdapter);
        }

        Font font = new Font("Arial", Font.BOLD, 14);

        JButton newGame = new JButton("New Game"); // New Game button
        newGame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newGame();
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
        newGame.setFont(font);

        Board board = new Board();
        JPanel instructions = new JPanel();
        instructions.setLayout(new BoxLayout(instructions, BoxLayout.PAGE_AXIS));

        JLabel i1 = new JLabel("HOW TO PLAY: Use your arrow keys to move the tiles. When");
        JLabel i2 = new JLabel("two tiles with the same number touch, they merge into one!");
        i1.setFont(font);
        i2.setFont(font);
        instructions.add(i1);
        instructions.add(i2);

        String statusDisplay;
        if (status == 0)
            statusDisplay = "Join the numbers and get to the 2048 tile!";
        else if (status == 1)
            statusDisplay = "Game Over! You scored " + score + " points.";
        else
            statusDisplay = "You won!";

        JLabel statusText = new JLabel(statusDisplay);
        statusText.setFont(font);
        JPanel text = new JPanel();
        text.add(statusText);

        board.setPreferredSize(new Dimension(550, 500));
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 500;
        c.ipadx = 550;
        c.gridheight = 100;
        c.gridwidth = 110;
        contentPane.add(board, c);

        c.gridy = 101;
        c.ipadx = 90;
        c.ipady = 10;
        c.gridheight = 2;
        c.gridwidth = 5;
        contentPane.add(text, c);
        this.setVisible(true);

        c.gridx = 5;
        c.ipadx = 2;
        c.insets = new Insets(0, 380 - text.getWidth(),  0, 0);
        contentPane.add(newGame, c);

        c.gridx = 1;
        c.gridy = 103;
        c.ipadx = 0;
        c.insets = new Insets(15, 55,  0, 0);
        c.gridwidth = 20;
        c.gridheight = 3;
        contentPane.add(instructions, c);

        this.requestFocus();
    }

    public static void main(String[] args) {
        Game f = new Game();
        f.refresh();
        f.show();
    }
}
