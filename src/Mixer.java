import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

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

            if (tNote.getStatus() == 3) {
                iterator.remove();
            } else {
                mixerMatrix.addToLED(tNote.update(currentTime));
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
                return;
            }
        }
    }


}
