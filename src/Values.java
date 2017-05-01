import java.awt.*;

/**
 * Created by Ina on 4/10/17.
 */
public class Values {

    Double value;
    Point move;

    public Values(Double value, Point move) {
        this.value = value;
        this.move = move;
    }

    public Double getValue() {
        return value;
    }

    public Point getMove() {
        return move;
    }

    public void setValue(Double next_value) {
        value = next_value;
    }

    public void setMove(Point next_move) {
        move = next_move;
    }
}
