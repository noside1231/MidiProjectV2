import javafx.scene.paint.Color;

import static jdk.nashorn.internal.objects.NativeMath.min;

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

    void addColor(Color c) {
        int r = (int)((c.getRed() + ledColor.getRed())*255);
        int g = (int)((c.getGreen() + ledColor.getGreen())*255);
        int b = (int)((c.getBlue() + ledColor.getBlue())*255);

        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;

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

    public void setSelected(boolean t) {
        selected = t;
    }




}
