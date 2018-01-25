import javafx.scene.paint.Color;

/**
 * Created by Edison on 1/19/18.
 */
public class TriggeredNote {

    long timeTriggered;
    Note note;

    int status; //0 fade in, 1 hold, 2 fade out

    public TriggeredNote(Note n, long t) {
        note = n;
        timeTriggered = t;
    }

    long getTimeTriggered() {
        return timeTriggered;
    }

    Note getNote() {
        return note;
    }

    int getStatus() {
        return status;
    }

    public void resetTrigger(long t) {
        timeTriggered = t;
        status = 0;
    }

    public void incrementStatus(long t) {
        timeTriggered = t;
        status++;
    }

    public Color[][] update(long t) {

        Color[][] tColors = new Color[note.getMatrix().ledsPerStrip][note.getMatrix().getStrips()];

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                    tColors[x][y] = note.getLED(x, y);
            }
        }

        switch (status) {
            case 0:
                if ((t - getTimeTriggered()) / 1000000000.0 >= note.getFadeIn() || note.getFadeIn() == 0) {
                    incrementStatus(t);
                    break;
                }
                //apply preset


                tColors = applyFadeIn(tColors, t);
                break;
            case 1:
                if ((t - getTimeTriggered()) / 1000000000.0 >= note.getHold() || note.getHold() == 0) {
                    incrementStatus(t);
                    break;
                }
                //apply preset


                break;
            case 2:
                if ((t - getTimeTriggered()) / 1000000000.0 >= note.getFadeOut() || note.getFadeOut() == 0) {
                    incrementStatus(t);
                    break;
                }
                //apply preset


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
        opacity = (t - timeTriggered) / (1000000000.0 * note.getFadeIn());

        for (int y = 0; y < note.getMatrix().getStrips(); y++) {
            for (int x = 0; x < note.getMatrix().getLedsPerStrip(); x++) {
                cols[x][y] = Color.color(cols[x][y].getRed() * opacity, cols[x][y].getGreen() * opacity, cols[x][y].getBlue() * opacity);
            }
        }
        return cols;
    }

    Color[][] applyFadeOut(Color[][] cols, long t) {
        double opacity;
        opacity = 1 - ((t - timeTriggered) / (1000000000.0 * note.getFadeOut()));

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


}
