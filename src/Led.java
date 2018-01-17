import javafx.scene.paint.Color;

/**
 * Created by edisongrauman on 12/20/17.
 */
public class Led {

    Color ledColor;
    boolean selected;

    public Led() {
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

    Color getLEDColor() {
        return ledColor;
    }

    void resetSelected() {
        selected = false;
    }

    void resetColor() {
        ledColor = Color.BLACK;
    }

    String getLEDString() {
        return String.format( "#%02X%02X%02X",
                (int)( ledColor.getRed() * 255 ),
                (int)( ledColor.getGreen() * 255 ),
                (int)( ledColor.getBlue() * 255 ) );
    }




}
