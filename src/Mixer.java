import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Created by Edison on 1/17/18.
 */
public class Mixer {

    LEDMatrix mixerMatrix;
    LinkedList<TriggeredNote> currentlyTriggeredNotes;
    ListIterator<TriggeredNote> iterator;
    long currentTime;

    public Mixer(int strips, int ledsPerStrip) {

        mixerMatrix = new LEDMatrix(strips, ledsPerStrip);
        currentlyTriggeredNotes = new LinkedList<>();
    }

    public Led[][] update(long curTime) {
        currentTime = curTime;

        mixerMatrix.reset();


        iterator = currentlyTriggeredNotes.listIterator();

        while(iterator.hasNext()) {

            TriggeredNote tNote = iterator.next();
                for (int y = 0; y < mixerMatrix.getStrips(); y++) {
                    for (int x = 0; x < mixerMatrix.getLedsPerStrip(); x++) {

                        Color tColor = tNote.getNote().getLED(x, y);
                        double opacity;
                        switch (tNote.getStatus()) {
                            case 0:
                                if (tNote.getNote().getFadeIn() == 0) {
                                    tNote.incrementStatus(currentTime);
                                    break;
                                }
                                if ((curTime-tNote.getTimeTriggered())/1000000000.0 >= tNote.getNote().getFadeIn()) {
                                    tNote.incrementStatus(currentTime);
                                    break;
                                }
                                opacity = (currentTime-tNote.getTimeTriggered())/(1000000000.0*tNote.getNote().getFadeIn());
                                tColor = Color.color(tColor.getRed()*opacity, tColor.getGreen()*opacity, tColor.getBlue()*opacity);
                                mixerMatrix.addToLED(x, y, tColor);
                                break;
                            case 1:
                                if ((curTime-tNote.getTimeTriggered())/1000000000.0 >= tNote.getNote().getHold()) {
                                    tNote.incrementStatus(currentTime);
                                    break;
                                }
                                mixerMatrix.addToLED(x, y, tNote.getNote().getLED(x, y));
                                break;
                            case 2:
                                if (tNote.getNote().getFadeOut() == 0) {
                                    tNote.incrementStatus(currentTime);
                                    break;
                                }
                                if ((curTime-tNote.getTimeTriggered())/1000000000.0 >= tNote.getNote().getFadeOut()) {
                                    tNote.incrementStatus(currentTime);
                                    break;
                                }
                                opacity = 1-(currentTime-tNote.getTimeTriggered())/(1000000000.0*tNote.getNote().getFadeOut());
                                tColor = Color.color(tColor.getRed()*opacity, tColor.getGreen()*opacity, tColor.getBlue()*opacity);
                                mixerMatrix.addToLED(x, y, tColor);
                                break;
                            case 3:
                                try {
                                    iterator.remove();
                                } catch (Exception e) {}; //?
                                break;

                        }

                    }
                }
        }

        return mixerMatrix.getLEDS();
    }

    public LEDMatrix getMixerMatrix() {
        return mixerMatrix;
    }

    public void setTriggered(Note n) {

        for (int j = 0; j < getCurrentlyTriggeredNotes().size(); j++) {
            if (n.getID() == getCurrentlyTriggeredNotes().get(j)) {
                resetNote(n.getID());
                return;
            }
        }
        currentlyTriggeredNotes.add(new TriggeredNote(n, currentTime));
    }

    ArrayList<Integer> getCurrentlyTriggeredNotes() {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            a.add(currentlyTriggeredNotes.get(i).getNote().getID());
        }
        return a;
    }

    void resetNote(int i) {
        for (int j = 0; j < currentlyTriggeredNotes.size(); j++) {
            if (i == currentlyTriggeredNotes.get(i).getNote().getID()) {
                currentlyTriggeredNotes.get(i).resetTrigger(currentTime);
            }
        }
    }

}
