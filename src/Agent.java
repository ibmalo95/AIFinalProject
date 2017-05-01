import java.awt.*;

/**
 * Created by Ina on 4/8/17.
 */
public interface Agent {

    public Point move(GameState state);
    public String getMark();
}
