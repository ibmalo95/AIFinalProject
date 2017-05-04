import java.awt.*;
import java.util.Scanner;

/**
 * Created by Ina on 4/8/17.
 */
public class HumanAgent implements Agent{

    private String mark;

    public HumanAgent(String mark) {
        this.mark = mark;
    }

    @Override
    public Point move(GameState state) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a row: ");
        int row = scanner.nextInt();
        System.out.println("Enter a col: ");
        int col = scanner.nextInt();
        Point move = new Point(row, col);

        // get user input
        while (!state.moves().contains(move)) {
            System.out.println("Not a move... enter new row: ");
            row = scanner.nextInt();
            System.out.println("Not a move... enter new col: ");
            col = scanner.nextInt();
            move = new Point(row, col);
        }

        return move;
    }

    public String getMark() {
        return mark;
    }
}
