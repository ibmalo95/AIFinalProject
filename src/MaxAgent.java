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
        // check to see what alpha and beta are supposed to start at
//        Values next_move = max_value(state);

        Values next_move = max_value(state, -10000.0, 10000.0);
        return next_move.getMove();
    }

    // Minimax without pruning
    public Values max_value(GameState state) {

        // if the state is terminal return the utility
        if (state.cutoff(state.getHeight())) {
            // return the estimate of what the utility would be
            return new Values(state.utility(), null);
        }

        ArrayList<Values> values = new ArrayList<Values>();
        for (Point move: state.moves()) {
            GameState child = state.neighbor(move, mark);
//            child.display();
            Values next = new MinAgent().min_value(child); // (value, next_move)
//            System.out.println(next.getValue());
            Values value = new Values(next.getValue(), move); // (value, move)
            values.add(value);
        }

        // get the maximum value
        Double max = values.get(0).getValue();
        int max_index = 0;
        for (int i = 1; i < values.size(); i++) {
            Values next = values.get(i);
            if (next.getValue() >= max) {
                max = next.getValue();
                max_index = i;
            }
        }
        // return max (value, move)
        return values.get(max_index);
    }

    // Minimax with alpha beta pruning
    public Values max_value(GameState state, Double alpha, Double beta) {
        //state.incrementHeight();
        // if the state is terminal return the utility
        if (state.cutoff(state.getHeight())) {
            // return the estimate of what the utility would be
            return new Values(state.utility(mark), null);
        }

        // Alpha-beta pruning
        Values v = new Values(-10000.0, null);
        boolean marked = false;
        for (Point move: state.moves()) {
            GameState child = state.neighbor(move, mark);

            // check to see what alpha and beta are supposed to start at
            if (!marked) {
                child.increment();
                marked = true;
            }
            Values next = new MinAgent().min_value(child, alpha, beta); // (value, next_move)

            Values value = new Values(next.getValue(), move); // (value, move)

            if (value.getValue() > v.getValue()) {
                v.setValue(value.getValue());
                v.setMove(value.getMove());
            }
            if (v.getValue() >= beta) {
                //v.setMove(move);
                state.increment();
                return v;
            }

            if (v.getValue() > alpha)
                alpha = v.getValue();
        }
        state.increment();
        return v;
    }

    public String getMark() {
        return mark;
    }
}
