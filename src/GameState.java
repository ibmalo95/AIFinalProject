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

    // get the values of each line
    public HashMap<String, Double[]> eval() {
        HashMap<String, Double[]> values = new HashMap<String, Double[]>();
        for (int r = 0; r < size; r++) {
            Double rowA = 0.0;
            Double colA = 0.0;
            Double rowO = 0.0;
            Double colO = 0.0;
            for (int c = 0; c < size; c++) {
                if (grid[r][c].equals("A"))
                    rowA++;
                if (grid[r][c].equals("O"))
                    rowO++;
                if (grid[c][r].equals("A"))
                    colA++;
                if (grid[c][r].equals("O"))
                    colO++;
            }
            values.put("row" + r, new Double[] {rowA, rowO});
            values.put("col" + r, new Double[]{colA, colO});
        }
        Double leftA = 0.0;
        Double rightA = 0.0;
        Double leftO = 0.0;
        Double rightO = 0.0;
        for (int r = 0; r < size; r++) {
            if (grid[r][r].equals("A"))
                rightA++;
            if (grid[r][r].equals("O"))
                rightO++;
            if (grid[r][size - r - 1].equals("A"))
                leftA++;
            if (grid[r][size - r - 1].equals("O"))
                leftO++;
        }
        values.put("diagonal1", new Double[] {rightA, rightO});
        values.put("diagonal2", new Double[] {leftA, leftO});

        return values;
    }

    // return the score
    public Double score() {
        HashMap<String, Double[]> values = eval();
        Double score = 0.0;

        for (String line: values.keySet()) {
            Double[] value = values.get(line);
            if (value[0] == 4.0)
                score += 100.0;
            else if (value[1] == 4.0)
                score += -100.0;
            else if (value[0] == 3.0)
                score += 50.0;
            else if (value[1] == 3.0)
                score += -50.0;
            else if (value[0] == 2.0)
                score += 10.0;
            else if (value[1] == 2.0)
                score += -10.0;
            else if (value[0] == 1.0)
                score += 1.0;
            else if (value[1] == 1.0)
                score += -1.0;
            else
                score += 0.0;
        }
        return score;
    }


    public Boolean terminal() {
        return wins("A") || wins("O") || moves().size() == 0;
    }

    // determines when to cutoff the tree
    public Boolean cutoff(int depth) {
        return wins("A") || wins("O") || moves().size() == 0 || depth > 2;
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
        return score();
    }

    public static void main(String[] args) {
        GameState game = new GameState(4);

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