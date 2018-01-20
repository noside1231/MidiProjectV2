/**
 * Created by Edison on 1/19/18.
 */
public class TriggeredNote {

    long timeTriggered;
    Note note;

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



}
