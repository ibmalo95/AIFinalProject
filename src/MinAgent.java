import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Ina on 4/8/17.
 */
public class MinAgent implements Agent {

    private String mark;

    public MinAgent() {
        this.mark = "O";
    }

    // Return where to put a mark
    @Override
    public Point move(GameState state) {

        System.out.println("Thinking...");
        // check to see what alpha and beta are supposed to start at

        Values next_move = min_value(state, -1000000, 1000000);
        return next_move.getMove();
    }

    // Minimax with alpha-beta pruning
    public Values min_value(GameState state, int alpha, int beta) {

        if (state.cutoff(state.getHeight())) {
            // return the estimate of what the utility would be
            return new Values(state.utility(state), null);
        }

        int height = state.getHeight() + 1;

        // Alpha-beta pruning
        // very small negative number
        Values v = new Values(1000000, null);
        for (Point move: state.moves()) {
            GameState child = state.neighbor(move, mark);
            child.setHeight(height);

            // check to see what alpha and beta are supposed to start at
            Values next = new MaxAgent().max_value(child, alpha, beta); //(value, next_move)
            Values value = new Values(next.getValue(), move); // (value, move)

            // if the value is less than v
            if (value.getValue() < v.getValue()) {
                v.setValue(value.getValue());
                v.setMove(move);
            }

            else if (v.getValue() <= alpha) {
                return v;
            }

            else if (v.getValue() < beta)
                beta = v.getValue();
        }
        return v;
    }

    public String getMark() {
        return mark;
    }
}
