import javafx.scene.paint.Color;
import org.json.JSONObject;

/**
 * Created by edisongrauman on 12/20/17.
 */

public class LEDMatrix {

    Led[][] leds;

    int strips;
    int ledsPerStrip;


    public LEDMatrix(int strips, int ledsPerStrip) {
        this.strips = strips;
        this.ledsPerStrip = ledsPerStrip;

        leds = new Led[ledsPerStrip][strips];

        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
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
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
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

    public void addToLED(int x, int y, Color c) {
        leds[x][y].addColor(c);
    }

    public void addToLED(Color[][] c) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                leds[x][y].addColor(c[x][y]);
            }
        }
    }

    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                tFile.put((Integer.toString(x) + " " + Integer.toString(y)), getLEDString(x, y));
            }
        }
        return tFile;
    }

    public void loadData(JSONObject currentFile) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                setLED(x, y, Color.web(currentFile.getString((Integer.toString(x) + " " + Integer.toString(y)))));
            }
        }
    }

    public void setMatrix(LEDMatrix c) {
        for (int y = 0; y < strips; y++) {
            for (int x = 0; x < ledsPerStrip; x++) {
                setLED(x,y,c.getLED(x,y));
            }
        }
    }




}
