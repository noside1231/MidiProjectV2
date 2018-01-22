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

//        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            TriggeredNote tNote = iterator.next();
            if ((curTime-tNote.getTimeTriggered())/1000000000.0 < tNote.getNote().getHold()) {
                for (int y = 0; y < mixerMatrix.getStrips(); y++) {
                    for (int x = 0; x < mixerMatrix.getLedsPerStrip(); x++) {

//                        switch (currentlyTriggeredNotes.get(i).getStatus()) {
                        switch (1) {
                            case 0:
                                break;
                            case 1:
                                mixerMatrix.addToLED(x, y, tNote.getNote().getLED(x, y));
                                break;
                            case 2:
                                break;
                        }


                        mixerMatrix.addToLED(x, y, tNote.getNote().getLED(x, y));
                    }
                }
            } else {
                iterator.remove();
            }


        }

        return mixerMatrix.getLEDS();
    }

    public LEDMatrix getMixerMatrix() {
        return mixerMatrix;
    }

    public void setTriggered(Note n) {
        currentlyTriggeredNotes.add(new TriggeredNote(n, currentTime));
    }

}
