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

    void incrementStatus(long t) {
        timeTriggered = t;
        status++;
    }


}
