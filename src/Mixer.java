import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Edison on 1/17/18.
 */
public class Mixer {

    LEDMatrix mixerMatrix;
    int dmxChannels[];
    LinkedList<TriggeredNote> currentlyTriggeredNotes;
    ListIterator<TriggeredNote> iterator;
    long currentTime;

    SimpleObjectProperty<ArrayList<Integer>> triggerMultiList;

    public Mixer(int strips, int ledsPerStrip, int ch) {

        mixerMatrix = new LEDMatrix(strips, ledsPerStrip);
        dmxChannels = new int[ch];
        currentlyTriggeredNotes = new LinkedList<>();
        triggerMultiList = new SimpleObjectProperty<>();
        triggerMultiList.set(new ArrayList<>());
    }

    public Led[][] update(long curTime) {
        currentTime = curTime;
        mixerMatrix.reset();

        iterator = currentlyTriggeredNotes.listIterator();

        while(iterator.hasNext()) {
            TriggeredNote tNote = iterator.next();

            if (tNote.getStatus() == 3) {
                iterator.remove();
            } else {
                mixerMatrix.addToLED(tNote.update(currentTime));
            }
        }
        return mixerMatrix.getLEDS();
    }

    public int[] updateDMX() {

        for (int i = 0; i < dmxChannels.length; i++) {
            dmxChannels[i] = 0;
        }
        return dmxChannels;
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
        TriggeredNote t = new TriggeredNote(n, currentTime);
        t.getTriggeredMulti().addListener(event -> triggerMulti(t.getTriggeredMulti().get()));
//        currentlyTriggeredNotes.add(new TriggeredNote(n, currentTime));
        currentlyTriggeredNotes.add(t);
    }

    ArrayList<Integer> getCurrentlyTriggeredNotes() {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            a.add(currentlyTriggeredNotes.get(i).getNote().getID());
        }
        return a;
    }

    void triggerMulti(ArrayList<Integer> t) {
        triggerMultiList.set(t);
    }

    public SimpleObjectProperty<ArrayList<Integer>> getTriggerMultiList() {
        return triggerMultiList;
    }


    void resetNote(int i) {
        for (int j = 0; j < currentlyTriggeredNotes.size(); j++) {
            if (i == currentlyTriggeredNotes.get(i).getNote().getID()) {
                currentlyTriggeredNotes.get(i).resetTrigger(currentTime);
                return;
            }
        }
    }


}
