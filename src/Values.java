import java.awt.*;

/**
 * Created by Ina on 4/10/17.
 */
public class Values {

    int value;
    Point move;

    public Values(int value, Point move) {
        this.value = value;
        this.move = move;
    }

    public int getValue() {
        return value;
    }

    public Point getMove() {
        return move;
    }

    public void setValue(int next_value) {
        value = next_value;
    }

    public void setMove(Point next_move) {
        move = next_move;
    }
}
