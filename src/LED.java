import javafx.scene.paint.Color;

/**
 * Created by edisongrauman on 12/20/17.
 */
public class LED {

    Color ledColor;
    boolean selected;

    public LED() {
        ledColor = Color.rgb(0,0,0);
        selected = false;
    }

    void setColor(int r, int g, int b) {
        ledColor = Color.rgb(r,g,b);
    }

    void setColor(Color c) {
        ledColor = c;
    }
    Color getLED() {
        return ledColor;
    }

    void toggleSelected() {
        selected = !selected;
    }

    boolean getSelected() {
        return selected;
    }

    void reset() {
        resetColor();
        resetSelected();
    }

    void resetSelected() {
        selected = false;
    }

    void resetColor() {
        ledColor = Color.BLACK;
    }



}
