import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Edison on 1/17/18.
 */
public class Mixer {

    LEDMatrix mixerMatrix;
    ArrayList<TriggeredNote> currentlyTriggeredNotes;
    LinkedList<TriggeredNote> cur;
//    ArrayList<TriggeredNote> currentlyTriggeredNotes;
    long currentTime;

    public Mixer(int strips, int ledsPerStrip) {

        mixerMatrix = new LEDMatrix(strips, ledsPerStrip);
        currentlyTriggeredNotes = new ArrayList<>();
//        cur = new LinkedList<>();
//
//        cur.
    }

    public Led[][] update(long curTime) {
        currentTime = curTime;

        mixerMatrix.reset();

//        System.out.println(currentlyTriggeredNotes.size());
        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            Note tNote = currentlyTriggeredNotes.get(i).getNote();
            if ((curTime-currentlyTriggeredNotes.get(i).getTimeTriggered())/1000000000.0 < currentlyTriggeredNotes.get(i).getNote().getHold()) {
                for (int y = 0; y < mixerMatrix.getStrips(); y++) {
                    for (int x = 0; x < mixerMatrix.getLedsPerStrip(); x++) {

//                        switch (currentlyTriggeredNotes.get(i).getStatus()) {
                        switch (1) {
                            case 0:
                                break;
                            case 1:
                                mixerMatrix.addToLED(x, y, tNote.getLED(x, y));
                                break;
                            case 2:
                                break;
                        }


                        mixerMatrix.addToLED(x, y, tNote.getLED(x, y));
                    }
                }
            } else {
                currentlyTriggeredNotes.remove(i);
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
