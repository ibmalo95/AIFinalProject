import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ina on 4/8/17.
 */
public class GameState {

    private int size;
    private String[][] grid;
    private int height;

    public GameState(int size) {
        this.size = size;
        this.height = 0;

        // Generate random row number and column numbers that should be blocks
//        ArrayList<Point> blocks = new ArrayList<Point>(); // will sometimes get the same points
//        Random randomizer = new Random();
//        // Have this number be proportional to the size of the board
//        int number = 3;
//        for (int i = 0; i < number; i++) {
//            int row = randomizer.nextInt(size);
//            int col = randomizer.nextInt(size);
//            blocks.add(new Point(row, col));
//        }
//        System.out.println(blocks.toString());

        grid = new String[size][size];
        for (int r = 0; r < size; r++) {
            String[] row = new String[size];
            grid[r] = row;
            for (int c = 0; c < size; c++) {
                row[c] = "-";
            }
        }
    }

    // TODO: Figure out when this should be called
    public void increment() {
        height++;
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
        GameState neighbor = new GameState(this.size);
        neighbor.height = this.height;

        for (int r = 0; r < this.size; r++) {
            for (int c = 0; c < this.size; c++) {
                if (this.grid[r][c].equals("A"))
                    neighbor.grid[r][c] = "A";
                else if (this.grid[r][c].equals("O"))
                    neighbor.grid[r][c] = "O";
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
            boolean complete = true;
            for (int c = 0; c < size; c++) {
                if (!grid[r][c].equals(mark))
                    complete = false;
            }
            if (complete)
                return true;
        }
        return false;
    }

    public Boolean cols(String mark) {
        for (int c = 0; c < size; c++) {
            boolean complete = true;
            for (int r = 0; r < size; r++) {
                if (!grid[r][c].equals(mark))
                    complete = false;
            }
            if (complete)
                return true;
        }
        return false;
    }

    public Boolean diagonal(String mark) {
        boolean right = true;
        boolean left = true;
        for (int r = 0; r < size; r++) {
            if (!grid[r][r].equals(mark))
                right = false;
            if (!grid[r][size - r - 1].equals(mark))
                left = false;
        }
        return right || left;
    }

    public Double eval(String mark) {
        Double number = 0.0;
        for (int r = 0; r < size; r++) {
            int row = 0;
            int col = 0;
            for (int c = 0; c < size; c++) {
                // by rows
                if (grid[r][c].equals("-") || grid[r][c].equals(mark)) {
                    row++;
                }
                //  by columns
                if (grid[c][r].equals("-") || grid[c][r].equals(mark)) {
                    col++;
                }
            }
            if (row == size)
                number++;
            if (col == size)
                number++;
        }
        int left = 0;
        int right = 0;
        for (int r = 0; r < size; r++) {

            if (grid[r][r].equals("-") || grid[r][r].equals(mark)) {
                right++;
            }
            if (grid[r][size-r-1].equals("-") || grid[r][size-r-1].equals(mark))
                left++;
        }
        if (right == size)
            number++;
        if (left == size)
            number++;

        return number;
    }


    public Boolean terminal() {
        return wins("A") || wins("O") || moves().size() == 0;
    }

    // determines when to cutoff the tree
    public Boolean cutoff(int depth) {
        return wins("A") || wins("O") || moves().size() == 0 || depth > 4;
    }

    // returns utility
    public Double utility() {
        if (wins("A"))
            return 1.0;
        else if (wins("O"))
            return -1.0;
        else if (moves().size() == 0)
            return 0.0;
        else
            return null;
    }

    // returns the estimated utility
    public Double utility(String mark) {
        if (wins("A"))
            return 1.0;

        else if (wins("O"))
            return -1.0;

        else if (moves().size() == 0)
            return 0.0;

        else if (mark.equals("A"))
            return (eval(mark) - eval("O"));

        else
            return (eval(mark) - eval("A"));
    }

    public static void main(String[] args) {
        GameState game = new GameState(4);
//        game.grid[0][0] = "O";
//        game.grid[3][0] = "O";
//        game.grid[1][1] = "O";
//        game.grid[2][0] = "O";
//        game.grid[2][2] = "A";
//        game.grid[2][1] = "A";
//        game.grid[3][3] = "A";
//        game.grid[1][3] = "A";

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