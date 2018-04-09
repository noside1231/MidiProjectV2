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
        clearDMXArr();

        iterator = currentlyTriggeredNotes.listIterator();

        while(iterator.hasNext()) {
            TriggeredNote tNote = iterator.next();

            if (tNote.getStatus() == 3) {
                iterator.remove();
            } else {
                mixerMatrix.addToLED(tNote.update(currentTime));
                addToDmxArr(tNote.getUpdatedDMX(currentTime));
            }
        }
        return mixerMatrix.getLEDS();
    }

    public DMXChannel[] updateDMX() {

        DMXChannel[] s = new DMXChannel[dmxChannels.length];
        for (int i = 0; i < dmxChannels.length; i++) {
            s[i] = new DMXChannel(i);
            s[i].setValue(dmxChannels[i]);

        }
        return s;
    }

    public LEDMatrix getMixerMatrix() {
        return mixerMatrix;
    }

    public void setTriggered(Note n) {

        //if retriggered currently triggered note, reset the note
        for (int j = 0; j < getCurrentlyTriggeredNotes().size(); j++) {
            if (n.getID() == getCurrentlyTriggeredNotes().get(j)) {
                resetNote(n.getID());
                return;
            }
        }

        TriggeredNote t = new TriggeredNote(n, currentTime);

        //end triggers
        if (t.getNote().getEndTrigger()) {
            iterator = currentlyTriggeredNotes.listIterator();
            while(iterator.hasNext()) {
                TriggeredNote tNote = iterator.next();
                tNote.setStatus(3);
            }
        }

        //trigger multi notes
        triggerMultiList.set(t.getTriggeredMulti());
        triggerMultiList.set(new ArrayList<>());

        currentlyTriggeredNotes.add(t);
    }

    public ArrayList<Integer> getCurrentlyTriggeredNotes() {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < currentlyTriggeredNotes.size(); i++) {
            a.add(currentlyTriggeredNotes.get(i).getNote().getID());
        }
        return a;
    }

    public SimpleObjectProperty<ArrayList<Integer>> getTriggerMultiList() {
        return triggerMultiList;
    }

    void resetNote(int id) {
        for (int j = 0; j < currentlyTriggeredNotes.size(); j++) {
            if (id == currentlyTriggeredNotes.get(j).getNote().getID()) {
                currentlyTriggeredNotes.get(j).resetTrigger(currentTime);
                return;
            }
        }
    }

    private void clearDMXArr() {
        for (int i = 0; i < dmxChannels.length; i++) {
            dmxChannels[i] = 0;
        }
    }

    private void addToDmxArr(int channel, int val) {
        dmxChannels[channel] += val;
        if (dmxChannels[channel] > 255) {
            dmxChannels[channel] = 255;
        }
    }

    private void addToDmxArr(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            addToDmxArr(i, arr[i]);
        }
    }


}
