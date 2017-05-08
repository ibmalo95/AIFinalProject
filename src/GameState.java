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

    public GameState(int size) {
        this.size = size;
        this.height = 1;
        this.win = size - 2;
        maxDepth = 5;

        grid = new String[size][size];
        for (int r = 0; r < size; r++) {
            String[] row = new String[size];
            grid[r] = row;
            for (int c = 0; c < size; c++) {
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

    // return the row and column of the empty grid spaces
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
        GameState neighbor = new GameState(this.size);
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
        return rows(mark) || cols(mark);
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

    // Count the number of A's and O's in each row and column
    public HashMap<String, int[]> eval(GameState state) {
        HashMap<String, int[]> values = new HashMap<String, int[]>();
        int count = 0;

        for (int r = 0; r < size; r++) {
            int rowA = 0;
            int colA = 0;
            int rowO = 0;
            int colO = 0;
            for (int c = 0; c < size; c++) {
                String row = state.grid[r][c];
                String col = state.grid[c][r];
                if (row.equals("A"))
                    rowA++;
                else if (row.equals("O"))
                    rowO++;

                if (col.equals("A"))
                    colA++;
                else if (col.equals("O"))
                    colO++;
            }
            values.put("row" + count, new int[]{rowA, rowO});
            values.put("col" + count, new int[]{colA, colO});
            count++;
        }

        return values;
    }

    // Evaluate the score of each line and sum them together
    public int score(GameState state) {
        HashMap<String, int[]> values = eval(state);
        int score = 0;

        for (String line: values.keySet()) {
            int[] value = values.get(line);
            int lineA = value[0];
            int lineO = value[1];

            if (lineO == 0 && lineA == 0) {
                // do nothing
            }
            else if (lineO == 0) {
                if (lineA == 5)
                    score += 100;
                else if (lineA == 4)
                    score += 70;
                else if (lineA == 3)
                    score += 50;
                else if (lineA == 2)
                    score += 30;
                else if (lineA == 1)
                    score += 1;
            }
            else if (lineA == 0 || lineA == 1) {
                if (lineO == 5)
                    score += -100;
                else if (lineO == 4)
                    score += -70;
                else if (lineO == 3)
                    score += -50;
                else if (lineO == 2)
                    score += -30;
                else if (lineO == 1)
                    score += -1;
            }
            else if (lineO == 1) {
                if (lineA == 5)
                    score += 70;
                else if (lineA == 4)
                    score += 50;
                else if (lineA == 3)
                    score += 30;
                else if (lineA == 2)
                    score += 10;
                else if (lineA == 1)
                    score += 1;
            }

            else if (lineA == 1) {
                if (lineO == 5)
                    score += -70;
                else if (lineO == 4)
                    score += -50;
                else if (lineO == 3)
                    score += -30;
                else if (lineO == 2)
                    score += -10;
                else if (lineO == 1)
                    score += -1;
            }
            else if (lineO == 2) {
                if (lineA == 5)
                    score += 50;
                else if (lineA == 4)
                    score += 30;
                else if (lineA == 3)
                    score += 10;
                else if (lineA == 2)
                    score += 5;
                else if (lineA == 1)
                    score += 1;
            }

            else if (lineA == 2) {
                if (lineO == 5)
                    score += -50;
                else if (lineO == 4)
                    score += -30;
                else if (lineO == 3)
                    score += -10;
                else if (lineO == 2)
                    score += -5;
                else if (lineO == 1)
                    score += -1;
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
        int gameSize = 7;
        GameState game = new GameState(gameSize);

        game.display();

        Agent[] agents = {new MaxAgent(), new HumanAgent("O")};
        for (int i = 0; i < gameSize * gameSize; i++) {
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