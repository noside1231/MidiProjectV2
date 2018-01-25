import javafx.scene.paint.Color;

/**
 * Created by Edison on 1/19/18.
 */
public class TriggeredNote {

    long timeStatus;
    Note note;
    long originalTriggerTime;

    int status; //0 fade in, 1 hold, 2 fade out

    public TriggeredNote(Note n, long t) {
        note = n;
        timeStatus = t;
        originalTriggerTime = t;
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
                System.out.println("NONE");
                break;
            case "Rainbow":
                tColors = applyRainbowPreset(tColors, t);
                break;
            case "Flash":
                System.out.println("NONE");
                break;
            case "Trail":
                System.out.println("NONE");
                break;
            case "Twinkle":
                System.out.println("NONE");
                break;
            case "Multi":
                System.out.println("NONE");
                break;
        }

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
        double curT = (t-originalTriggerTime)/1000000000.0;
        System.out.println(curT);

        double speed = curT*note.getPresetParameter("Rainbow", "Speed");
        double spread = note.getPresetParameter("Rainbow", "Spread");
        double offset = note.getPresetParameter("Rainbow", "Offset");
        double skip = note.getPresetParameter("Rainbow", "Skip");

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                cols[x][y] = Color.hsb((speed+(spread*x)+(y*offset))%360, 1, 1);
            }
        }
        return cols;
    }

    Color[][] applyFlashPreset(Color[][] cols, long t) {

        double curT = (t-originalTriggerTime)/1000000000.0;

        double frequency = note.getPresetParameter("Flash", "Speed");
        double length = note.getPresetParameter("Flash", "Spread");

        double period = 1/frequency;




        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {


//                cols[x][y]

            }
        }


        return cols;
    }


}
