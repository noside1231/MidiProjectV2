import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Edison on 1/19/18.
 */
public class TriggeredNote {

    long timeStatus;
    Note note;
    long originalTriggerTime;

    SimpleObjectProperty<ArrayList<Integer>> triggeredMulti;

    int status; //0 fade in, 1 hold, 2 fade out

    public TriggeredNote(Note n, long t) {
        note = n;
        timeStatus = t;
        originalTriggerTime = t;
        triggeredMulti = new SimpleObjectProperty<>();
    }

    long getTimeStatus() {
        return timeStatus;
    }

    Note getNote() {
        return note;
    }

    int getStatus() {
        return status;
    }

    public void resetTrigger(long t) {
        originalTriggerTime = t;
        timeStatus = t;
        status = 0;
    }

    public void incrementStatus(long t) {
        timeStatus = t;
        status++;
    }

    void setStatus(int t) {
        status = t;
    }

    public Color[][] update(long t) {

        Color[][] tColors = new Color[note.getMatrix().ledsPerStrip][note.getMatrix().getStrips()];

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                tColors[x][y] = note.getLED(x, y);
            }
        }

        //apply preset
        switch (note.getCurrentPreset()) {
            case "None":
//                System.out.println("NONE");
                break;
            case "Rainbow":
                tColors = applyRainbowPreset(tColors, t);
                break;
            case "Flash":
                tColors = applyFlashPreset(tColors, t);
                break;
            case "Trail":
                tColors = applyTrailPreset(tColors, t);
                break;
            case "Twinkle":
                tColors = applyTwinklePreset(tColors, t);
                break;
            case "Multi":
                applyMultiPreset();
                setStatus(3);
                break;
        }

        //apply fade
        switch (status) {
            case 0:
                if ((t - getTimeStatus()) / 1000000000.0 >= note.getFadeIn() || note.getFadeIn() == 0) {
                    incrementStatus(t);
                    break;
                }
                tColors = applyFadeIn(tColors, t);
                break;
            case 1:
                if ((t - getTimeStatus()) / 1000000000.0 >= note.getHold() || note.getHold() == 0) {
                    incrementStatus(t);
                    break;
                }
                break;
            case 2:
                if ((t - getTimeStatus()) / 1000000000.0 >= note.getFadeOut() || note.getFadeOut() == 0) {
                    incrementStatus(t);
                    break;
                }
                tColors = applyFadeOut(tColors, t);
                break;
        }

        if (status != 3) {
            return tColors;
        } else {
            return blackOut(tColors);
        }

    }

    int[] getUpdatedDMX() {
        int[] vals = new int[note.getDmxValues().length];
        for (int i = 0; i < vals.length; i++) {
            if (note.getDmxValues()[i].getChecked()) {
                vals[i] = note.getDmxValues()[i].getValue();
            } else {
                vals[i] = 0;
            }
        }

        return vals;
    }

    Color[][] applyFadeIn(Color[][] cols, long t) {
        double opacity;
        opacity = (t - timeStatus) / (1000000000.0 * note.getFadeIn());

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                cols[x][y] = Color.color(cols[x][y].getRed() * opacity, cols[x][y].getGreen() * opacity, cols[x][y].getBlue() * opacity);
            }
        }
        return cols;
    }

    Color[][] applyFadeOut(Color[][] cols, long t) {
        double opacity;
        opacity = 1 - ((t - timeStatus) / (1000000000.0 * note.getFadeOut()));

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                cols[x][y] = Color.color(cols[x][y].getRed() * opacity, cols[x][y].getGreen() * opacity, cols[x][y].getBlue() * opacity);
            }
        }
        return cols;
    }

    Color[][] blackOut(Color[][] cols) {
        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                cols[x][y] = Color.BLACK;
            }
        }
        return cols;
    }

    Color[][] applyRainbowPreset(Color[][] cols, long t) {
        double curT = (t - originalTriggerTime) / 1000000000.0;

        double speed = curT * note.getPresetParameter("Rainbow", "Speed");
        double spread = note.getPresetParameter("Rainbow", "Spread");
        double offset = note.getPresetParameter("Rainbow", "Offset");
        double skip = note.getPresetParameter("Rainbow", "Skip");

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                if (cols[x][y].getRed() != 0 || cols[x][y].getGreen() != 0 || cols[x][y].getBlue() != 0) {
                    cols[x][y] = Color.hsb((speed + (spread * x) + (y * offset)) % 360, 1, 1);
                }
            }
        }
        return cols;
    }

    Color[][] applyFlashPreset(Color[][] cols, long t) {

        double curT = (t - originalTriggerTime) / 1000000000.0;

        double frequency = note.getPresetParameter("Flash", "Speed");
        double length = note.getPresetParameter("Flash", "Spread");

        boolean on = (curT * frequency - Math.floor(curT * frequency)) < length / 100;

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                if (!on) {
                    cols[x][y] = Color.BLACK;
                }
            }
        }
        return cols;
    }

    Color[][] applyTrailPreset(Color[][] cols, long t) {
        double curT = (t - originalTriggerTime) / 1000000000.0;

        int speedX = note.getPresetParameter("Trail", "SpeedX");
        int speedY = note.getPresetParameter("Trail", "SpeedY");
        int lengthX = note.getPresetParameter("Trail", "LengthX");
        int lengthY = note.getPresetParameter("Trail", "LengthY");
        double skipX = note.getPresetParameter("Trail", "SkipX");
        double skipd = note.getPresetParameter("Trail", "SkipY");

        int trailIndexX, trailIndexY;
        if (speedX < 0) {
            trailIndexX = note.getMatrix().getLedsPerStrip() - (int) (-curT * speedX) % (note.getMatrix().getLedsPerStrip() + lengthX);
        } else if (speedX > 0) {
            trailIndexX = -lengthX + (int) ((curT * speedX)) % (note.getMatrix().getLedsPerStrip() + lengthX);
        } else {
            trailIndexX = 0;
        }
        if (speedY < 0) {
            trailIndexY = note.getMatrix().getStrips() - (int) (-curT * speedY) % (note.getMatrix().getStrips() + lengthY);
        } else if (speedY > 0) {
            trailIndexY = -lengthY + (int) ((curT * speedY)) % (note.getMatrix().getStrips() + lengthY);
        } else {
            trailIndexY = 0;
        }


        Color[][] tCols = new Color[note.getMatrix().getLedsPerStrip()][note.getMatrix().getStrips()];

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {

                if (x >= 0 && x < note.getMatrix().getLedsPerStrip()) {
                    tCols[x][y] = Color.BLACK;
                }

                if (x >= trailIndexX && x < trailIndexX + lengthX && y >= trailIndexY && y < trailIndexY + lengthY) {
                    tCols[x][y] = cols[x][y];
                }


            }
        }


        return tCols;
    }

    void applyMultiPreset() {
        triggeredMulti.set(note.getMultiPreset());
        triggeredMulti.set(new ArrayList<>());
    }

    SimpleObjectProperty<ArrayList<Integer>> getTriggeredMulti() {
        return triggeredMulti;
    }

    Color[][] applyTwinklePreset(Color[][] cols, long t) {

        int twinkleAmount = note.getPresetParameter("Twinkle", "Amount");
        int variance = note.getPresetParameter("Twinkle", "Variance");
        int fadeIn = note.getPresetParameter("Twinkle", "Fade In");
        int hold = note.getPresetParameter("Twinkle", "Hold");
        int fadeOut = note.getPresetParameter("Twinkle", "Fade Out");

        return cols;
    }


}
