import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Ina on 4/8/17.
 */
public class MaxAgent implements Agent {

    private String mark;

    public MaxAgent() {
        this.mark = "A";
    }

    @Override
    // Return where to put a mark
    public Point move(GameState state) {

        System.out.println("Thinking...");

        Values next_move = max_value(state, -1000000, 1000000);
        return next_move.getMove();
    }

    // Minimax with alpha beta pruning
    public Values max_value(GameState state, int alpha, int beta) {

        // if the state is terminal return the utility
        if (state.cutoff(state.getHeight())) {
            // return the estimate of what the utility would be
            return new Values(state.utility(state), null);
        }
        int height = state.getHeight() + 1;

        // Alpha-beta pruning
        Values v = new Values(-1000000, null);

        for (Point move: state.moves()) {
            GameState child = state.neighbor(move, mark);
            child.setHeight(height);

            // check to see what alpha and beta are supposed to start at
            Values next = new MinAgent().min_value(child, alpha, beta); // (value, next_move)
            Values value = new Values(next.getValue(), move); // (value, move)

            if (value.getValue() > v.getValue()) {
                v.setValue(value.getValue());
                v.setMove(value.getMove());
            }
            else if (v.getValue() >= beta) {
                return v;
            }

            else if (v.getValue() > alpha)
                alpha = v.getValue();
        }
        return v;
    }

    public String getMark() {
        return mark;
    }
}
