import javafx.scene.paint.Color;

/**
 * Created by edisongrauman on 12/20/17.
 */

public class LEDMatrix {

    Led[][] leds;

    static int defaultStrips = 5;
    static int defaultLedsPerStrip = 30;

    int strips;
    int ledsPerStrip;

    public LEDMatrix() {
        this(defaultStrips, defaultLedsPerStrip);
    }

    public LEDMatrix(int s, int lps) {
        strips = s;
        ledsPerStrip = lps;

        leds = new Led[ledsPerStrip][strips];

        for (int x = 0; x < ledsPerStrip; x++) {
            for (int y = 0; y < strips; y++) {
                leds[x][y]  = new Led();
            }
        }

    }

    void setLED(int x, int y, Color c) {
        leds[x][y].setColor(c);
    }
    Color getLED(int x, int y) {
        return leds[x][y].getLED();
    }

    String getLEDString(int x, int y) {
        return leds[x][y].getLEDString();
    }
    boolean getLEDSelected(int x, int y) {
        return leds[x][y].getSelected();
    }

    void toggleSelected(int x, int y) {
        leds[x][y].toggleSelected();
    }

    void reset() {
        for (int x = 0; x < ledsPerStrip; x++) {
            for (int y = 0; y < strips; y++) {
                leds[x][y].reset();
            }
        }
    }

    public Led[][] getLEDS() {
        return leds;
    }

    public int getStrips() {
        return strips;
    }

    public int getLedsPerStrip() {
        return ledsPerStrip;
    }

    public void setSelected(int x, int y, boolean t) {
        leds[x][y].setSelected(t);
    }

}
