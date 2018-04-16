import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Edison on 1/19/18.
 */
public class TriggeredNote {

    private long timeStatus;
    private Note note;
    private long originalTriggerTime;

    private ArrayList<Integer> triggeredMulti;


    private int status; //0 fade in, 1 hold, 2 fade out

    private double timePassed;

    public TriggeredNote(Note n, long t) {
        note = n;
        timeStatus = t;
        originalTriggerTime = t;
        triggeredMulti = new ArrayList<>();

        for(int i = 0; i < note.getMultiTrigger().length; i++) {
            if (note.getMultiTrigger()[i]) {
                triggeredMulti.add(i);
            }
        }
    }

    public ArrayList<Integer> getTriggeredMulti() {
        return triggeredMulti;
    }

    public long getTimeStatus() {
        return timeStatus;
    }

    public Note getNote() {
        return note;
    }

    public int getStatus() {
        return status;
    }

    public void resetTrigger(long t) {
        originalTriggerTime = t;
        timeStatus = t;
        status = 0;
    }

    public float getTotalTriggerDuration() {
        return note.getFadeIn()+note.getFadeOut()+note.getHold();
    }

    public double getTotalTriggerTimeRemaining() {
        return getTotalTriggerDuration()-timePassed;
    }

    public double getTimeRemainingPercentage() {
        return 2-(getTotalTriggerTimeRemaining()/getTotalTriggerDuration());
    }

    public void incrementStatus(long t) {
        timeStatus = t;
        status++;
    }

    void setStatus(int t) {
        status = t;
    }

    public Color[][] update(long t) {

        timePassed = (originalTriggerTime-t) / 1000000000.0;

        Color[][] tColors = new Color[note.getMatrix().ledsPerStrip][note.getMatrix().getStrips()];

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                tColors[x][y] = note.getLED(x, y);
            }
        }

        //apply preset
        for (int i = 0; i < 4; i++) {
            switch (note.getCurrentPreset()[i]) {
                case "None":
//                System.out.println("NONE");
                    break;
                case "Rainbow":
                    tColors = applyRainbowPreset(tColors, t, i);
                    break;
                case "Flash":
                    tColors = applyFlashPreset(tColors, t, i);
                    break;
                case "Trail":
                    tColors = applyTrailPreset(tColors, t, i);
                    break;
                case "Twinkle":
                    tColors = applyTwinklePreset(tColors, t, i);
                    break;
                case "Wave":
                    tColors = applyWavePreset(tColors, t, i);
            }
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

    int[] getUpdatedDMX(long t) {
        int[] vals = new int[note.getDmxValues().length];
        for (int i = 0; i < vals.length; i++) {
            if (note.getDmxValues()[i].getChecked()) {

                if (note.getDmxValues()[i].getStartVal() != 0 || note.getDmxValues()[i].getEndVal() != 0) {

                    double value = (t - originalTriggerTime) / 1000000000.0;
                    double end = note.getFadeIn() + note.getFadeOut() + note.getHold();

                    int start1 = 0;
                    double stop1 = end;
                    int start2 = note.getDmxValues()[i].getStartVal();
                    int stop2 = note.getDmxValues()[i].getEndVal();


                    vals[i] = (int)(start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1)));
                    if (vals[i] > 255) {
                        vals[i] = 255;
                    } else if (vals[i] < 0) {
                        vals[i] = 0;
                    }

//                            System.out.println(vals[i]);
                } else {
                    vals[i] = note.getDmxValues()[i].getValue();
                }


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

    Color[][] applyRainbowPreset(Color[][] cols, long t, int ind) {
        double curT = (t - originalTriggerTime) / 1000000000.0;

        double speed = curT * note.getPresetParameter("Rainbow", "Speed", ind);
        double spread = note.getPresetParameter("Rainbow", "Spread", ind);
        double offset = note.getPresetParameter("Rainbow", "Offset", ind);
        double skip = note.getPresetParameter("Rainbow", "Skip", ind);

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                if (cols[x][y].getRed() != 0 || cols[x][y].getGreen() != 0 || cols[x][y].getBlue() != 0) {
                    cols[x][y] = Color.hsb((speed + (spread * x) + (y * offset)) % 360, 1, 1);
                }
            }
        }
        return cols;
    }

    Color[][] applyFlashPreset(Color[][] cols, long t, int ind) {

        double curT = (t - originalTriggerTime) / 1000000000.0;

        double frequency = note.getPresetParameter("Flash", "Speed", ind);
        double length = note.getPresetParameter("Flash", "Spread", ind);

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

    Color[][] applyTrailPreset(Color[][] cols, long t, int ind) {
        double curT = (t - originalTriggerTime) / 1000000000.0;

        int speedX = note.getPresetParameter("Trail", "SpeedX", ind);
        int speedY = note.getPresetParameter("Trail", "SpeedY", ind);
        int lengthX = note.getPresetParameter("Trail", "LengthX", ind);
        int lengthY = note.getPresetParameter("Trail", "LengthY", ind);
        double skipX = note.getPresetParameter("Trail", "SkipX", ind);
        double skipd = note.getPresetParameter("Trail", "SkipY", ind);

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


    Color[][] applyTwinklePreset(Color[][] cols, long t, int ind) {

        int twinkleAmount = note.getPresetParameter("Twinkle", "Amount", ind);
        int variance = note.getPresetParameter("Twinkle", "Variance", ind);
        int fadeIn = note.getPresetParameter("Twinkle", "Fade In", ind);
        int hold = note.getPresetParameter("Twinkle", "Hold", ind);
        int fadeOut = note.getPresetParameter("Twinkle", "Fade Out", ind);

        return cols;
    }

    Color[][] applyWavePreset(Color[][] cols, long t, int ind) {
        double curT = (t - originalTriggerTime) / 1000000000.0;

        int f = note.getPresetParameter("Wave", "Frequency", ind);
        int s = note.getPresetParameter("Wave", "Speed", ind);
        int type = note.getPresetParameter("Wave", "WaveType", ind);

        double speed = Math.toRadians(s)*20;

        double frequency = Math.toRadians(f)*2;

        if (type == 0) {
            for (int y = 0; y < note.getMatrix().getStrips(); y++) {
                for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                    cols[x][y] = Color.color(cols[x][y].getRed() * ((Math.sin((curT*speed)+(frequency*x))+1)/2),
                            cols[x][y].getGreen() *((Math.sin((curT*speed)+(frequency*x))+1)/2),
                            cols[x][y].getBlue() * ((Math.sin((curT*speed)+(frequency*x))+1)/2));
                }
            }
        }
        else if (type == 1) {
            for (int y = 0; y < note.getMatrix().getStrips(); y++) {
                for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {

                    if (Math.sin((curT*speed)+(frequency*x)) > 0) {
                        cols[x][y] = Color.BLACK;
                    }


                }
            }
        }



        return cols;
    }


}
