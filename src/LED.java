import javafx.scene.paint.Color;

/**
 * Created by edisongrauman on 12/20/17.
 */
public class LED {

    Color ledColor;

    public LED() {
        ledColor = Color.rgb(0,0,0);
    }

    void setColor(int r, int g, int b) {
        ledColor = Color.rgb(r,g,b);
    }
    Color getColor() {
        return ledColor;
    }

    void reset() {
        setColor(0,0,0);
    }



}
