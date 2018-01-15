/**
 * Created by edisongrauman on 12/20/17.
 */
public class LEDMatrix {

    LED[][] leds;

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

        leds = new LED[ledsPerStrip][strips];

        for (int x = 0; x < ledsPerStrip; x++) {
            for (int y = 0; y < strips; y++) {
                leds[x][y]  = new LED();
            }
        }

    }

    public int getStrips() {
        return strips;
    }

    public int getLedsPerStrip() {
        return ledsPerStrip;
    }



}
