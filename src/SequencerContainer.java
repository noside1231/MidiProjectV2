import org.json.JSONObject;
import sun.awt.image.ImageWatched;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by edisongrauman on 3/6/18.
 */
public class SequencerContainer {


    private LinkedList<Sequencer> sequencers;

    private int currentSequencer = 0;

    private int lastAssignedID = 1;

    private String defaultName = "UNNAMED ";

    public SequencerContainer() {
        sequencers = new LinkedList<>();
        addSequencer();


    }

    public Sequencer getCurrentSequencer() {
        return sequencers.get(currentSequencer);
    }

    public void setCurrentSequencerNoteAmount(int i) {
        sequencers.get(currentSequencer).setNoteAmount(i);
    }

    public int getCurrentSequencerNoteAmount() {
        return sequencers.get(currentSequencer).getNoteAmount();
    }
    public void setCurrentSequencerNoteMapping(String s) {
        sequencers.get(currentSequencer).setNoteMapping(s);
    }

    public void setCurrentSequencer(String s) {
        ListIterator<Sequencer> iterator = sequencers.listIterator();

        int ind = 0;
        while (iterator.hasNext()) {
            Sequencer tSequencer = iterator.next();
//            System.out.println(ind + " " + tSequencer.getName() + "   " + s);
            if (tSequencer.getName().equals(s)) {
                currentSequencer = ind;
                return;
            }
            ind++;
        }
    }

    public void setCurrentSequencerNotePressed(String s) {
        sequencers.get(currentSequencer).setNotePressed(s);
    }

    public void setCurrentSequencerActiveChannel(String s) {
        sequencers.get(currentSequencer).setActiveChannel(s);
    }

    public void addSequencer() {
        sequencers.add(new Sequencer(lastAssignedID, "UNNAMED"+Integer.toString(lastAssignedID)));
        setCurrentSequencer("UNNAMED"+Integer.toString(lastAssignedID));
        lastAssignedID++;
    }

    public ArrayList<String> getSequencerList() {
        ArrayList<String> s = new ArrayList<>();

        ListIterator<Sequencer> iterator = sequencers.listIterator();
        while (iterator.hasNext()) {
            Sequencer tSequencer = iterator.next();
            s.add(tSequencer.getName());
        }
        return s;
    }

    public void removeSequencer() {
        sequencers.remove(currentSequencer);
        if (sequencers.isEmpty()) {
            addSequencer();
        }
        setCurrentSequencer(sequencers.getFirst().getName());
    }

    public void renameCurrentSequencer(String s) {

        if (!s.isEmpty()) {
            sequencers.get(currentSequencer).setName(s);
        }

    }

    public LinkedList<Sequencer> getSequencers() {
        return sequencers;
    }


    public JSONObject saveData() {
        JSONObject tFile = new JSONObject();

        int i;
        for (i = 0; i < sequencers.size(); i++) {
            tFile.put(Integer.toString(i), sequencers.get(i).saveData());
        }
        tFile.put("SequencerAmount", i);
        return tFile;
    }

    public void loadData(JSONObject tFile) {
        sequencers.clear();
        for (int i = 0; i < tFile.getInt("SequencerAmount"); i++) {
            addSequencer();
            sequencers.get(currentSequencer).loadData(tFile.getJSONObject(Integer.toString(i)));
        }


    }
}
