import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Ina on 4/8/17.
 */
public class GameState {

    private int size;
    private String[][] grid;
    private int height;
    private int win;
    private int maxDepth;
    private int blocked;

    public GameState(int size, boolean first) {
        this.size = size;
        this.height = 1;
        this.win = 3;
        if (size <= 5) {
            this.maxDepth = 6;
            this.blocked = 2;
        }
        else if (size <= 9) {
            this.maxDepth = 4;
            blocked = 5;
        }
        else {
            this.maxDepth = 2;
            blocked = 5;
        }
        ArrayList<Point> blocks = new ArrayList<Point>();
        if (first) {
            // Generate random row number and column numbers that should be blocks
            if (height == 1) {
                Random randomizer = new Random();

                for (int i = 0; i < blocked; i++) {
                    int row = randomizer.nextInt(size);
                    int col = randomizer.nextInt(size);
                    Point block = new Point(row, col);

                    while (blocks.contains(block)) {
                        row = randomizer.nextInt(size);
                        col = randomizer.nextInt(size);
                        block = new Point(row, col);
                    }
                    blocks.add(block);
                }
            }
        }


        grid = new String[size][size];
        for (int r = 0; r < size; r++) {
            String[] row = new String[size];
            grid[r] = row;
            for (int c = 0; c < size; c++) {
                Point x = new Point(r,c);
                if (blocks.contains(x))
                    row[c] = "X";
                else
                    row[c] = "-";
            }
        }
    }

    public void setHeight(int nextHeight) {
        height = nextHeight;
    }

    public int getHeight() {
        return height;
    }

    //  print the grid
    public void display() {
        for (String[] row : grid) {
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    // return the (r,c) of the empty grid spaces
    public ArrayList<Point> moves() {
        ArrayList<Point> moves = new ArrayList<Point>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].equals("-"))
                    moves.add(new Point(r,c));
            }
        }
        return moves;
    }

    // Return a state like this one but with this move made.
    // Make sure there is no aliasing between the two states.
    public GameState neighbor(Point move, String mark) {

        // deep copy of the current state
        GameState neighbor = new GameState(this.size, false);
        neighbor.height = this.height;

        for (int r = 0; r < this.size; r++) {
            for (int c = 0; c < this.size; c++) {
                String gridPoint = this.grid[r][c];
                if (gridPoint.equals("A"))
                    neighbor.grid[r][c] = "A";
                else if (gridPoint.equals("O"))
                    neighbor.grid[r][c] = "O";
                else if (gridPoint.equals("X"))
                    neighbor.grid[r][c] = "X";
            }
        }

        Double x = move.getX(); // row
        int row = x.intValue();
        Double y = move.getY(); // col
        int col = y.intValue();
        // mark that spot in the grid with mark
        neighbor.grid[row][col] = mark;
        return neighbor;
    }


    public Boolean wins(String mark) {
        return rows(mark) || cols(mark) || diagonal(mark);
    }

    public Boolean rows(String mark) {
        for (int r = 0; r < size; r++) {
            int start = 0;
            for (int c = 0; c < size; c++) {
                if (!grid[r][c].equals(mark))
                    start = 0;
                else
                    start++;
                if (start == win)
                    return true;
            }
        }
        return false;
    }

    public Boolean cols(String mark) {
        for (int c = 0; c < size; c++) {
            int start = 0;
            boolean complete = true;
            for (int r = 0; r < size; r++) {
                if (!grid[r][c].equals(mark))
                    start = 0;
                else
                    start++;
                if (start == win)
                    return true;
            }
        }
        return false;
    }

    public Boolean diagonal(String mark) {
        int right = 0;
        int left = 0;
        for (int r = 0; r < size; r++) {
            if (!grid[r][r].equals(mark))
                right = 0;
            else
                right++;
            if (!grid[r][size - r - 1].equals(mark))
                left = 0;
            else
                right++;

            if (right == win)
                return true;
            else if (left == win)
                return true;
        }
        return false;
    }

    // get the values of each line
    public HashMap<String, int[]> eval(GameState state) {
        HashMap<String, int[]> values = new HashMap<String, int[]>();
        for (int r = 0; r < size; r++) {
            int rowA = 0;
            int colA = 0;
            int rowO = 0;
            int colO = 0;
            int rowX = 0;
            int colX = 0;
            for (int c = 0; c < size; c++) {
                String row = state.grid[r][c];
                String col = state.grid[c][r];

                if (row.equals("A") && rowO == 0)
                    rowA++;
                else if (row.equals("O"))
                    rowO++;
                else if (row.equals("X"))
                    rowX++;
                if (col.equals("A"))
                    colA++;
                else if (col.equals("O"))
                    colO++;
                else if (col.equals("X"))
                    colX++;
            }

            values.put("row" + r, new int[]{rowA, rowO, rowX});

            values.put("col" + r, new int[]{colA, colO, colX});
        }
        int leftA = 0;
        int rightA = 0;
        int leftO = 0;
        int rightO = 0;
        int leftX = 0;
        int rightX = 0;
        for (int r = 0; r < size; r++) {
            String right = state.grid[r][r];
            String left = state.grid[r][size - r - 1];

            if (right.equals("A"))
                rightA++;
            else if (right.equals("O"))
                rightO++;
            else if (right.equals("X"))
                rightX++;

            if (left.equals("A"))
                leftA++;
            else if (left.equals("O"))
                leftO++;
            else if (left.equals("X"))
                leftX++;
        }

        values.put("diagonal1", new int[]{leftA, leftO, leftX});
        values.put("diagonal2", new int[]{rightA, rightO, rightX});

        return values;
    }

    // return the score
    public int score(GameState state) {
        HashMap<String, int[]> values = eval(state);
        int score = 0;

        for (String line: values.keySet()) {
            int[] value = values.get(line);
            int lineA = value[0];
            int lineO = value[1];
            int lineX = value[2];

            if (lineO == 0 && lineA == 0) {
                // do nothing
            }
            else if (lineA >= 3) {
                score += 50;
            }
            else if (lineA == 2) {
                score += 10;
            }
            else if (lineA == 1) {
                score += 1;
            }
            else if (lineO >= 3) {
                score += -50;
            }
            else if (lineO == 2) {
                score += -10;
            }
            else if (lineO == 1) {
                score += - 1;
            }
        }

        return score;
    }

    // determines when to cutoff the tree
    public Boolean cutoff(int depth) {
        return wins("A") || wins("O") || moves().size() == 0 || depth > maxDepth;
    }


    // returns the estimated utility
    public int utility(GameState state) {
        return score(state);
    }

    public static void main(String[] args) {
        int gameSize = 6;
        GameState game = new GameState(gameSize, true);

        game.display();

        Agent[] agents = {new MaxAgent(), new HumanAgent("O")};
        for (int i = 0; i < 16; i++) {
            Agent agent = agents[i%2];
            Point move = agent.move(game);

            game = game.neighbor(move, agent.getMark());
            game.display();

            // if an agent wins break out of loop
            // wont recognize a tie when we start with a grid that is almost done
            if (game.wins(agent.getMark())) {
                break;
            }
        }
    }
}